package com.synapse.social.studioasinc;

import java.io.File;

/**
 * This class is a temporary shim to fix a build error.
 * It was deleted and replaced with UploadFiles, but other activities still reference it.
 * This class now acts as an adapter, redirecting old calls to the new consolidated UploadFiles class.
 */
public class ImageUploader {

    public interface UploadCallback {
        void onUploadComplete(String imageUrl);
        void onUploadError(String errorMessage);
    }

    public static void uploadImage(String path, final UploadCallback legacyCallback) {
        if (path == null || legacyCallback == null) {
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            legacyCallback.onUploadError("File not found: " + path);
            return;
        }

        // Redirect the call to the new, consolidated UploadFiles class
        UploadFiles.uploadFile(path, file.getName(), new UploadFiles.UploadCallback() {
            @Override
            public void onProgress(int percent) {
                // The old callback didn't have progress, so this is ignored.
            }

            @Override
            public void onSuccess(String url, String publicId) {
                // On success, call the old callback's success method.
                legacyCallback.onUploadComplete(url);
            }

            @Override
            public void onFailure(String error) {
                // On failure, call the old callback's error method.
                legacyCallback.onUploadError(error);
            }
        });
    }
}
