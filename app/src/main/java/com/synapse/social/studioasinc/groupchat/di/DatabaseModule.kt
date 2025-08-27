package com.synapse.social.studioasinc.groupchat.di

import android.content.Context
import androidx.room.Room
import com.synapse.social.studioasinc.groupchat.data.local.dao.*
import com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideGroupChatDatabase(@ApplicationContext context: Context): GroupChatDatabase {
        return Room.databaseBuilder(
            context,
            GroupChatDatabase::class.java,
            "group_chat_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideGroupDao(database: GroupChatDatabase): GroupDao {
        return database.groupDao()
    }
    
    @Provides
    fun provideGroupMemberDao(database: GroupChatDatabase): GroupMemberDao {
        return database.groupMemberDao()
    }
    
    @Provides
    fun provideGroupMessageDao(database: GroupChatDatabase): GroupMessageDao {
        return database.groupMessageDao()
    }
    
    @Provides
    fun provideUserDao(database: GroupChatDatabase): UserDao {
        return database.userDao()
    }
}