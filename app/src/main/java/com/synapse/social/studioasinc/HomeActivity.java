package com.synapse.social.studioasinc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.adapter.ViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase _firebase;
    private DatabaseReference udb;
    private ImageView settings_button;
    private ImageView nav_search_ic;
    private ImageView nav_inbox_ic;
    private ImageView nav_profile_ic;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.home);
        FirebaseApp.initializeApp(this);
        initialize();
        initializeLogic();
    }

    private void initialize() {
        auth = FirebaseAuth.getInstance();
        _firebase = FirebaseDatabase.getInstance();
        udb = _firebase.getReference("skyline/users");

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        settings_button = findViewById(R.id.settings_button);
        nav_search_ic = findViewById(R.id.nav_search_ic);
        nav_inbox_ic = findViewById(R.id.nav_inbox_ic);
        nav_profile_ic = findViewById(R.id.nav_profile_ic);
    }

    private void initializeLogic() {
        viewPager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.home_24px);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_video_library_48px);
                    break;
                case 2:
                    tab.setIcon(R.drawable.icon_notifications_round);
                    break;
            }
        }).attach();

        nav_search_ic.setOnClickListener(_view -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        });

        nav_inbox_ic.setOnClickListener(_view -> {
            Intent intent = new Intent(getApplicationContext(), InboxActivity.class);
            startActivity(intent);
        });

        nav_profile_ic.setOnClickListener(_view -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(intent);
        });

        settings_button.setOnClickListener(_view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        DatabaseReference getReference = udb.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.child("avatar").getValue(String.class) != null && !dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(nav_profile_ic);
                    } else {
                        nav_profile_ic.setImageResource(R.drawable.ic_account_circle_48px);
                    }
                } else {
                    nav_profile_ic.setImageResource(R.drawable.ic_account_circle_48px);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                nav_profile_ic.setImageResource(R.drawable.ic_account_circle_48px);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(HomeActivity.this)
                .setTitle("Exit Synapse")
                .setMessage("Are you certain you wish to terminate the Synapse session? Please confirm your decision.")
                .setIcon(R.drawable.baseline_logout_black_48dp)
                .setPositiveButton("Exit", (_dialog, _which) -> finishAffinity())
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}