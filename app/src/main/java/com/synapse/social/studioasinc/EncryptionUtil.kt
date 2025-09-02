package com.synapse.social.studioasinc

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object EncryptionUtil {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val AES_CIPHER_ALGORITHM = "AES/GCM/NoPadding"
    private const val KEY_ALIAS = "e2ee_secret_key"

    private fun getKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = getKeyStore()
        val secretKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return secretKeyEntry?.secretKey ?: generateSecretKey()
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val parameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        }.build()
        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }

    fun encrypt(value: String): String? {
        return try {
            val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
            val combined = iv + encrypted
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (ex: GeneralSecurityException) {
            Log.e("EncryptionUtil", "Encryption failed", ex)
            null
        }
    }

    fun decrypt(encrypted: String): String {
        return try {
            val combined = Base64.decode(encrypted, Base64.DEFAULT)
            val iv = combined.sliceArray(0..11)
            val encryptedBytes = combined.sliceArray(12 until combined.size)
            val cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM)
            val ivParameterSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), ivParameterSpec)
            val original = cipher.doFinal(encryptedBytes)
            String(original, Charsets.UTF_8)
        } catch (ex: GeneralSecurityException) {
            Log.e("EncryptionUtil", "Decryption failed", ex)
            "[Decryption Failed]"
        }
    }
}
