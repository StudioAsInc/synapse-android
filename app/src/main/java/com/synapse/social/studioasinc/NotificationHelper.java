package com.synapse.social.studioasinc;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NotificationHelper {

    public static void sendNotification(final String recipientId, final String senderName, final String message) {
        DatabaseReference recipientRef = FirebaseDatabase.getInstance().getReference("skyline/users").child(recipientId);
        recipientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("oneSignalPlayerId")) {
                    final String playerId = dataSnapshot.child("oneSignalPlayerId").getValue(String.class);
                    if (playerId != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject notification = new JSONObject();
                                    notification.put("app_id", "044e1911-6911-4871-95f9-d60003002fe2");
                                    notification.put("include_player_ids", new JSONArray().put(playerId));
                                    notification.put("headings", new JSONObject().put("en", "New Message from " + senderName));
                                    notification.put("contents", new JSONObject().put("en", message));

                                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), notification.toString());
                                    Request request = new Request.Builder()
                                            .url("https://onesignal.com/api/v1/notifications")
                                            .header("Authorization", "Basic os_v2_app_arhbseljcfehdfpz2yaagabp4kctlrelaefuvevrbp25teeq2mbo3jakcg27enu7kpb6dlbgbl6kc5smr3afktu27pzv6enj7nyksua")
                                            .post(body)
                                            .build();
                                    OkHttpClient client = new OkHttpClient();
                                    client.newCall(request).execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
