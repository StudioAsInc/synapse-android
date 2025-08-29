package com.synapse.social.studioasinc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class NotificationFragment extends Fragment {

    // UI Components
    private LinearLayout body;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView notificationsRecyclerView;
    private TextView noNotificationsText;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        
        initialize(view);
        
        return view;
    }
    
    private void initialize(View view) {
        body = view.findViewById(R.id.body);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        notificationsRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        noNotificationsText = view.findViewById(R.id.noNotificationsText);
        
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Mock refresh for notifications
                swipeLayout.setRefreshing(false);
            }
        });
        
        // Show mock notification UI
        showMockNotifications();
    }
    
    private void showMockNotifications() {
        // Mock notification UI - this is just for demonstration
        noNotificationsText.setText("No notifications yet\n\nYou'll see notifications here when someone:\n• Likes your posts\n• Comments on your posts\n• Follows you\n• Mentions you");
    }
}