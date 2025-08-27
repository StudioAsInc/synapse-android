package com.synapse.social.studioasinc.groupchat

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class GroupChatApplication : Application() {

    @Inject
    lateinit var userRepository: UserRepositoryImpl

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Enable Firebase Database offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        
        // Initialize user presence tracking when user is authenticated
        applicationScope.launch {
            FirebaseAuth.getInstance().currentUser?.let {
                try {
                    userRepository.initializePresenceTracking()
                    userRepository.syncUserData()
                } catch (e: Exception) {
                    // Handle initialization errors
                }
            }
        }
    }
}