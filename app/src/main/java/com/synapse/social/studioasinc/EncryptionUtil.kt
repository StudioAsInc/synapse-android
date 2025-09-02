package com.synapse.social.studioasinc

import android.content.Context
import android.util.Base64
import android.util.Log
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {

    private const val AES = "AES"
    private const val AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"
    private const val PREFS_NAME = "EncPrefs"
    private const val SECRET_KEY_ALIAS = "e2ee_secret_key"

    private fun getSecretKey(context: Context): SecretKey {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var encodedKey = prefs.getString(SECRET_KEY_ALIAS, null)
        if (encodedKey == null) {
            val keyGenerator = KeyGenerator.getInstance(AES)
            keyGenerator.init(256)
            val newKey = keyGenerator.generateKey()
            encodedKey = Base64.encodeToString(newKey.encoded, Base64.DEFAULT)
            prefs.edit().putString(SECRET_KEY_ALIAS, encodedKey).apply()
            return newKey
        }
        val decodedKey = Base64.decode(encodedKey, Base64.DEFAULT)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, AES)
    }

    fun encrypt(value: String, context: Context): String? {
        return try {
            val skeySpec = getSecretKey(context)
            val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)
            val ivBytes = ByteArray(16)
            SecureRandom().nextBytes(ivBytes)
            val iv = IvParameterSpec(ivBytes)
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            val encrypted = cipher.doFinal(value.toByteArray())
            val combined = ivBytes + encrypted
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun decrypt(encrypted: String, context: Context): String {
        return try {
            val skeySpec = getSecretKey(context)
            val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)
            val combined = Base64.decode(encrypted, Base64.DEFAULT)
            val ivBytes = combined.sliceArray(0..15)
            val encryptedBytes = combined.sliceArray(16 until combined.size)
            val iv = IvParameterSpec(ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val original = cipher.doFinal(encryptedBytes)
            String(original)
        } catch (ex: Exception) {
            Log.e("EncryptionUtil", "Decryption failed", ex)
            "[Decryption Failed]"
        }
    }
}
