package com.synapse.social.studioasinc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.adapter.NotificationAdapter;
import com.synapse.social.studioasinc.model.Notification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ViewStub emptyViewStub;
    private View emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        recyclerView = view.findViewById(R.id.notifications_list);
        emptyViewStub = view.findViewById(R.id.view_stub_empty);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(), notificationList);
        recyclerView.setAdapter(notificationAdapter);

        setupSwipeActions();
        fetchNotifications();

        return view;
    }

    private void fetchNotifications() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            showEmptyView();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("skyline/notifications").child(firebaseUser.getUid());
        reference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    if (notification != null) {
                        notificationList.add(notification);
                    }
                }

                Collections.reverse(notificationList);
                notificationAdapter.notifyDataSetChanged();

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                if (notificationList.isEmpty()) {
                    showEmptyView();
                    recyclerView.setVisibility(View.GONE);
                } else {
                    hideEmptyView();
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }

    private void setupSwipeActions() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                markNotificationAsRead(notificationList.get(position));
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void markNotificationAsRead(Notification notification) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null || notification.isRead()) {
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("skyline/notifications")
                .child(firebaseUser.getUid())
                .child(String.valueOf(notification.getTimestamp())); // Assuming timestamp is unique key
        ref.child("read").setValue(true);
    }

    private void markAllNotificationsAsRead() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("skyline/notifications").child(firebaseUser.getUid());
        for (Notification notification : notificationList) {
            if (!notification.isRead()) {
                ref.child(String.valueOf(notification.getTimestamp())).child("read").setValue(true);
            }
        }
    }

    private void showEmptyView() {
        if (emptyView == null) {
            emptyView = emptyViewStub.inflate();
        }
        emptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.notifications_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_mark_all_read) {
            markAllNotificationsAsRead();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
