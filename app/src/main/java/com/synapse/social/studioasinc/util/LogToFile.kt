package com.synapse.social.studioasinc.util

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LogToFile {
    private const val TAG = "LogToFile"
    private const val LOG_FILE_NAME = "chat_log.txt"

    fun log(context: Context, message: String) {
        val logMessage = "${getCurrentTimestamp()} - $message"
        Log.d("NotificationLogger", logMessage) // Also log to logcat for easier debugging

        try {
            val logFile = getLogFile(context)
            if (logFile != null) {
                FileWriter(logFile, true).use {
                    it.append(logMessage)
                    it.append("\n")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write to log file", e)
        }
    }

    private fun getLogFile(context: Context): File? {
        return try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            File(downloadsDir, LOG_FILE_NAME)
        } catch (e: Exception) {
            Log.e(TAG, "Could not get log file directory", e)
            null
        }
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
    }
}
