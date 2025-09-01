package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionKeyManager {
    private static final String TAG = "EncryptionKeyManager";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String KEY_ALIAS_PREFIX = "synapse_e2e_";
    private static final String PREFS_NAME = "encryption_keys";
    private static final String SESSION_KEYS_PREFIX = "session_keys_";
    
    private final Context context;
    private final SharedPreferences prefs;
    private final KeyStore keyStore;
    
    public EncryptionKeyManager(Context context) {
        this.context = context;
        try {
            // Use EncryptedSharedPreferences for secure storage of sensitive data
            MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
            
            this.prefs = EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            
            this.keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            this.keyStore.load(null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize KeyStore", e);
            throw new RuntimeException("Failed to initialize encryption", e);
        }
    }
    
    public boolean generateUserKeyPair(String userId) {
        try {
            String keyAlias = KEY_ALIAS_PREFIX + userId;
            
            if (keyStore.containsAlias(keyAlias)) {
                Log.d(TAG, "Key pair already exists for user: " + userId);
                return true;
            }
            
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER);
            
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .setKeySize(2048)
                .setUserAuthenticationRequired(false)
                .build();
            
            keyPairGenerator.initialize(spec);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            
            String publicKeyBase64 = Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.DEFAULT);
            prefs.edit().putString("public_key_" + userId, publicKeyBase64).commit();
            
            Log.d(TAG, "Generated new RSA key pair for user: " + userId);
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to generate key pair for user: " + userId, e);
            return false;
        }
    }
    
    public String getPublicKey(String userId) {
        return prefs.getString("public_key_" + userId, null);
    }
    
    public PrivateKey getPrivateKey(String userId) {
        try {
            String keyAlias = KEY_ALIAS_PREFIX + userId;
            if (!keyStore.containsAlias(keyAlias)) {
                return null;
            }
            
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias, null);
            return entry.getPrivateKey();
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to get private key for user: " + userId, e);
            return null;
        }
    }
    
    public SecretKey generateSessionKey(String userId1, String userId2) {
        try {
            byte[] sessionKeyBytes = new byte[32];
            SECURE_RANDOM.nextBytes(sessionKeyBytes);
            
            SecretKey sessionKey = new SecretKeySpec(sessionKeyBytes, "AES");
            
            String sessionKeyId = getSessionKeyId(userId1, userId2);
            String sessionKeyBase64 = Base64.encodeToString(sessionKeyBytes, Base64.DEFAULT);
            prefs.edit().putString(SESSION_KEYS_PREFIX + sessionKeyId, sessionKeyBase64).commit();
            
            Log.d(TAG, "Generated new session key for users: " + userId1 + " and " + userId2);
            return sessionKey;
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to generate session key", e);
            return null;
        }
    }
    
    public SecretKey getSessionKey(String userId1, String userId2) {
        try {
            String sessionKeyId = getSessionKeyId(userId1, userId2);
            String sessionKeyBase64 = prefs.getString(SESSION_KEYS_PREFIX + sessionKeyId, null);
            
            if (sessionKeyBase64 == null) {
                return null;
            }
            
            byte[] sessionKeyBytes = Base64.decode(sessionKeyBase64, Base64.DEFAULT);
            return new SecretKeySpec(sessionKeyBytes, "AES");
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to get session key", e);
            return null;
        }
    }
    
    private String getSessionKeyId(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }
    
    public boolean hasKeys(String userId) {
        try {
            String keyAlias = KEY_ALIAS_PREFIX + userId;
            return keyStore.containsAlias(keyAlias) && getPublicKey(userId) != null;
        } catch (Exception e) {
            Log.e(TAG, "Failed to check keys for user: " + userId, e);
            return false;
        }
    }
}
