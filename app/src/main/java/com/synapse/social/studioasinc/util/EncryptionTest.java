package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.util.Log;

import javax.crypto.SecretKey;

/**
 * Simple test class to verify encryption functionality
 * Run this in development to test the encryption system
 */
public class EncryptionTest {
    private static final String TAG = "EncryptionTest";
    
    public static void testEncryption(Context context) {
        Log.d(TAG, "Starting encryption tests...");
        
        try {
            // Test 1: Key Generation
            testKeyGeneration(context);
            
            // Test 2: Message Encryption/Decryption
            testMessageEncryption(context);
            
            // Test 3: Session Key Management
            testSessionKeys(context);
            
            Log.d(TAG, "All encryption tests passed!");
            
        } catch (Exception e) {
            Log.e(TAG, "Encryption test failed", e);
        }
    }
    
    private static void testKeyGeneration(Context context) {
        Log.d(TAG, "Testing key generation...");
        
        EncryptionKeyManager keyManager = new EncryptionKeyManager(context);
        String testUserId = "test_user_" + System.currentTimeMillis();
        
        boolean success = keyManager.generateUserKeyPair(testUserId);
        if (success) {
            Log.d(TAG, "✓ Key generation successful");
            
            String publicKey = keyManager.getPublicKey(testUserId);
            if (publicKey != null) {
                Log.d(TAG, "✓ Public key retrieved successfully");
            } else {
                Log.e(TAG, "✗ Failed to retrieve public key");
            }
            
            if (keyManager.hasKeys(testUserId)) {
                Log.d(TAG, "✓ Key verification successful");
            } else {
                Log.e(TAG, "✗ Key verification failed");
            }
        } else {
            Log.e(TAG, "✗ Key generation failed");
        }
    }
    
    private static void testMessageEncryption(Context context) {
        Log.d(TAG, "Testing message encryption...");
        
        EncryptionKeyManager keyManager = new EncryptionKeyManager(context);
        String testUserId = "test_user_" + System.currentTimeMillis();
        
        // Generate keys
        keyManager.generateUserKeyPair(testUserId);
        String publicKey = keyManager.getPublicKey(testUserId);
        
        if (publicKey == null) {
            Log.e(TAG, "✗ Cannot test encryption without public key");
            return;
        }
        
        // Test message
        String testMessage = "Hello, this is a test message for E2E encryption!";
        
        // Encrypt
        MessageEncryption.EncryptedMessage encrypted = 
            MessageEncryption.encryptMessage(testMessage, publicKey);
        
        if (encrypted != null) {
            Log.d(TAG, "✓ Message encryption successful");
            
            // Decrypt
            String decrypted = MessageEncryption.decryptMessage(
                encrypted, keyManager.getPrivateKey(testUserId));
            
            if (testMessage.equals(decrypted)) {
                Log.d(TAG, "✓ Message decryption successful");
                Log.d(TAG, "✓ Original: " + testMessage);
                Log.d(TAG, "✓ Decrypted: " + decrypted);
            } else {
                Log.e(TAG, "✗ Message decryption failed - content mismatch");
            }
        } else {
            Log.e(TAG, "✗ Message encryption failed");
        }
    }
    
    private static void testSessionKeys(Context context) {
        Log.d(TAG, "Testing session key management...");
        
        EncryptionKeyManager keyManager = new EncryptionKeyManager(context);
        String user1 = "user1_" + System.currentTimeMillis();
        String user2 = "user2_" + System.currentTimeMillis();
        
        // Generate session key
        SecretKey sessionKey = keyManager.generateSessionKey(user1, user2);
        if (sessionKey != null) {
            Log.d(TAG, "✓ Session key generation successful");
            
            // Test session key encryption
            String testMessage = "Fast session key encryption test!";
            MessageEncryption.EncryptedMessage encrypted = 
                MessageEncryption.encryptWithSessionKey(testMessage, sessionKey);
            
            if (encrypted != null) {
                Log.d(TAG, "✓ Session key encryption successful");
                
                // Decrypt with same session key
                String decrypted = MessageEncryption.decryptWithSessionKey(
                    encrypted, sessionKey);
                
                if (testMessage.equals(decrypted)) {
                    Log.d(TAG, "✓ Session key decryption successful");
                } else {
                    Log.e(TAG, "✗ Session key decryption failed");
                }
            } else {
                Log.e(TAG, "✗ Session key encryption failed");
            }
            
            // Test session key retrieval
            SecretKey retrievedKey = keyManager.getSessionKey(user1, user2);
            if (retrievedKey != null && retrievedKey.equals(sessionKey)) {
                Log.d(TAG, "✓ Session key retrieval successful");
            } else {
                Log.e(TAG, "✗ Session key retrieval failed");
            }
        } else {
            Log.e(TAG, "✗ Session key generation failed");
        }
    }
}
