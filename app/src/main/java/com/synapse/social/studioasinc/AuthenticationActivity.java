package com.synapse.social.studioasinc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.ActionCodeSettings;
import android.content.Intent;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.credentials.CredentialManager;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.exceptions.ClearCredentialException;
import android.os.CancellationSignal;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;
    private com.google.android.gms.common.SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        mAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(this);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        com.google.android.material.textfield.TextInputEditText emailEditText = findViewById(R.id.email_edit_text);
        com.google.android.material.button.MaterialButton sendLinkButton = findViewById(R.id.send_link_button);

        sendLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(AuthenticationActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendSignInLink(email);
            }
        });

        handleSignInWithEmailLink();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        Executor executor = Executors.newSingleThreadExecutor();

        credentialManager.getCredentialAsync(
                request,
                this,
                new CancellationSignal(),
                executor,
                new CredentialManager.Callback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignIn(result.getCredential());
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        Log.w(TAG, "getCredential failed", e);
                        Toast.makeText(AuthenticationActivity.this, "Sign-in failed.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void handleSignIn(androidx.credentials.Credential credential) {
        if (credential instanceof CustomCredential && credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            try {
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());
                firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
            } catch (Exception e) {
                Log.e(TAG, "handleSignIn: ", e);
            }
        } else {
            Log.w(TAG, "Credential is not of type Google ID!");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in
            // You can navigate to the main activity or update the UI.
            Toast.makeText(this, "Signed in as " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            // User is signed out
        }
    }

    private void signOut() {
        mAuth.signOut();
        ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
        credentialManager.clearCredentialStateAsync(
            clearRequest,
            null,
            Executors.newSingleThreadExecutor(),
            new CredentialManager.Callback<Void, ClearCredentialException>() {
                @Override
                public void onResult(Void result) {
                    updateUI(null);
                }

                @Override
                public void onError(ClearCredentialException e) {
                    Log.e(TAG, "Couldn't clear user credentials: " + e.getLocalizedMessage());
                }
            });
    }

    private void sendSignInLink(String email) {
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        .setUrl("https://synapsesocial.page.link/finishSignUp")
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                getPackageName(),
                                true, /* installIfNotAvailable */
                                "1"    /* minimumVersion */)
                        .build();

        mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(AuthenticationActivity.this, "Sign-in link sent to your email.", Toast.LENGTH_SHORT).show();
                            // Save email to SharedPreferences
                            getSharedPreferences("AuthPrefs", MODE_PRIVATE).edit().putString("email", email).apply();
                        } else {
                            Log.w(TAG, "sendSignInLinkToEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Failed to send sign-in link.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleSignInWithEmailLink() {
        final Intent intent = getIntent();
        final String emailLink = intent.getDataString();

        if (emailLink != null && mAuth.isSignInWithEmailLink(emailLink)) {
            String email = getSharedPreferences("AuthPrefs", MODE_PRIVATE).getString("email", null);
            if (email == null) {
                Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully signed in with email link!");
                                AuthResult result = task.getResult();
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Log.e(TAG, "Error signing in with email link", task.getException());
                                Toast.makeText(AuthenticationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }
}
