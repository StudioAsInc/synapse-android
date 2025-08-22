package com.synapse.social.studioasinc;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private ArrayList<HashMap<String, Object>> attachments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Get the attachments from the intent
        if (getIntent().hasExtra("attachments")) {
            attachments = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("attachments");
        }

        recyclerView = findViewById(R.id.gallery_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Using a vertical list

        adapter = new GalleryAdapter(this, attachments);
        recyclerView.setAdapter(adapter);
    }
}
