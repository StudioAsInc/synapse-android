package com.synapse.social.studioasinc.groupchat.data.repository

import com.synapse.social.studioasinc.groupchat.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    
    // User operations
    suspend fun getCurrentUser(): Result<User?>
    suspend fun getUserById(userId: String): Result<User?>
    fun getUserByIdFlow(userId: String): Flow<User?>
    suspend fun getUserByUsername(username: String): Result<User?>
    suspend fun getUserByEmail(email: String): Result<User?>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun searchUsers(query: String): Result<List<User>>
    suspend fun getUsersByIds(userIds: List<String>): Result<List<User>>
    
    // Online status operations
    suspend fun updateOnlineStatus(isOnline: Boolean): Result<Unit>
    suspend fun getOnlineUsers(): Result<List<User>>
    fun getUserOnlineStatusFlow(userId: String): Flow<Boolean>
    
    // FCM token operations
    suspend fun updateFcmToken(token: String): Result<Unit>
    suspend fun getFcmToken(userId: String): Result<String?>
    
    // Profile operations
    suspend fun updateProfilePhoto(photoFile: java.io.File): Result<String>
    suspend fun deleteProfilePhoto(): Result<Unit>
    
    // User preferences and settings
    suspend fun updateUserPreferences(preferences: Map<String, Any>): Result<Unit>
    suspend fun getUserPreferences(): Result<Map<String, Any>>
    
    // Sync operations
    suspend fun syncUserData(): Result<Unit>
    suspend fun syncUserContacts(): Result<List<User>>
}