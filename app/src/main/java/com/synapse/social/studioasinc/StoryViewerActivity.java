package com.synapse.social.studioasinc;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.carousel.CarouselView;
import com.synapse.social.studioasinc.models.Story;

import java.util.ArrayList;

public class StoryViewerActivity extends AppCompatActivity {

    private CarouselView storyCarouselView;
    private ArrayList<Story> stories;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_viewer);

        storyCarouselView = findViewById(R.id.story_carousel_view);

        stories = (ArrayList<Story>) getIntent().getSerializableExtra("stories");
        currentPosition = getIntent().getIntExtra("position", 0);

        storyCarouselView.setAdapter(new StoryCarouselAdapter(stories));
        storyCarouselView.scrollToPosition(currentPosition);
    }

    private static class StoryCarouselAdapter extends RecyclerView.Adapter<StoryCarouselAdapter.StoryViewHolder> {

        private final ArrayList<Story> stories;

        public StoryCarouselAdapter(ArrayList<Story> stories) {
            this.stories = stories;
        }

        @NonNull
        @Override
        public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);
            return new StoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
            Story story = stories.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(story.getUrl()))
                    .into(holder.storyImageView);
        }

        @Override
        public int getItemCount() {
            return stories.size();
        }

        static class StoryViewHolder extends RecyclerView.ViewHolder {
            ImageView storyImageView;

            public StoryViewHolder(@NonNull View itemView) {
                super(itemView);
                storyImageView = itemView.findViewById(R.id.story_image_view);
            }
        }
    }
}
