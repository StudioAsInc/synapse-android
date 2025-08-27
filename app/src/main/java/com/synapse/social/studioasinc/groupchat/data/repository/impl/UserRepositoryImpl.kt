package com.synapse.social.studioasinc.groupchat.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao
import com.synapse.social.studioasinc.groupchat.data.model.User
import com.synapse.social.studioasinc.groupchat.data.remote.FirebaseConstants
import com.synapse.social.studioasinc.groupchat.data.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseMessaging: FirebaseMessaging
) : UserRepository {

    private val usersRef = firebaseDatabase.getReference(FirebaseConstants.USERS)
    private val onlineStatusRef = firebaseDatabase.getReference(FirebaseConstants.USER_ONLINE_STATUS)
    private val storageRef = firebaseStorage.reference

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                val user = userDao.getUserById(firebaseUser.uid)
                if (user != null) {
                    Result.success(user)
                } else {
                    // Try to get from Firebase
                    val result = getUserById(firebaseUser.uid)
                    result
                }
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User?> {
        return try {
            // Try local first
            val localUser = userDao.getUserById(userId)
            if (localUser != null) {
                return Result.success(localUser)
            }

            // Fetch from Firebase
            val snapshot = usersRef.child(userId).get().await()
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    userDao.insertUser(user)
                    Result.success(user)
                } else {
                    Result.success(null)
                }
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserByIdFlow(userId: String): Flow<User?> {
        return userDao.getUserByIdFlow(userId)
    }

    override suspend fun getUserByUsername(username: String): Result<User?> {
        return try {
            // Try local first
            val localUser = userDao.getUserByUsername(username)
            if (localUser != null) {
                return Result.success(localUser)
            }

            // Query Firebase by username
            val query = usersRef.orderByChild("username").equalTo(username)
            val snapshot = query.get().await()
            
            if (snapshot.exists()) {
                val userSnapshot = snapshot.children.first()
                val user = userSnapshot.getValue(User::class.java)
                if (user != null) {
                    userDao.insertUser(user)
                    Result.success(user)
                } else {
                    Result.success(null)
                }
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Result<User?> {
        return try {
            // Try local first
            val localUser = userDao.getUserByEmail(email)
            if (localUser != null) {
                return Result.success(localUser)
            }

            // Query Firebase by email
            val query = usersRef.orderByChild("email").equalTo(email)
            val snapshot = query.get().await()
            
            if (snapshot.exists()) {
                val userSnapshot = snapshot.children.first()
                val user = userSnapshot.getValue(User::class.java)
                if (user != null) {
                    userDao.insertUser(user)
                    Result.success(user)
                } else {
                    Result.success(null)
                }
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            if (currentUser.uid != user.id) {
                return Result.failure(Exception("Cannot update other user's profile"))
            }

            val updatedUser = user.copy(updatedAt = System.currentTimeMillis())
            
            usersRef.child(user.id).setValue(updatedUser).await()
            userDao.updateUser(updatedUser)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(query: String): Result<List<User>> {
        return try {
            val localUsers = userDao.searchUsers(query)
            
            // Also search Firebase for more comprehensive results
            val firebaseUsers = mutableListOf<User>()
            
            // Search by display name
            val displayNameQuery = usersRef.orderByChild("displayName")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .limitToFirst(20)
            
            val displayNameSnapshot = displayNameQuery.get().await()
            displayNameSnapshot.children.forEach { userSnapshot ->
                val user = userSnapshot.getValue(User::class.java)
                if (user != null && user.isActive) {
                    firebaseUsers.add(user)
                }
            }

            // Search by username
            val usernameQuery = usersRef.orderByChild("username")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .limitToFirst(20)
            
            val usernameSnapshot = usernameQuery.get().await()
            usernameSnapshot.children.forEach { userSnapshot ->
                val user = userSnapshot.getValue(User::class.java)
                if (user != null && user.isActive && !firebaseUsers.any { it.id == user.id }) {
                    firebaseUsers.add(user)
                }
            }

            // Combine and deduplicate results
            val allUsers = (localUsers + firebaseUsers).distinctBy { it.id }
            
            // Cache Firebase results locally
            if (firebaseUsers.isNotEmpty()) {
                userDao.insertUsers(firebaseUsers)
            }
            
            Result.success(allUsers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsersByIds(userIds: List<String>): Result<List<User>> {
        return try {
            val users = mutableListOf<User>()
            val missingUserIds = mutableListOf<String>()

            // Try to get from local database first
            userIds.forEach { userId ->
                val localUser = userDao.getUserById(userId)
                if (localUser != null) {
                    users.add(localUser)
                } else {
                    missingUserIds.add(userId)
                }
            }

            // Fetch missing users from Firebase
            if (missingUserIds.isNotEmpty()) {
                val firebaseUsers = mutableListOf<User>()
                
                missingUserIds.forEach { userId ->
                    try {
                        val snapshot = usersRef.child(userId).get().await()
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                firebaseUsers.add(user)
                                users.add(user)
                            }
                        }
                    } catch (e: Exception) {
                        // Continue with other users if one fails
                    }
                }

                // Cache Firebase results
                if (firebaseUsers.isNotEmpty()) {
                    userDao.insertUsers(firebaseUsers)
                }
            }

            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateOnlineStatus(isOnline: Boolean): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val timestamp = System.currentTimeMillis()
            
            val statusData = mapOf(
                "isOnline" to isOnline,
                "lastSeen" to timestamp
            )

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.USERS}/${currentUser.uid}/isOnline" to isOnline,
                "/${FirebaseConstants.USERS}/${currentUser.uid}/lastSeen" to timestamp,
                "/${FirebaseConstants.USER_ONLINE_STATUS}/${currentUser.uid}" to statusData
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            userDao.updateOnlineStatus(currentUser.uid, isOnline, timestamp)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOnlineUsers(): Result<List<User>> {
        return try {
            val users = userDao.getOnlineUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserOnlineStatusFlow(userId: String): Flow<Boolean> {
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isOnline = snapshot.child("isOnline").getValue(Boolean::class.java) ?: false
                    trySend(isOnline)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            onlineStatusRef.child(userId).addValueEventListener(listener)

            awaitClose {
                onlineStatusRef.child(userId).removeEventListener(listener)
            }
        }
    }

    override suspend fun updateFcmToken(token: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.USERS}/${currentUser.uid}/fcmToken" to token
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            userDao.updateFcmToken(currentUser.uid, token)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFcmToken(userId: String): Result<String?> {
        return try {
            val user = userDao.getUserById(userId)
            if (user != null && user.fcmToken.isNotEmpty()) {
                Result.success(user.fcmToken)
            } else {
                // Get from Firebase
                val snapshot = usersRef.child(userId).child("fcmToken").get().await()
                val token = snapshot.getValue(String::class.java)
                Result.success(token)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfilePhoto(photoFile: File): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val fileName = "${currentUser.uid}_profile.jpg"
            val photoRef = storageRef
                .child(FirebaseConstants.USER_PROFILES)
                .child(fileName)

            val uploadTask = photoRef.putFile(android.net.Uri.fromFile(photoFile)).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.USERS}/${currentUser.uid}/photoUrl" to downloadUrl.toString(),
                "/${FirebaseConstants.USERS}/${currentUser.uid}/updatedAt" to System.currentTimeMillis()
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            userDao.updatePhotoUrl(currentUser.uid, downloadUrl.toString())

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProfilePhoto(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val fileName = "${currentUser.uid}_profile.jpg"
            val photoRef = storageRef
                .child(FirebaseConstants.USER_PROFILES)
                .child(fileName)

            try {
                photoRef.delete().await()
            } catch (e: Exception) {
                // Photo might not exist, continue with update
            }

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.USERS}/${currentUser.uid}/photoUrl" to "",
                "/${FirebaseConstants.USERS}/${currentUser.uid}/updatedAt" to System.currentTimeMillis()
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            userDao.updatePhotoUrl(currentUser.uid, "")

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserPreferences(preferences: Map<String, Any>): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val updates = hashMapOf<String, Any>()
            preferences.forEach { (key, value) ->
                updates["/${FirebaseConstants.USERS}/${currentUser.uid}/preferences/$key"] = value
            }
            updates["/${FirebaseConstants.USERS}/${currentUser.uid}/updatedAt"] = System.currentTimeMillis()

            firebaseDatabase.reference.updateChildren(updates).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserPreferences(): Result<Map<String, Any>> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val snapshot = usersRef.child(currentUser.uid).child("preferences").get().await()
            val preferences = snapshot.value as? Map<String, Any> ?: emptyMap()

            Result.success(preferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncUserData(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val snapshot = usersRef.child(currentUser.uid).get().await()
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    userDao.insertUser(user)
                }
            }

            // Update FCM token
            val token = firebaseMessaging.token.await()
            updateFcmToken(token)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncUserContacts(): Result<List<User>> {
        return try {
            // This would typically involve syncing phone contacts
            // For now, returning empty list
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Initialize user presence tracking
     */
    suspend fun initializePresenceTracking() {
        try {
            val currentUser = firebaseAuth.currentUser ?: return

            // Set up online presence
            val presenceRef = onlineStatusRef.child(currentUser.uid)
            val connectedRef = firebaseDatabase.getReference(".info/connected")

            connectedRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    if (connected) {
                        // Set online status
                        presenceRef.child("isOnline").setValue(true)
                        presenceRef.child("lastSeen").setValue(ServerValue.TIMESTAMP)

                        // Set offline status when disconnected
                        presenceRef.child("isOnline").onDisconnect().setValue(false)
                        presenceRef.child("lastSeen").onDisconnect().setValue(ServerValue.TIMESTAMP)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        } catch (e: Exception) {
            // Handle initialization error
        }
    }
}