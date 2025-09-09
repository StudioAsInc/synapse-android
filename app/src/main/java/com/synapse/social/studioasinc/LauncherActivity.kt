package com.synapse.social.studioasinc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LauncherActivity : AppCompatActivity() {

    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private var hasNavigated = false
    private val TAG = "LauncherActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase first
        FirebaseApp.initializeApp(this)
        
        // Add a small delay to ensure Firebase is fully initialized
        // This is crucial for proper authentication state restoration
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthenticationState()
        }, 200) // 200ms delay to ensure Firebase Auth state is fully restored
    }
    
    private fun checkAuthenticationState() {
        if (hasNavigated) return // Prevent multiple navigations
        
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        
        Log.d(TAG, "Checking authentication state. Current user: ${currentUser?.uid}")
        
        if (currentUser != null) {
            // User is signed in, navigate to HomeActivity
            Log.d(TAG, "User is authenticated, navigating to HomeActivity")
            hasNavigated = true
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            // No user is signed in, navigate to AuthActivity
            Log.d(TAG, "No user authenticated, navigating to AuthActivity")
            hasNavigated = true
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up auth state listener if it was added
        authStateListener?.let { listener ->
            FirebaseAuth.getInstance().removeAuthStateListener(listener)
        }
    }
}
