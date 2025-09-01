package com.synapse.social.studioasinc.util;

import android.util.Base64;
import android.util.Log;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MessageEncryption {
    private static final String TAG = "MessageEncryption";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String RSA_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    
    /**
     * Encrypts a message using RSA for key exchange and AES for the actual message
     * @param message The plaintext message to encrypt
     * @param recipientPublicKey The recipient's public key
     * @return EncryptedMessage object containing encrypted data and metadata
     */
    public static EncryptedMessage encryptMessage(String message, String recipientPublicKey) {
        try {
            // Generate a random AES key for this message
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(AES_KEY_SIZE);
            SecretKey aesKey = keyGen.generateKey();
            
            // Encrypt the AES key with recipient's RSA public key
            PublicKey publicKey = stringToPublicKey(recipientPublicKey);
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());
            
            // Encrypt the message with AES
            byte[] encryptedMessage = encryptWithAES(message, aesKey);
            
            // Create encrypted message object
            EncryptedMessage encryptedMsg = new EncryptedMessage();
            encryptedMsg.encryptedAesKey = Base64.encodeToString(encryptedAesKey, Base64.DEFAULT);
            encryptedMsg.encryptedMessage = Base64.encodeToString(encryptedMessage, Base64.DEFAULT);
            encryptedMsg.encryptionType = "RSA_AES_HYBRID";
            encryptedMsg.timestamp = System.currentTimeMillis();
            
            Log.d(TAG, "Message encrypted successfully");
            return encryptedMsg;
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to encrypt message", e);
            return null;
        }
    }
    
    /**
     * Decrypts a message using the recipient's private key
     * @param encryptedMessage The encrypted message object
     * @param recipientPrivateKey The recipient's private key
     * @return Decrypted plaintext message, or null if failed
     */
    public static String decryptMessage(EncryptedMessage encryptedMessage, PrivateKey recipientPrivateKey) {
        try {
            // Decrypt the AES key with RSA private key
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.DECRYPT_MODE, recipientPrivateKey);
            byte[] encryptedAesKey = Base64.decode(encryptedMessage.encryptedAesKey, Base64.DEFAULT);
            byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
            SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
            
            // Decrypt the message with AES
            byte[] encryptedMessageBytes = Base64.decode(encryptedMessage.encryptedMessage, Base64.DEFAULT);
            String decryptedMessage = decryptWithAES(encryptedMessageBytes, aesKey);
            
            Log.d(TAG, "Message decrypted successfully");
            return decryptedMessage;
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to decrypt message", e);
            return null;
        }
    }
    
    /**
     * Encrypts a message using a shared session key (faster than RSA)
     * @param message The plaintext message
     * @param sessionKey The shared AES session key
     * @return EncryptedMessage object
     */
    public static EncryptedMessage encryptWithSessionKey(String message, SecretKey sessionKey) {
        try {
            byte[] encryptedMessage = encryptWithAES(message, sessionKey);
            
            EncryptedMessage encryptedMsg = new EncryptedMessage();
            encryptedMsg.encryptedMessage = Base64.encodeToString(encryptedMessage, Base64.DEFAULT);
            encryptedMsg.encryptionType = "AES_SESSION";
            encryptedMsg.timestamp = System.currentTimeMillis();
            
            Log.d(TAG, "Message encrypted with session key successfully");
            return encryptedMsg;
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to encrypt message with session key", e);
            return null;
        }
    }
    
    /**
     * Decrypts a message using a shared session key
     * @param encryptedMessage The encrypted message
     * @param sessionKey The shared AES session key
     * @return Decrypted plaintext message
     */
    public static String decryptWithSessionKey(EncryptedMessage encryptedMessage, SecretKey sessionKey) {
        try {
            byte[] encryptedMessageBytes = Base64.decode(encryptedMessage.encryptedMessage, Base64.DEFAULT);
            String decryptedMessage = decryptWithAES(encryptedMessageBytes, sessionKey);
            
            Log.d(TAG, "Message decrypted with session key successfully");
            return decryptedMessage;
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to decrypt message with session key", e);
            return null;
        }
    }
    
    private static byte[] encryptWithAES(String message, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        
        // Generate random IV
        byte[] iv = new byte[GCM_IV_LENGTH];
        SECURE_RANDOM.nextBytes(iv);
        
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        
        byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        
        // Combine IV and encrypted data
        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
        
        return result;
    }
    
    private static String decryptWithAES(byte[] encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        
        // Extract IV and encrypted data
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encrypted = new byte[encryptedData.length - GCM_IV_LENGTH];
        
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);
        System.arraycopy(encryptedData, iv.length, encrypted, 0, encrypted.length);
        
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
    
    private static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.decode(publicKeyString, Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }
    
    /**
     * Data class for encrypted messages
     */
    public static class EncryptedMessage {
        public String encryptedAesKey; // For RSA hybrid encryption
        public String encryptedMessage;
        public String encryptionType; // "RSA_AES_HYBRID" or "AES_SESSION"
        public long timestamp;
        
        public EncryptedMessage() {}
    }
}
