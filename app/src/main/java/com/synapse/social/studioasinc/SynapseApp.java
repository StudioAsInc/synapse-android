package com.synapse.social.studioasinc;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.onesignal.user.subscriptions.IPushSubscriptionObserver;
import com.onesignal.user.subscriptions.PushSubscriptionChangedState;
import com.synapse.social.studioasinc.AI.SyraAIBotManager;

import java.util.Calendar;

public class SynapseApp extends Application {
    
    private static Context mContext;
    private Thread.UncaughtExceptionHandler mExceptionHandler;
    
    public static FirebaseAuth mAuth;
    
    public static DatabaseReference getCheckUserReference;
    public static DatabaseReference setUserStatusRef;
    public static DatabaseReference setUserStatusReference;
    
    public static Calendar mCalendar;
    
    public static Context getContext() {
        return mContext;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        this.mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.mCalendar = Calendar.getInstance();
        
        // Initialize Firebase with disk persistence
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        
        // Create notification channels
        createNotificationChannels();
        
        this.mAuth = FirebaseAuth.getInstance();
        this.getCheckUserReference = FirebaseDatabase.getInstance().getReference("skyline/users");
        this.setUserStatusRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        
        // Keep users data synced for offline use
        getCheckUserReference.keepSynced(true);
        
        // Set up global exception handler
        Thread.setDefaultUncaughtExceptionHandler(
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread mThread, Throwable mThrowable) {
                    Intent mIntent = new Intent(mContext, DebugActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtra("error", Log.getStackTraceString(mThrowable));
                    mContext.startActivity(mIntent);
                    mExceptionHandler.uncaughtException(mThread, mThrowable);
                }
            });
        
        setUserStatus();

        // Initialize OneSignal
        final String ONESIGNAL_APP_ID = "044e1911-6911-4871-95f9-d60003002fe2";
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        // Add a subscription observer to get the Player ID and save it to Firestore
        OneSignal.getUser().getPushSubscription().addObserver(new IPushSubscriptionObserver() {
            @Override
            public void onPushSubscriptionChange(@NonNull PushSubscriptionChangedState state) {
                if (state.getCurrent().getOptedIn()) {
                    String playerId = state.getCurrent().getId();
                    if (mAuth.getCurrentUser() != null && playerId != null) {
                        String userUid = mAuth.getCurrentUser().getUid();
                        OneSignalManager.savePlayerIdToRealtimeDatabase(userUid, playerId);
                    }
                }
            }
        });
    }
    
    private void createNotificationChannels() {
        // Create notification channels for Android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationManager notificationManager = 
                (android.app.NotificationManager) getSystemService(android.app.NotificationManager.class);
            
            // Messages channel
            android.app.NotificationChannel messagesChannel = new android.app.NotificationChannel(
                "messages",
                "Messages",
                android.app.NotificationManager.IMPORTANCE_HIGH
            );
            messagesChannel.setDescription("Chat message notifications");
            messagesChannel.enableLights(true);
            messagesChannel.setLightColor(android.graphics.Color.RED);
            messagesChannel.enableVibration(true);
            messagesChannel.setShowBadge(true);
            messagesChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);
            
            // General notifications channel
            android.app.NotificationChannel generalChannel = new android.app.NotificationChannel(
                "general",
                "General",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            generalChannel.setDescription("General app notifications");
            generalChannel.enableLights(false);
            generalChannel.enableVibration(false);
            
            // Create the channels
            notificationManager.createNotificationChannel(messagesChannel);
            notificationManager.createNotificationChannel(generalChannel);
        }
        
        // Initialize Syra AI Bot
        initializeSyraBot();
    }
    
    public static void setUserStatus() {
        if (mAuth.getCurrentUser() != null) {
            setUserStatusReference = FirebaseDatabase.getInstance().getReference("skyline/users")
                .child(mAuth.getCurrentUser().getUid())
                .child("status");
            
            // Enable offline sync for status reference
            setUserStatusReference.keepSynced(true);
            
            getCheckUserReference.child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            setUserStatusRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Boolean connected = dataSnapshot.getValue(Boolean.class);
                                    if (connected != null && connected) {
                                        // Set online status and prepare offline handler
                                        setUserStatusReference.setValue("online");
                                        setUserStatusReference.onDisconnect()
                                            .setValue(String.valueOf(mCalendar.getTimeInMillis()))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("SynapseApp", "Offline status handler set");
                                                    }
                                                }
                                            });
                                    }
                                }
                                
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("SynapseApp", "Connection listener cancelled", databaseError.toException());
                                }
                            });
                        }
                    }
                    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("SynapseApp", "User check cancelled", databaseError.toException());
                    }
                });
        }
    }
    
    public static void setUserStatusOnline() {
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference statusRef = FirebaseDatabase.getInstance()
                .getReference("skyline/users")
                .child(mAuth.getCurrentUser().getUid())
                .child("status");
            
            // Cancel any pending offline operations
            statusRef.onDisconnect().cancel();
            
            // Set online status
            statusRef.setValue("online")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("SynapseApp", "Failed to set online status");
                            // Retry setting online status
                            statusRef.setValue("online");
                        }
                    }
                });
        }
    }
    
    public static void setUserStatusOffline() {
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference statusRef = FirebaseDatabase.getInstance()
                .getReference("skyline/users")
                .child(mAuth.getCurrentUser().getUid())
                .child("status");
            
            statusRef.setValue(String.valueOf(mCalendar.getTimeInMillis()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("SynapseApp", "Failed to set offline status");
                            // Retry setting offline status
                            statusRef.setValue(String.valueOf(mCalendar.getTimeInMillis()));
                        }
                    }
                });
        }
    }
    
    public static void setUserStatusOfflineListenerCancel() {
        if (setUserStatusReference != null) {
            setUserStatusReference.onDisconnect().cancel()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("SynapseApp", "Offline status handler cancelled");
                        }
                    }
                });
        }
    }
    
    /**
     * Initialize Syra AI Bot service
     */
    private void initializeSyraBot() {
        try {
            // Start Syra AI Bot service
            SyraAIBotManager botManager = SyraAIBotManager.getInstance(this);
            botManager.startBot();
            Log.d("SynapseApp", "Syra AI Bot initialized successfully");
        } catch (Exception e) {
            Log.e("SynapseApp", "Failed to initialize Syra AI Bot: " + e.getMessage());
        }
    }
    
    /**
     * Enable offline persistence for any database reference
     * @param ref DatabaseReference to enable offline sync for
     */
    public static void enableOfflineSync(DatabaseReference ref) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        ref.keepSynced(true);
    }
}