package com.synapse.social.studioasinc;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.synapse.social.studioasinc.adapter.MessagesPagerAdapter;

public class MessagesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.messages);
        initialize(_savedInstanceState);
        FirebaseApp.initializeApp(this);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        tabLayout = findViewById(R.id.MessagesPageTabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    private void initializeLogic() {
        viewPager.setAdapter(new MessagesPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("CHATS");
                    break;
                case 1:
                    tab.setText("CHANNELS");
                    break;
                case 2:
                    tab.setText("GROUPS");
                    break;
            }
        }).attach();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}
