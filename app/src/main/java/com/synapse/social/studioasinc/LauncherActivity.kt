package com.synapse.social.studioasinc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.synapse.social.studioasinc.permissionreq.AskPermission

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val askPermission = AskPermission(this)
        val user = FirebaseAuth.getInstance().currentUser

        if (askPermission.areAllPermissionsGranted()) {
            if (user != null) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
            }
        } else {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
        finish()
    }
}
