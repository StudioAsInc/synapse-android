package com.synapse.social.studioasinc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> searchedUsersList = new ArrayList<>();
    private MaterialToolbar toolbar;
    private SearchView searchView;
    private RecyclerView searchUserLayoutRecyclerView;
    private TextView searchUserLayoutNoUserFound;
    private SearchUserLayoutRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        initialize(savedInstanceState);
        FirebaseApp.initializeApp(this);
        initializeLogic();
    }

    private void initialize(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_view);
        searchUserLayoutRecyclerView = findViewById(R.id.SearchUserLayoutRecyclerView);
        searchUserLayoutNoUserFound = findViewById(R.id.SearchUserLayoutNoUserFound);
    }

    private void initializeLogic() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        searchUserLayoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchUserLayoutRecyclerViewAdapter(searchedUsersList);
        searchUserLayoutRecyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showAllUsers();
                } else {
                    searchUsers(newText);
                }
                return true;
            }
        });

        showAllUsers();
    }

    private void searchUsers(String searchText) {
        DatabaseReference searchRef = FirebaseDatabase.getInstance().getReference("skyline/users");
        Query searchQuery = searchRef.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff").limitToLast(50);
        performSearch(searchQuery);
    }

    private void showAllUsers() {
        DatabaseReference searchRef = FirebaseDatabase.getInstance().getReference("skyline/users");
        Query searchQuery = searchRef.limitToLast(50);
        performSearch(searchQuery);
    }

    private void performSearch(Query searchQuery) {
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchedUsersList.clear();
                if (snapshot.exists()) {
                    searchUserLayoutRecyclerView.setVisibility(View.VISIBLE);
                    searchUserLayoutNoUserFound.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HashMap<String, Object> searchMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (searchMap != null) {
                            searchedUsersList.add(searchMap);
                        }
                    }
                } else {
                    searchUserLayoutRecyclerView.setVisibility(View.GONE);
                    searchUserLayoutNoUserFound.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public class SearchUserLayoutRecyclerViewAdapter extends RecyclerView.Adapter<SearchUserLayoutRecyclerViewAdapter.ViewHolder> {

        private ArrayList<HashMap<String, Object>> data;

        public SearchUserLayoutRecyclerViewAdapter(ArrayList<HashMap<String, Object>> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.synapse_users_list_cv, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HashMap<String, Object> currentItem = data.get(position);
            holder.bind(currentItem);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private com.google.android.material.imageview.ShapeableImageView profileAvatar;
            private TextView username, name;
            private ImageView genderBadge, badge, userStatusCircleIN;

            ViewHolder(View view) {
                super(view);
                profileAvatar = view.findViewById(R.id.profileAvatar);
                username = view.findViewById(R.id.username);
                name = view.findViewById(R.id.name);
                genderBadge = view.findViewById(R.id.genderBadge);
                badge = view.findViewById(R.id.badge);
                userStatusCircleIN = view.findViewById(R.id.userStatusCircleIN);
            }

            void bind(final HashMap<String, Object> item) {
                name.setText(getStringValue(item, "username", ""));

                if (Boolean.parseBoolean(getStringValue(item, "banned", "false"))) {
                    profileAvatar.setImageResource(R.drawable.banned_avatar);
                } else {
                    String avatarUrl = getStringValue(item, "avatar", "null");
                    if ("null".equals(avatarUrl)) {
                        profileAvatar.setImageResource(R.drawable.avatar);
                    } else {
                        Glide.with(itemView.getContext()).load(avatarUrl).into(profileAvatar);
                    }
                }

                String nickname = getStringValue(item, "nickname", "null");
                username.setText("null".equals(nickname) ? "@" + getStringValue(item, "username", "") : nickname);

                String gender = getStringValue(item, "gender", "hidden");
                genderBadge.setVisibility("hidden".equals(gender) ? View.GONE : View.VISIBLE);
                if ("male".equals(gender)) {
                    genderBadge.setImageResource(R.drawable.male_badge);
                } else if ("female".equals(gender)) {
                    genderBadge.setImageResource(R.drawable.female_badge);
                }

                badge.setVisibility(View.GONE);
                String accountType = getStringValue(item, "account_type", "user");
                switch (accountType) {
                    case "admin":
                        badge.setImageResource(R.drawable.admin_badge);
                        badge.setVisibility(View.VISIBLE);
                        break;
                    case "moderator":
                        badge.setImageResource(R.drawable.moderator_badge);
                        badge.setVisibility(View.VISIBLE);
                        break;
                    case "support":
                        badge.setImageResource(R.drawable.support_badge);
                        badge.setVisibility(View.VISIBLE);
                        break;
                    case "user":
                        if (Boolean.parseBoolean(getStringValue(item, "account_premium", "false"))) {
                            badge.setImageResource(R.drawable.premium_badge);
                            badge.setVisibility(View.VISIBLE);
                        } else if (Boolean.parseBoolean(getStringValue(item, "verify", "false"))) {
                            badge.setImageResource(R.drawable.verified_badge);
                            badge.setVisibility(View.VISIBLE);
                        }
                        break;
                }

                userStatusCircleIN.setVisibility("online".equals(getStringValue(item, "status", "offline")) ? View.VISIBLE : View.GONE);

                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                    intent.putExtra("uid", getStringValue(item, "uid", ""));
                    startActivity(intent);
                });
            }

            private String getStringValue(HashMap<String, Object> map, String key, String defaultValue) {
                if (map.containsKey(key) && map.get(key) != null) {
                    return map.get(key).toString();
                }
                return defaultValue;
            }
        }
    }
}
