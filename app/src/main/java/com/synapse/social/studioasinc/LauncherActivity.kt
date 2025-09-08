package com.synapse.social.studioasinc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is signed in
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            // No user is signed in
            startActivity(Intent(this, AuthActivity::class.java))
        }
        finish()
    }
}
