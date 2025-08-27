package com.synapse.social.studioasinc.groupchat.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCompressor @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val MAX_IMAGE_SIZE_KB = 500 // 500KB max for images
        private const val MAX_AVATAR_SIZE_KB = 100 // 100KB max for avatars
        private const val MAX_IMAGE_WIDTH = 1280
        private const val MAX_IMAGE_HEIGHT = 1280
        private const val MAX_AVATAR_SIZE = 512
        private const val COMPRESSION_QUALITY = 80
    }

    /**
     * Compress image for group chat messages
     */
    suspend fun compressImageForMessage(imageFile: File): File = withContext(Dispatchers.IO) {
        try {
            Compressor.compress(context, imageFile) {
                resolution(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT)
                quality(COMPRESSION_QUALITY)
                format(Bitmap.CompressFormat.JPEG)
                size(MAX_IMAGE_SIZE_KB * 1024L) // Convert to bytes
            }
        } catch (e: Exception) {
            // Fallback to manual compression if Compressor fails
            compressImageManually(imageFile, MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT, MAX_IMAGE_SIZE_KB)
        }
    }

    /**
     * Compress image for avatars (group icons, profile pictures)
     */
    suspend fun compressImageForAvatar(imageFile: File): File = withContext(Dispatchers.IO) {
        try {
            Compressor.compress(context, imageFile) {
                resolution(MAX_AVATAR_SIZE, MAX_AVATAR_SIZE)
                quality(COMPRESSION_QUALITY)
                format(Bitmap.CompressFormat.JPEG)
                size(MAX_AVATAR_SIZE_KB * 1024L) // Convert to bytes
            }
        } catch (e: Exception) {
            // Fallback to manual compression if Compressor fails
            compressImageManually(imageFile, MAX_AVATAR_SIZE, MAX_AVATAR_SIZE, MAX_AVATAR_SIZE_KB)
        }
    }

    /**
     * Manual image compression fallback
     */
    private suspend fun compressImageManually(
        imageFile: File,
        maxWidth: Int,
        maxHeight: Int,
        maxSizeKB: Int
    ): File = withContext(Dispatchers.IO) {
        try {
            // Decode image bounds first
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(imageFile.absolutePath, options)

            // Calculate sample size
            val sampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            
            // Decode image with sample size
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inJustDecodeBounds = false
            }
            
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, decodeOptions)
                ?: throw IOException("Failed to decode image")

            // Rotate image if needed based on EXIF data
            val rotatedBitmap = rotateImageIfRequired(bitmap, imageFile.absolutePath)

            // Scale bitmap to target dimensions
            val scaledBitmap = scaleBitmapToFit(rotatedBitmap, maxWidth, maxHeight)

            // Create output file
            val outputFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
            
            // Compress and save
            var quality = COMPRESSION_QUALITY
            do {
                FileOutputStream(outputFile).use { out ->
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                }
                quality -= 10
            } while (outputFile.length() > maxSizeKB * 1024 && quality > 10)

            // Clean up bitmaps
            if (rotatedBitmap != bitmap) {
                bitmap.recycle()
            }
            scaledBitmap.recycle()

            outputFile
        } catch (e: Exception) {
            // If all else fails, return the original file
            imageFile
        }
    }

    /**
     * Calculate sample size for efficient memory usage
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Rotate image based on EXIF orientation data
     */
    private fun rotateImageIfRequired(bitmap: Bitmap, imagePath: String): Bitmap {
        try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: Exception) {
            return bitmap
        }
    }

    /**
     * Rotate bitmap by specified degrees
     */
    private fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Scale bitmap to fit within specified dimensions while maintaining aspect ratio
     */
    private fun scaleBitmapToFit(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }

        val scaleX = maxWidth.toFloat() / width
        val scaleY = maxHeight.toFloat() / height
        val scale = minOf(scaleX, scaleY)

        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * Get estimated file size after compression
     */
    suspend fun getEstimatedCompressedSize(imageFile: File, isAvatar: Boolean = false): Long {
        return withContext(Dispatchers.IO) {
            try {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeFile(imageFile.absolutePath, options)

                val (maxWidth, maxHeight, maxSizeKB) = if (isAvatar) {
                    Triple(MAX_AVATAR_SIZE, MAX_AVATAR_SIZE, MAX_AVATAR_SIZE_KB)
                } else {
                    Triple(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT, MAX_IMAGE_SIZE_KB)
                }

                val sampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
                val compressedWidth = options.outWidth / sampleSize
                val compressedHeight = options.outHeight / sampleSize

                // Rough estimation: 3 bytes per pixel for JPEG at 80% quality
                val estimatedSize = (compressedWidth * compressedHeight * 3 * 0.8).toLong()
                minOf(estimatedSize, maxSizeKB * 1024L)
            } catch (e: Exception) {
                imageFile.length()
            }
        }
    }

    /**
     * Check if image needs compression
     */
    fun needsCompression(imageFile: File, isAvatar: Boolean = false): Boolean {
        val maxSizeBytes = if (isAvatar) {
            MAX_AVATAR_SIZE_KB * 1024L
        } else {
            MAX_IMAGE_SIZE_KB * 1024L
        }
        
        return imageFile.length() > maxSizeBytes
    }

    /**
     * Clean up temporary compressed files
     */
    fun cleanupTempFiles() {
        try {
            context.cacheDir.listFiles { file ->
                file.name.startsWith("compressed_")
            }?.forEach { file ->
                file.delete()
            }
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }
}