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

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<CreateGroupActivity.Contact> contactList;
    private List<CreateGroupActivity.Contact> contactListFiltered;
    private OnContactSelectedListener listener;

    public interface OnContactSelectedListener {
        void onContactSelected(CreateGroupActivity.Contact contact, boolean isSelected);
    }

    public ContactsAdapter(List<CreateGroupActivity.Contact> contactList, OnContactSelectedListener listener) {
        this.contactList = contactList;
        this.contactListFiltered = new ArrayList<>(contactList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        CreateGroupActivity.Contact contact = contactListFiltered.get(position);
        holder.tvContactName.setText(contact.getName());
        holder.tvContactPhoneNumber.setText(contact.getPhoneNumber());
        holder.cbContact.setOnCheckedChangeListener(null);
        holder.cbContact.setChecked(false); // Reset checked state
        holder.cbContact.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onContactSelected(contact, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public void filterList(List<CreateGroupActivity.Contact> filteredList) {
        contactListFiltered = filteredList;
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvContactName;
        TextView tvContactPhoneNumber;
        CheckBox cbContact;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.tv_contact_name);
            tvContactPhoneNumber = itemView.findViewById(R.id.tv_contact_phone_number);
            cbContact = itemView.findViewById(R.id.cb_contact);
        }
    }
}
