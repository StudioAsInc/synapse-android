package com.synapse.social.studioasinc.crypto;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.HybridDecrypt;
import com.google.crypto.tink.HybridEncrypt;
import com.google.crypto.tink.InsecureSecretKeyAccess;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.TinkProtoKeysetFormat;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.hybrid.HybridConfig;
import com.google.crypto.tink.integration.android.AndroidKeysetManager;
import com.google.crypto.tink.signature.SignatureConfig;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class TinkE2EEHelper {

    private static final String TAG = "TinkE2EEHelper";
    private static final String KEYSET_NAME = "hybrid_private_keyset";
    private static final String MASTER_KEY_URI = "android-keystore://hybrid_master_key";
    private static final String PREFERENCE_FILE = "tink_preference";


    private KeysetHandle privateKeysetHandle;

    public TinkE2EEHelper(Context context) throws GeneralSecurityException, IOException {
        TinkConfig.register();
        initializeLocalKeyset(context);
    }

    private void initializeLocalKeyset(Context context) throws GeneralSecurityException, IOException {
        privateKeysetHandle = new AndroidKeysetManager.Builder()
                .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE)
                .withKeyTemplate(HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM)
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .getKeysetHandle();
    }

    public String getSerializedPublicKey() throws GeneralSecurityException, IOException {
        KeysetHandle publicKeysetHandle = privateKeysetHandle.getPublicKeysetHandle();
        byte[] serializedKeyset = TinkProtoKeysetFormat.serializeKeyset(publicKeysetHandle, InsecureSecretKeyAccess.get());
        return Base64.encodeToString(serializedKeyset, Base64.NO_WRAP);
    }

    public String encrypt(String serializedTheirPublicKey, String plaintext) throws GeneralSecurityException, IOException {
        byte[] theirPublicKeyBytes = Base64.decode(serializedTheirPublicKey, Base64.NO_WRAP);
        KeysetHandle theirPublicKeysetHandle = TinkProtoKeysetFormat.parseKeyset(theirPublicKeyBytes, InsecureSecretKeyAccess.get());

        HybridEncrypt hybridEncrypt = theirPublicKeysetHandle.getPrimitive(HybridEncrypt.class);

        byte[] ciphertext = hybridEncrypt.encrypt(plaintext.getBytes(), null);
        return Base64.encodeToString(ciphertext, Base64.NO_WRAP);
    }

    public String decrypt(String ciphertext) throws GeneralSecurityException {
        HybridDecrypt hybridDecrypt = privateKeysetHandle.getPrimitive(HybridDecrypt.class);
        byte[] plaintext = hybridDecrypt.decrypt(Base64.decode(ciphertext, Base64.NO_WRAP), null);
        return new String(plaintext);
    }
}
