package com.synapse.social.studioasinc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<CreateGroupActivity.User> userList;
    private List<CreateGroupActivity.User> userListFiltered;
    private List<CreateGroupActivity.User> selectedUsers;
    private OnUserSelectedListener listener;

    public interface OnUserSelectedListener {
        void onUserSelected(CreateGroupActivity.User user, boolean isSelected);
    }

    public UsersAdapter(List<CreateGroupActivity.User> userList, List<CreateGroupActivity.User> selectedUsers, OnUserSelectedListener listener) {
        this.userList = userList;
        this.userListFiltered = new ArrayList<>(userList);
        this.selectedUsers = selectedUsers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        CreateGroupActivity.User user = userListFiltered.get(position);
        holder.tvUserName.setText(user.getUsername());
        holder.tvUserUid.setText(user.getUid());
        holder.cbUser.setOnCheckedChangeListener(null);
        holder.cbUser.setChecked(selectedUsers.contains(user));
        holder.cbUser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onUserSelected(user, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return userListFiltered.size();
    }

    public void filterList(List<CreateGroupActivity.User> filteredList) {
        userListFiltered = filteredList;
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvUserUid;
        CheckBox cbUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserUid = itemView.findViewById(R.id.tv_user_uid);
            cbUser = itemView.findViewById(R.id.cb_user);
        }
    }
}
