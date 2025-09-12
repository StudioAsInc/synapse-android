package com.synapse.social.studioasinc.crypto;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class E2EEHelper {

    private static final String TAG = "E2EEHelper";
    private static final String IDENTITY_KEY_ALIAS_PREFIX = "e2ee_identity_";
    private static final String E2EE_PUBLIC_KEYS_REF = "skyline/e2ee_public_keys";
    // Temporary switch to disable encryption to avoid Keystore-related crashes.
    // When true, all crypto operations will be no-ops and plaintext will be used.
    private static final boolean ENCRYPTION_DISABLED = true;

    private Context context;
    private String uid;

    public E2EEHelper(Context context) {
        this.context = context;
        // Ensure user is authenticated before using this helper
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            // Handle the case where user is null, maybe throw an exception
            // or ensure this helper is only instantiated for authenticated users.
            Log.e(TAG, "E2EEHelper initialized without an authenticated user.");
        }
    }

    public void initializeKeys(final KeysInitializationListener listener) {
        if (ENCRYPTION_DISABLED) {
            if (listener != null) listener.onKeysInitialized();
            return;
        }
        if (uid == null) {
            listener.onKeyInitializationFailed(new IllegalStateException("User is not authenticated."));
            return;
        }
        if (!identityKeyExists()) {
            Log.d(TAG, "Identity key does not exist. Generating new keys.");
            generateAndStoreIdentityKeyPair(listener);
        } else {
            Log.d(TAG, "Identity key already exists.");
            if (listener != null) {
                listener.onKeysInitialized();
            }
        }
    }

    private boolean identityKeyExists() {
        return context.getSharedPreferences("e2ee", Context.MODE_PRIVATE)
                .contains(getIdentityKeyAlias());
    }

    private void generateAndStoreIdentityKeyPair(final KeysInitializationListener listener) {
        try {
            IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();

            String privateKeyBase64 = Base64.encodeToString(identityKeyPair.getPrivateKey().serialize(), Base64.NO_WRAP);
            context.getSharedPreferences("e2ee", Context.MODE_PRIVATE)
                    .edit()
                    .putString(getIdentityKeyAlias(), privateKeyBase64)
                    .apply();

            publishIdentityPublicKey(identityKeyPair.getPublicKey().serialize(), listener);

        } catch (Exception e) {
            Log.e(TAG, "Error generating and storing identity key pair", e);
            if (listener != null) {
                listener.onKeyInitializationFailed(e);
            }
        }
    }

    private void publishIdentityPublicKey(byte[] publicKeyBytes, final KeysInitializationListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(E2EE_PUBLIC_KEYS_REF).child(uid);
        String publicKeyBase64 = Base64.encodeToString(publicKeyBytes, Base64.NO_WRAP);

        HashMap<String, Object> publicKeyMap = new HashMap<>();
        publicKeyMap.put("identityKey", publicKeyBase64);
        publicKeyMap.put("timestamp", System.currentTimeMillis());

        databaseReference.setValue(publicKeyMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Successfully published identity public key to Firebase.");
                if (listener != null) {
                    listener.onKeysInitialized();
                }
            } else {
                Log.e(TAG, "Failed to publish identity public key to Firebase.", task.getException());
                if (listener != null) {
                    listener.onKeyInitializationFailed(task.getException());
                }
            }
        });
    }

    public void getPublicKey(String otherUserUid, final PublicKeyListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(E2EE_PUBLIC_KEYS_REF).child(otherUserUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("identityKey")) {
                    String publicKeyBase64 = dataSnapshot.child("identityKey").getValue(String.class);
                    if (publicKeyBase64 != null) {
                        byte[] publicKeyBytes = Base64.decode(publicKeyBase64, Base64.NO_WRAP);
                        listener.onPublicKeyReceived(publicKeyBytes);
                    } else {
                        listener.onPublicKeyFailed(new Exception("Public key is null in database."));
                    }
                } else {
                    listener.onPublicKeyFailed(new Exception("Public key not found for user: " + otherUserUid));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onPublicKeyFailed(databaseError.toException());
            }
        });
    }

    private ECPrivateKey loadPrivateKey() throws InvalidKeyException {
        String privateKeyBase64 = context.getSharedPreferences("e2ee", Context.MODE_PRIVATE)
                .getString(getIdentityKeyAlias(), null);
        if (privateKeyBase64 != null) {
            byte[] privateKeyBytes = Base64.decode(privateKeyBase64, Base64.NO_WRAP);
            return Curve.decodePrivatePoint(privateKeyBytes);
        }
        return null;
    }

    public void establishSession(String otherUserUid, byte[] otherUserPublicKeyBytes, final SessionEstablishmentListener listener) {
        if (ENCRYPTION_DISABLED) {
            if (listener != null) listener.onSessionEstablished();
            return;
        }
        try {
            ECPrivateKey myPrivateKey = loadPrivateKey();
            if (myPrivateKey == null) {
                listener.onSessionEstablishmentFailed(new Exception("Could not load own private key."));
                return;
            }

            ECPublicKey theirPublicKey = Curve.decodePoint(otherUserPublicKeyBytes, 0);

            byte[] sharedSecret = Curve.calculateAgreement(theirPublicKey, myPrivateKey);

            String sharedSecretBase64 = Base64.encodeToString(sharedSecret, Base64.NO_WRAP);
            context.getSharedPreferences("e2ee_sessions", Context.MODE_PRIVATE)
                    .edit()
                    .putString(otherUserUid, sharedSecretBase64)
                    .apply();

            Log.d(TAG, "Successfully established session with " + otherUserUid);
            listener.onSessionEstablished();

        } catch (InvalidKeyException e) {
            Log.e(TAG, "Error establishing session", e);
            listener.onSessionEstablishmentFailed(e);
        }
    }

    public String encrypt(String otherUserUid, String plaintext) throws Exception {
        if (ENCRYPTION_DISABLED) {
            return plaintext;
        }
        String sessionKeyBase64 = context.getSharedPreferences("e2ee_sessions", Context.MODE_PRIVATE)
                .getString(otherUserUid, null);

        if (sessionKeyBase64 == null) {
            throw new Exception("Session not established with user: " + otherUserUid);
        }

        byte[] sessionKeyBytes = Base64.decode(sessionKeyBase64, Base64.NO_WRAP);
        byte[] keyBytes = new byte[32];
        System.arraycopy(sessionKeyBytes, 0, keyBytes, 0, Math.min(sessionKeyBytes.length, 32));
        SecretKey sessionKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.ENCRYPT_MODE, sessionKey, gcmParameterSpec);

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));

        byte[] ivAndCiphertext = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, ivAndCiphertext, 0, iv.length);
        System.arraycopy(ciphertext, 0, ivAndCiphertext, iv.length, ciphertext.length);

        return Base64.encodeToString(ivAndCiphertext, Base64.NO_WRAP);
    }

    public String decrypt(String senderUid, String ciphertextAndIv) throws Exception {
        if (ENCRYPTION_DISABLED) {
            return ciphertextAndIv;
        }
        String sessionKeyBase64 = context.getSharedPreferences("e2ee_sessions", Context.MODE_PRIVATE)
                .getString(senderUid, null);

        if (sessionKeyBase64 == null) {
            throw new Exception("Session not established with user: " + senderUid);
        }

        byte[] sessionKeyBytes = Base64.decode(sessionKeyBase64, Base64.NO_WRAP);
        byte[] keyBytes = new byte[32];
        System.arraycopy(sessionKeyBytes, 0, keyBytes, 0, Math.min(sessionKeyBytes.length, 32));
        SecretKey sessionKey = new SecretKeySpec(keyBytes, "AES");

        byte[] ivAndCiphertext = Base64.decode(ciphertextAndIv, Base64.NO_WRAP);

        byte[] iv = new byte[12];
        System.arraycopy(ivAndCiphertext, 0, iv, 0, 12);

        byte[] ciphertext = new byte[ivAndCiphertext.length - 12];
        System.arraycopy(ivAndCiphertext, 12, ciphertext, 0, ciphertext.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, sessionKey, gcmParameterSpec);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return new String(plaintext, "UTF-8");
    }

    private String getIdentityKeyAlias() {
        return IDENTITY_KEY_ALIAS_PREFIX + uid;
    }

    public interface KeysInitializationListener {
        void onKeysInitialized();
        void onKeyInitializationFailed(Exception e);
    }

    public interface PublicKeyListener {
        void onPublicKeyReceived(byte[] publicKey);
        void onPublicKeyFailed(Exception e);
    }

    public interface SessionEstablishmentListener {
        void onSessionEstablished();
        void onSessionEstablishmentFailed(Exception e);
    }
}
