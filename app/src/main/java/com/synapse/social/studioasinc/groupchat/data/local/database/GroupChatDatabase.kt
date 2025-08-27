package com.synapse.social.studioasinc.groupchat.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.synapse.social.studioasinc.groupchat.data.local.dao.*
import com.synapse.social.studioasinc.groupchat.data.model.*

@Database(
    entities = [
        Group::class,
        GroupMember::class,
        GroupMessage::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GroupChatDatabase : RoomDatabase() {
    
    abstract fun groupDao(): GroupDao
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun groupMessageDao(): GroupMessageDao
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: GroupChatDatabase? = null
        
        fun getDatabase(context: Context): GroupChatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroupChatDatabase::class.java,
                    "group_chat_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}