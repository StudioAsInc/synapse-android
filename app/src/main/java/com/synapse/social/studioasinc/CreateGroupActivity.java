package com.synapse.social.studioasinc;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateGroupActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private List<User> userList = new ArrayList<>();
    private List<User> selectedUsers = new ArrayList<>();
    private UsersAdapter usersAdapter;
    private ChipGroup chipGroupSelectedUsers;
    private EditText etSearch;
    private TextInputEditText etGroupName;
    private FloatingActionButton fabCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        rvUsers = findViewById(R.id.rv_contacts); // Keep the same id for now
        chipGroupSelectedUsers = findViewById(R.id.chip_group_selected_contacts); // Keep the same id for now
        etSearch = findViewById(R.id.et_search);
        etGroupName = findViewById(R.id.et_group_name);
        fabCreateGroup = findViewById(R.id.fab_create_group);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        fabCreateGroup.setEnabled(false);

        usersAdapter = new UsersAdapter(userList, selectedUsers, this::onUserSelected);
        rvUsers.setAdapter(usersAdapter);

        etGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCreateButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabCreateGroup.setOnClickListener(v -> {
            Toast.makeText(this, "Create Group clicked", Toast.LENGTH_SHORT).show();
        });

        searchUsers(""); // Load initial users
    }

    private void searchUsers(String query) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("skyline/users");
        Query searchQuery;
        if (query.isEmpty()) {
            searchQuery = usersRef;
        } else {
            searchQuery = usersRef.orderByChild("username").startAt(query).endAt(query + "\uf8ff");
        }

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HashMap<String, Object> userMap = new HashMap<>((Map<String, Object>) dataSnapshot.getValue());
                        if (userMap != null) {
                            userList.add(new User(userMap.get("username").toString(), userMap.get("uid").toString()));
                        }
                    }
                }
                usersAdapter.filterList(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateGroupActivity.this, "Failed to search users.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onUserSelected(User user, boolean isSelected) {
        if (isSelected) {
            selectedUsers.add(user);
            addChip(user);
        } else {
            selectedUsers.remove(user);
            removeChip(user);
        }
        updateCreateButtonState();
    }

    private void addChip(User user) {
        Chip chip = new Chip(this);
        chip.setText(user.getUsername());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            removeChip(user);
            selectedUsers.remove(user);
            int index = userList.indexOf(user);
            if (index != -1) {
                usersAdapter.notifyItemChanged(index);
            }
            updateCreateButtonState();
        });
        chipGroupSelectedUsers.addView(chip);
    }

    private void removeChip(User user) {
        for (int i = 0; i < chipGroupSelectedUsers.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupSelectedUsers.getChildAt(i);
            if (chip.getText().toString().equals(user.getUsername())) {
                chipGroupSelectedUsers.removeView(chip);
                break;
            }
        }
    }

    private void updateCreateButtonState() {
        boolean isGroupNameValid = etGroupName.getText() != null && !etGroupName.getText().toString().isEmpty();
        boolean areParticipantsSelected = !selectedUsers.isEmpty();
        fabCreateGroup.setEnabled(isGroupNameValid && areParticipantsSelected);
    }

    public static class User {
        private String username;
        private String uid;

        public User(String username, String uid) {
            this.username = username;
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public String getUid() {
            return uid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(uid, user.uid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uid);
        }
    }
}
