# E2E Encryption Implementation

This document describes the End-to-End encryption system implemented for the Synapse chat application.

## Overview

The E2E encryption system provides secure messaging by ensuring that:
- Messages are encrypted on the sender's device before transmission
- Only the intended recipient can decrypt messages using their private key
- Firebase serves only as a transport layer, never seeing plaintext messages
- Session keys are used for performance optimization

## Architecture

### 1. Key Management (`EncryptionKeyManager`)
- Generates RSA-2048 key pairs for each user
- Stores private keys securely in Android KeyStore
- Manages session keys for faster encryption
- Handles key rotation and cleanup

### 2. Message Encryption (`MessageEncryption`)
- Implements hybrid encryption (RSA + AES)
- RSA for key exchange, AES for message encryption
- Supports both RSA hybrid and session key modes
- Uses AES-GCM for authenticated encryption

### 3. Firebase Integration (`FirebaseEncryptionService`)
- Manages public key storage and retrieval
- Handles encrypted message storage
- Coordinates with Firebase Realtime Database

### 4. Chat Integration (`ChatEncryptionManager`)
- Main interface for the chat system
- Coordinates encryption/decryption workflow
- Manages message lifecycle

## How It Works

### Initial Setup
1. User signs up/logs in
2. `EncryptionKeyManager` generates RSA key pair
3. Public key is uploaded to Firebase
4. Private key remains on device only

### Sending Messages
1. Check if session key exists with recipient
2. If yes: Use AES session key for fast encryption
3. If no: Use RSA hybrid encryption (RSA for AES key, AES for message)
4. Generate session key for future messages
5. Store encrypted message in Firebase

### Receiving Messages
1. Firebase listener detects new message
2. `ChatEncryptionManager` decrypts using appropriate method
3. If session key available: Use AES decryption
4. If not: Use RSA decryption to get AES key, then decrypt message
5. Display decrypted message to user

## Security Features

- **RSA-2048**: Strong asymmetric encryption for key exchange
- **AES-256-GCM**: Fast symmetric encryption with authentication
- **Session Keys**: Performance optimization with frequent rotation
- **Android KeyStore**: Secure storage of private keys
- **No Plaintext Storage**: Firebase never sees unencrypted messages

## Usage

### Initialize Encryption
```java
ChatEncryptionManager encryptionManager = new ChatEncryptionManager(context);
encryptionManager.initializeCurrentUserEncryption(new ChatEncryptionManager.EncryptionCallback() {
    @Override
    public void onSuccess() {
        // Encryption ready
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### Send Encrypted Message
```java
encryptionManager.sendEncryptedMessage(
    messageText,
    recipientUid,
    attachments,
    repliedMessageId,
    new ChatEncryptionManager.MessageCallback() {
        @Override
        public void onSuccess(String messageId, EncryptedMessageModel encryptedMessage) {
            // Message sent successfully
        }
        
        @Override
        public void onError(String error) {
            // Handle error
        }
    }
);
```

### Decrypt Received Message
```java
encryptionManager.decryptReceivedMessage(
    encryptedMessageModel,
    new ChatEncryptionManager.DecryptCallback() {
        @Override
        public void onSuccess(String decryptedMessage) {
            // Display message
        }
        
        @Override
        public void onError(String error) {
            // Handle decryption error
        }
    }
);
```

## Integration with Existing Chat System

The encryption system is designed to work alongside the existing chat infrastructure:

1. **Message Sending**: Replace `_send_btn()` logic with encrypted message sending
2. **Message Receiving**: Modify Firebase listeners to decrypt incoming messages
3. **Database Structure**: Use existing chat nodes with encrypted content
4. **UI Updates**: Display decrypted messages in existing chat adapter

## Performance Considerations

- **Session Keys**: First message uses RSA (slower), subsequent use AES (faster)
- **Key Rotation**: Session keys can be rotated periodically for security
- **Batch Operations**: Multiple messages can share the same session key
- **Memory Management**: Keys are stored securely and cleaned up appropriately

## Security Best Practices

1. **Key Rotation**: Implement periodic key rotation
2. **Forward Secrecy**: Consider implementing Double Ratchet algorithm
3. **Key Verification**: Implement key fingerprint verification
4. **Audit Logging**: Log encryption/decryption events for security monitoring
5. **Error Handling**: Never expose encryption errors to users

## Future Enhancements

- **Double Ratchet**: Implement Signal Protocol for forward secrecy
- **Group Chat Encryption**: Extend to support encrypted group conversations
- **Key Backup**: Secure backup and recovery of encryption keys
- **Quantum Resistance**: Prepare for post-quantum cryptography
- **Cross-Platform**: Extend to web and iOS clients

## Troubleshooting

### Common Issues
1. **Key Generation Failure**: Check Android KeyStore permissions
2. **Decryption Errors**: Verify key synchronization between devices
3. **Performance Issues**: Check session key management
4. **Firebase Errors**: Verify database rules and permissions

### Debug Mode
Enable debug logging by setting appropriate log levels in each encryption class.

## Dependencies

The encryption system requires these dependencies in `build.gradle`:
```gradle
implementation 'org.bouncycastle:bcprov-jdk15on:1.70'
implementation 'org.bouncycastle:bcpkix-jdk15on:1.70'
implementation 'com.google.crypto.tink:tink-android:1.7.0'
```

## License

This encryption implementation follows industry best practices and is designed for production use.
