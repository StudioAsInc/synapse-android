package com.synapse.social.studioasinc;

import android.os.Handler;
import android.os.Looper;
import android.webkit.MimeTypeMap;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class UploadFiles {

    // --- CALLBACKS ---
    public interface UploadCallback {
        void onProgress(int percent);
        void onSuccess(String url, String publicId);
        void onFailure(String error);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String error);
    }

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    // --- CLOUDINARY CONFIGURATION ---
    private static final String CLOUDINARY_API_KEY = "577882927131931";
    private static final String CLOUDINARY_API_SECRET = "M_w_0uQKjnLRUe-u34driUBqUQU";
    private static final String CLOUDINARY_CLOUD_NAME = "djw3fgbls";
    private static final String CLOUDINARY_UPLOAD_URL = "https://api.cloudinary.com/v1_1/" + CLOUDINARY_CLOUD_NAME + "/auto/upload";

    private static final Map<String, HttpURLConnection> activeUploads = new HashMap<>();

    /**
     * Main upload method. All files are sent to Cloudinary.
     */
    public static void uploadFile(String filePath, String fileName, UploadCallback callback) {
        new Thread(() -> {
            uploadToCloudinary(filePath, fileName, callback);
        }).start();
    }

    /**
     * Handles uploading files to Cloudinary.
     */
    private static void uploadToCloudinary(String filePath, String fileName, UploadCallback callback) {
        String boundary = "----CloudinaryBoundary" + System.currentTimeMillis();
        String LF = "\r\n";
        File file = new File(filePath);
        if (!file.exists()) {
            postFailure(callback, "File not found");
            return;
        }

        try {
            long ts = System.currentTimeMillis() / 1000;
            String rawSig = "timestamp=" + ts + CLOUDINARY_API_SECRET;
            String signature = sha1Hex(rawSig);

            URL url = new URL(CLOUDINARY_UPLOAD_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setChunkedStreamingMode(64 * 1024);
            activeUploads.put(filePath, conn);

            DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(conn.getOutputStream(), 64 * 1024)
            );

            writeFormField(out, boundary, "api_key", CLOUDINARY_API_KEY);
            writeFormField(out, boundary, "timestamp", String.valueOf(ts));
            writeFormField(out, boundary, "signature", signature);
            writeFileField(out, boundary, "file", file, fileName, callback);

            out.writeBytes("--" + boundary + "--" + LF);
            out.flush();
            out.close();

            int status = conn.getResponseCode();
            InputStream respStream = (status == 200 ? conn.getInputStream() : conn.getErrorStream());
            StringBuilder resp = new StringBuilder();
            int c;
            while ((c = respStream.read()) != -1) resp.append((char) c);
            respStream.close();
            activeUploads.remove(filePath);
            conn.disconnect();

            if (status == 200) {
                JSONObject json = new JSONObject(resp.toString());
                String urlStr = json.getString("secure_url");
                String publicId = json.getString("public_id");
                String resourceType = json.getString("resource_type");
                postSuccess(callback, urlStr, publicId + "|" + resourceType);
            } else {
                postFailure(callback, "Cloudinary Upload failed: " + resp);
            }
        } catch (Exception e) {
            activeUploads.remove(filePath);
            postFailure(callback, "Cloudinary Exception: " + e.getMessage());
        }
    }

    /**
     * Deletes a file from Cloudinary.
     */
    public static void deleteByPublicId(String publicIdWithType, DeleteCallback callback) {
        if (publicIdWithType == null || publicIdWithType.isEmpty()) {
            postDeleteSuccess(callback);
            return;
        }

        String publicId = publicIdWithType;
        String resourceType = "raw"; // Default resource type

        if (publicIdWithType.contains("|")) {
            String[] parts = publicIdWithType.split("\\|", 2);
            publicId = parts[0];
            resourceType = parts[1];
        }

        final String finalPublicId = publicId;
        final String finalResourceType = resourceType;

        new Thread(() -> {
            try {
                long ts = System.currentTimeMillis() / 1000;
                String toSign = "public_id=" + finalPublicId + "&timestamp=" + ts + CLOUDINARY_API_SECRET;
                String signature = sha1Hex(toSign);

                String destroyUrl = "https://api.cloudinary.com/v1_1/"
                        + CLOUDINARY_CLOUD_NAME + "/" + finalResourceType + "/destroy";

                HttpURLConnection conn = (HttpURLConnection) new URL(destroyUrl).openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String body = "public_id=" + finalPublicId
                        + "&timestamp=" + ts
                        + "&api_key=" + CLOUDINARY_API_KEY
                        + "&signature=" + signature;

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(body);
                out.flush();
                out.close();

                int code = conn.getResponseCode();
                InputStream in = (code == 200 ? conn.getInputStream() : conn.getErrorStream());
                StringBuilder resp = new StringBuilder();
                int ch;
                while ((ch = in.read()) != -1) resp.append((char) ch);
                in.close();
                conn.disconnect();

                JSONObject js = new JSONObject(resp.toString());
                if (code == 200 && "ok".equals(js.optString("result"))) {
                    postDeleteSuccess(callback);
                } else {
                    postDeleteFailure(callback, "Delete failed (" + code + "): " + resp);
                }
            } catch (Exception e) {
                postDeleteFailure(callback, e.getMessage());
            }
        }).start();
    }

    public static void cancelUpload(String filePath) {
        HttpURLConnection c = activeUploads.remove(filePath);
        if (c != null) c.disconnect();
    }

    // --- HELPER METHODS ---

    private static void writeFormField(DataOutputStream o, String b, String name, String val) throws IOException {
        String LF = "\r\n";
        o.writeBytes("--" + b + LF);
        o.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LF + LF);
        o.writeBytes(val + LF);
    }

    private static void writeFileField(DataOutputStream o, String b, String field,
                                       File f, String fname, UploadCallback cb) throws IOException {
        String LF = "\r\n";
        o.writeBytes("--" + b + LF);
        o.writeBytes("Content-Disposition: form-data; name=\"" + field + "\"; filename=\"" + fname + "\"" + LF);
        o.writeBytes("Content-Type: " + getMimeType(getFileExtension(fname)) + LF + LF);
        FileInputStream in = new FileInputStream(f);
        byte[] buf = new byte[64 * 1024];
        long total = f.length(), sent = 0;
        int r;
        while ((r = in.read(buf)) != -1) {
            o.write(buf, 0, r);
            sent += r;
            postProgress(cb, (int)((sent * 100) / total));
        }
        in.close();
        o.writeBytes(LF);
    }

    private static String sha1Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] d = md.digest(s.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : d) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static String getFileExtension(String name) {
        int d = name.lastIndexOf('.');
        return d >= 0 ? name.substring(d + 1) : "";
    }

    private static String getMimeType(String ext) {
        String m = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
        return m != null ? m : "application/octet-stream";
    }

    // --- MAIN THREAD POSTING ---
    private static void postProgress(UploadCallback cb, int p) {
        if (cb != null) mainHandler.post(() -> cb.onProgress(p));
    }
    private static void postSuccess(UploadCallback cb, String url, String id) {
        if (cb != null) mainHandler.post(() -> cb.onSuccess(url, id));
    }
    private static void postFailure(UploadCallback cb, String e) {
        if (cb != null) mainHandler.post(() -> cb.onFailure(e));
    }
    private static void postDeleteSuccess(DeleteCallback cb) {
        if (cb != null) mainHandler.post(cb::onSuccess);
    }
    private static void postDeleteFailure(DeleteCallback cb, String e) {
        if (cb != null) mainHandler.post(() -> cb.onFailure(e));
    }
}
