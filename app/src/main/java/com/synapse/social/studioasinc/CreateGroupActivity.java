package com.synapse.social.studioasinc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {

    private static final int READ_CONTACTS_PERMISSION_REQUEST = 1;
    private RecyclerView rvContacts;
    private List<Contact> contactList = new ArrayList<>();
    private List<Contact> selectedContacts = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private ChipGroup chipGroupSelectedContacts;
    private EditText etSearch;
    private TextInputEditText etGroupName;
    private FloatingActionButton fabCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        rvContacts = findViewById(R.id.rv_contacts);
        chipGroupSelectedContacts = findViewById(R.id.chip_group_selected_contacts);
        etSearch = findViewById(R.id.et_search);
        etGroupName = findViewById(R.id.et_group_name);
        fabCreateGroup = findViewById(R.id.fab_create_group);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        fabCreateGroup.setEnabled(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST);
        } else {
            loadContacts();
        }

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
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                Toast.makeText(this, "Permission denied. Can't load contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(new Contact(name, phoneNumber));
            }
            cursor.close();
        }
        contactsAdapter = new ContactsAdapter(contactList, this::onContactSelected);
        rvContacts.setAdapter(contactsAdapter);
    }

    private void filterContacts(String query) {
        List<Contact> filteredList = new ArrayList<>();
        for (Contact contact : contactList) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(contact);
            }
        }
        contactsAdapter.filterList(filteredList);
    }

    private void onContactSelected(Contact contact, boolean isSelected) {
        if (isSelected) {
            selectedContacts.add(contact);
            addChip(contact);
        } else {
            selectedContacts.remove(contact);
            removeChip(contact);
        }
        updateCreateButtonState();
    }

    private void addChip(Contact contact) {
        Chip chip = new Chip(this);
        chip.setText(contact.getName());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            selectedContacts.remove(contact);
            chipGroupSelectedContacts.removeView(chip);
            contactsAdapter.notifyDataSetChanged(); // To update the checkbox state
            updateCreateButtonState();
        });
        chipGroupSelectedContacts.addView(chip);
    }

    private void removeChip(Contact contact) {
        for (int i = 0; i < chipGroupSelectedContacts.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupSelectedContacts.getChildAt(i);
            if (chip.getText().toString().equals(contact.getName())) {
                chipGroupSelectedContacts.removeView(chip);
                break;
            }
        }
    }

    private void updateCreateButtonState() {
        boolean isGroupNameValid = etGroupName.getText() != null && !etGroupName.getText().toString().isEmpty();
        boolean areParticipantsSelected = !selectedContacts.isEmpty();
        fabCreateGroup.setEnabled(isGroupNameValid && areParticipantsSelected);
    }

    public static class Contact {
        private String name;
        private String phoneNumber;

        public Contact(String name, String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
}
