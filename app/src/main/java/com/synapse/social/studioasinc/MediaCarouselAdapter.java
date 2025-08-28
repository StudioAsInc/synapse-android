package com.synapse.social.studioasinc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MediaCarouselAdapter extends RecyclerView.Adapter<MediaCarouselAdapter.MediaViewHolder> {

    private final Context context;
    private final ArrayList<HashMap<String, Object>> attachments;
    private static final int MAX_VISIBLE_ITEMS = 4;

    public MediaCarouselAdapter(Context context, ArrayList<HashMap<String, Object>> attachments) {
        this.context = context;
        this.attachments = attachments;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_media_carousel, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        HashMap<String, Object> attachment = attachments.get(position);
        String imageUrl = (String) attachment.get("url");

        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);

        if (position == MAX_VISIBLE_ITEMS - 1 && attachments.size() > MAX_VISIBLE_ITEMS) {
            holder.overlayContainer.setVisibility(View.VISIBLE);
            holder.moreImagesText.setText("+" + (attachments.size() - MAX_VISIBLE_ITEMS));
        } else {
            holder.overlayContainer.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GalleryActivity.class);
            ArrayList<String> imageUrls = attachments.stream()
                                                     .map(p -> (String) p.get("url"))
                                                     .collect(Collectors.toCollection(ArrayList::new));
            intent.putStringArrayListExtra(GalleryActivity.EXTRA_IMAGE_URLS, imageUrls);
            intent.putExtra(GalleryActivity.EXTRA_INITIAL_POSITION, position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(attachments.size(), MAX_VISIBLE_ITEMS);
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout overlayContainer;
        TextView moreImagesText;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carousel_image_view);
            overlayContainer = itemView.findViewById(R.id.overlay_container);
            moreImagesText = itemView.findViewById(R.id.more_images_text);
        }
    }
}
