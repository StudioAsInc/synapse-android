package com.synapse.social.studioasinc;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.synapse.social.studioasinc.DatabaseMigrationHelper;
import com.synapse.social.studioasinc.FirebaseConfig;

/**
 * Activity to handle database migration from Realtime Database to Firestore
 * This activity provides a user-friendly interface for the migration process
 */
public class DatabaseMigrationActivity extends AppCompatActivity {
    private static final String TAG = "DatabaseMigrationActivity";
    
    private LinearLayout migrationBody;
    private LinearLayout migrationLoadingBody;
    private LinearLayout migrationSuccessBody;
    private LinearLayout migrationErrorBody;
    private TextView migrationTitle;
    private TextView migrationDescription;
    private TextView migrationProgressText;
    private ProgressBar migrationProgressBar;
    private TextView migrationStatusText;
    private Button startMigrationButton;
    private Button retryButton;
    private Button continueButton;
    private boolean migrationInProgress = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_migration);
        
        initializeViews();
        setupClickListeners();
        
        // Initialize Firebase with Firestore
        FirebaseConfig.initializeFirebase(this);
        
        // Check if migration is needed
        checkMigrationStatus();
    }
    
    private void initializeViews() {
        migrationBody = findViewById(R.id.migrationBody);
        migrationLoadingBody = findViewById(R.id.migrationLoadingBody);
        migrationSuccessBody = findViewById(R.id.migrationSuccessBody);
        migrationErrorBody = findViewById(R.id.migrationErrorBody);
        migrationTitle = findViewById(R.id.migrationTitle);
        migrationDescription = findViewById(R.id.migrationDescription);
        migrationProgressText = findViewById(R.id.migrationProgressText);
        migrationProgressBar = findViewById(R.id.migrationProgressBar);
        startMigrationButton = findViewById(R.id.startMigrationButton);
        retryButton = findViewById(R.id.retryButton);
        continueButton = findViewById(R.id.continueButton);
        
        // Initially show the main migration body
        showMigrationBody();
    }
    
    private void setupClickListeners() {
        startMigrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMigration();
            }
        });
        
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMigration();
            }
        });
        
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mark migration as completed and proceed to main app
                markMigrationCompleted();
                proceedToMainApp();
            }
        });
    }
    
    private void checkMigrationStatus() {
        // Check if migration has already been completed
        boolean migrationCompleted = getSharedPreferences("migration", Context.MODE_PRIVATE)
                .getBoolean("migration_completed", false);
        
        if (migrationCompleted) {
            // Migration already completed, proceed to main app
            proceedToMainApp();
        } else {
            // Show migration interface
            showMigrationBody();
        }
    }
    
    private void showMigrationBody() {
        migrationBody.setVisibility(View.VISIBLE);
        migrationLoadingBody.setVisibility(View.GONE);
        migrationSuccessBody.setVisibility(View.GONE);
        migrationErrorBody.setVisibility(View.GONE);
        
        migrationTitle.setText("Database Migration Required");
        migrationDescription.setText("We're upgrading our database to provide better performance and scalability. " +
                "This migration will move your data to our new Firestore database. " +
                "The process is safe and your data will be preserved.");
    }
    
    private void showLoadingBody() {
        migrationBody.setVisibility(View.GONE);
        migrationLoadingBody.setVisibility(View.VISIBLE);
        migrationSuccessBody.setVisibility(View.GONE);
        migrationErrorBody.setVisibility(View.GONE);
        
        migrationProgressText.setText("Starting migration...");
        migrationProgressBar.setProgress(0);
    }
    
    private void showSuccessBody() {
        migrationBody.setVisibility(View.GONE);
        migrationLoadingBody.setVisibility(View.GONE);
        migrationSuccessBody.setVisibility(View.VISIBLE);
        migrationErrorBody.setVisibility(View.GONE);
    }
    
    private void showErrorBody(String errorMessage) {
        migrationBody.setVisibility(View.GONE);
        migrationLoadingBody.setVisibility(View.GONE);
        migrationSuccessBody.setVisibility(View.GONE);
        migrationErrorBody.setVisibility(View.VISIBLE);
        
        TextView errorText = findViewById(R.id.errorText);
        if (errorText != null) {
            errorText.setText("Migration failed: " + errorMessage);
        }
    }
    
    private void startMigration() {
        if (migrationInProgress) {
            return;
        }
        
        migrationInProgress = true;
        showLoadingBody();
        
        // Show progress bar and update status
        migrationProgressBar.setVisibility(View.VISIBLE);
        migrationStatusText.setText("Starting migration...");
        
        // Start the migration process
        DatabaseMigrationHelper.runCompleteMigration(new DatabaseMigrationHelper.MigrationCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        migrationInProgress = false;
                        migrationProgressBar.setVisibility(View.GONE);
                        migrationStatusText.setText("Migration completed successfully!");
                        showSuccessBody();
                        Toast.makeText(DatabaseMigrationActivity.this, "Migration completed successfully!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            
            @Override
            public void onFailure(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        migrationInProgress = false;
                        migrationProgressBar.setVisibility(View.GONE);
                        migrationStatusText.setText("Migration failed: " + e.getMessage());
                        showErrorBody(e.getMessage());
                        Log.e(TAG, "Migration failed", e);
                    }
                });
            }
        });
    }
    
    private void markMigrationCompleted() {
        getSharedPreferences("migration", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("migration_completed", true)
                .putLong("migration_timestamp", System.currentTimeMillis())
                .apply();
    }
    
    private void proceedToMainApp() {
        // Start the main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        // Prevent going back during migration
        if (migrationInProgress) {
            return;
        }
        super.onBackPressed();
    }
}