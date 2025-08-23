/*
ImageUploader - Modern Android image uploader for ImgBB API
Copyright (c) 2025 Ashik (StudioAs Inc.)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.synapse.social.studioasinc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUploader {

    public interface UploadCallback {
        void onUploadComplete(String imageUrl);

        void onUploadError(String errorMessage);
    }

    public static void uploadImage(String filePath, UploadCallback callback) {
        new ImageUploadTask(callback, false).execute(filePath);
    }
    
    public static void uploadImageWithCompression(String filePath, UploadCallback callback, boolean enableCompression) {
        new ImageUploadTask(callback, enableCompression).execute(filePath);
    }
    
    public static void uploadImageWithCompression(String filePath, UploadCallback callback) {
        // Default compression enabled
        new ImageUploadTask(callback, true).execute(filePath);
    }

    private static class ImageUploadTask extends AsyncTask<String, Void, String> {
        private UploadCallback callback;
        private boolean enableCompression;
        private static final int MAX_FILE_SIZE_BYTES = 2 * 1024 * 1024; // 2MB

        public ImageUploadTask(UploadCallback callback, boolean enableCompression) {
            this.callback = callback;
            this.enableCompression = enableCompression;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String filePath = params[0];
                File originalFile = new File(filePath);
                
                // Compress image if enabled and file is larger than 2MB
                File fileToUpload = originalFile;
                if (enableCompression && originalFile.length() > MAX_FILE_SIZE_BYTES) {
                    Log.d("ImageUploader", "Compressing image from " + (originalFile.length() / 1024 / 1024) + "MB");
                    fileToUpload = compressImage(originalFile);
                    if (fileToUpload != null) {
                        Log.d("ImageUploader", "Compressed to " + (fileToUpload.length() / 1024 / 1024) + "MB");
                    }
                }
                
                String boundary = "*****";
                String lineEnd = "\r\n";
                String twoHyphens = "--";

                // Set your server URL for image upload
                URL url = new URL("https://api.imgbb.com/1/upload?expiration=0&key=faa85ffbac0217ff67b5f3c4baa7fb29");
                
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                FileInputStream fileInputStream = new FileInputStream(fileToUpload);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                fileInputStream.close();
                dos.flush();
                dos.close();

                // Continue with your code to handle the response from the server

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith("Error")) {
                callback.onUploadError(result);
            } else {
                handleUploadResponse(result);
            }
        }

        private void handleUploadResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);

                // Assuming the image URL is under the key "data" and "url"
                JSONObject data = jsonResponse.getJSONObject("data");
                String imageUrl = data.getString("url");

                // Pass the image URL to the callback
                callback.onUploadComplete(imageUrl);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onUploadError("Error parsing JSON response");
            }
        }
        
        private File compressImage(File originalFile) {
            try {
                // Decode image size first
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(originalFile.getAbsolutePath(), options);
                
                // Calculate inSampleSize for compression
                options.inSampleSize = calculateInSampleSize(options, 1920, 1080); // Max dimensions
                options.inJustDecodeBounds = false;
                
                // Decode bitmap with new options
                Bitmap bitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), options);
                if (bitmap == null) {
                    Log.e("ImageUploader", "Failed to decode image for compression");
                    return originalFile;
                }
                
                // Create compressed file
                File compressedFile = new File(originalFile.getParent(), "compressed_" + originalFile.getName());
                FileOutputStream fos = new FileOutputStream(compressedFile);
                
                // Compress with quality 85 (good balance between size and quality)
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
                byte[] compressedData = baos.toByteArray();
                
                // Write to file
                fos.write(compressedData);
                fos.close();
                baos.close();
                
                // Recycle bitmap
                bitmap.recycle();
                
                // Check if compression was successful and file is under 2MB
                if (compressedFile.length() <= MAX_FILE_SIZE_BYTES) {
                    Log.d("ImageUploader", "Compression successful: " + (compressedFile.length() / 1024 / 1024) + "MB");
                    return compressedFile;
                } else {
                    Log.w("ImageUploader", "Compressed file still too large: " + (compressedFile.length() / 1024 / 1024) + "MB");
                    compressedFile.delete();
                    return originalFile;
                }
                
            } catch (Exception e) {
                Log.e("ImageUploader", "Error compressing image: " + e.getMessage());
                return originalFile;
            }
        }
        
        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
            
            return inSampleSize;
        }
    }
}