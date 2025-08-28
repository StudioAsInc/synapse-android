package com.synapse.social.studioasinc;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_INITIAL_POSITION = "initial_position";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        int initialPosition = getIntent().getIntExtra(EXTRA_INITIAL_POSITION, 0);

        ViewPager2 viewPager = findViewById(R.id.view_pager_gallery);
        ImageView backButton = findViewById(R.id.button_back);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            GalleryPagerAdapter adapter = new GalleryPagerAdapter(this, imageUrls);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(initialPosition, false);
        }

        backButton.setOnClickListener(v -> onBackPressed());
    }
}
