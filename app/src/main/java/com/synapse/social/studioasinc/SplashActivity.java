package com.synapse.social.studioasinc;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        } else {
            // User is not signed in
            startActivity(new Intent(SplashActivity.this, AuthActivity.class));
        }
        finish();
    }
}
