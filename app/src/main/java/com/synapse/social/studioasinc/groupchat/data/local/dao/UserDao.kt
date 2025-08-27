package com.synapse.social.studioasinc.groupchat.data.local.dao

import androidx.room.*
import com.synapse.social.studioasinc.groupchat.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY displayName ASC")
    fun getAllUsersFlow(): Flow<List<User>>
    
    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY displayName ASC")
    suspend fun getAllUsers(): List<User>
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?
    
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: String): Flow<User?>
    
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
    
    @Query("SELECT * FROM users WHERE (displayName LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%') AND isActive = 1")
    suspend fun searchUsers(query: String): List<User>
    
    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    suspend fun getUsersByIds(userIds: List<String>): List<User>
    
    @Query("SELECT * FROM users WHERE isOnline = 1 AND isActive = 1")
    suspend fun getOnlineUsers(): List<User>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Query("UPDATE users SET isOnline = :isOnline, lastSeen = :lastSeen WHERE id = :userId")
    suspend fun updateOnlineStatus(userId: String, isOnline: Boolean, lastSeen: Long)
    
    @Query("UPDATE users SET fcmToken = :token WHERE id = :userId")
    suspend fun updateFcmToken(userId: String, token: String)
    
    @Query("UPDATE users SET photoUrl = :photoUrl WHERE id = :userId")
    suspend fun updatePhotoUrl(userId: String, photoUrl: String)
    
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
    
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}