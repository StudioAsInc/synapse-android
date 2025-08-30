package com.synapse.social.studioasinc

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class User(
    val uid: String = "",
    val username: String? = null,
    val nickname: String? = null,
    val avatar: String? = null,
    val banned: String? = "false"
)

class ChatSettingsRepository {

    private val database = FirebaseDatabase.getInstance().getReference("skyline")
    private val usersRef = database.child("users")
    private val blocklistRef = database.child("blocklist")
    private val chatsRef = database.child("chats")
    private val auth = FirebaseAuth.getInstance()

    suspend fun getUser(userId: String): User? {
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (continuation.isActive) {
                        try {
                            val user = snapshot.getValue(User::class.java)
                            continuation.resume(user?.copy(uid = snapshot.key ?: ""))
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(error.toException())
                    }
                }
            }
            val ref = usersRef.child(userId)
            ref.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation {
                ref.removeEventListener(listener)
            }
        }
    }

    suspend fun isUserBlocked(userId: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid == null) {
                continuation.resume(false)
                return@suspendCancellableCoroutine
            }

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (continuation.isActive) {
                        continuation.resume(snapshot.exists())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(error.toException())
                    }
                }
            }
            val ref = blocklistRef.child(currentUserUid).child(userId)
            ref.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation {
                ref.removeEventListener(listener)
            }
        }
    }

    suspend fun blockUser(userIdToBlock: String) {
        return suspendCancellableCoroutine { continuation ->
            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid == null) {
                continuation.resumeWithException(Exception("User not logged in"))
                return@suspendCancellableCoroutine
            }

            blocklistRef.child(currentUserUid).child(userIdToBlock).setValue(userIdToBlock)
                .addOnSuccessListener {
                    if (continuation.isActive) {
                        continuation.resume(Unit)
                    }
                }
                .addOnFailureListener {
                    if (continuation.isActive) {
                        continuation.resumeWithException(it)
                    }
                }
        }
    }
    suspend fun getChatSettings(otherUserId: String): ChatSettings? {
        val currentUserUid = auth.currentUser?.uid ?: return null
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (continuation.isActive) {
                        val settings = snapshot.getValue(ChatSettings::class.java)
                        continuation.resume(settings)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(error.toException())
                    }
                }
            }
            val ref = chatsRef.child(currentUserUid).child(otherUserId).child("settings")
            ref.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation {
                ref.removeEventListener(listener)
            }
        }
    }

    suspend fun saveReadReceiptsSetting(otherUserId: String, isEnabled: Boolean) {
        val currentUserUid = auth.currentUser?.uid ?: return
        chatsRef.child(currentUserUid).child(otherUserId).child("settings").child("readReceiptsEnabled").setValue(isEnabled)
        chatsRef.child(otherUserId).child(currentUserUid).child("settings").child("readReceiptsEnabled").setValue(isEnabled)
    }

    suspend fun saveDisappearingMessagesSetting(otherUserId: String, isEnabled: Boolean) {
        val currentUserUid = auth.currentUser?.uid ?: return
        chatsRef.child(currentUserUid).child(otherUserId).child("settings").child("disappearingMessagesEnabled").setValue(isEnabled)
        chatsRef.child(otherUserId).child(currentUserUid).child("settings").child("disappearingMessagesEnabled").setValue(isEnabled)
    }

    suspend fun saveAutoSaveMediaSetting(otherUserId: String, isEnabled: Boolean) {
        val currentUserUid = auth.currentUser?.uid ?: return
        chatsRef.child(currentUserUid).child(otherUserId).child("settings").child("autoSaveMediaEnabled").setValue(isEnabled)
        chatsRef.child(otherUserId).child(currentUserUid).child("settings").child("autoSaveMediaEnabled").setValue(isEnabled)
    }
}

data class ChatSettings(
    val readReceiptsEnabled: Boolean = true,
    val disappearingMessagesEnabled: Boolean = false,
    val autoSaveMediaEnabled: Boolean = false
)
