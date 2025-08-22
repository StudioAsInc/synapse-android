package com.synapse.social.studioasinc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> attachments;

    public GalleryAdapter(Context context, ArrayList<HashMap<String, Object>> attachments) {
        this.context = context;
        this.attachments = attachments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, Object> attachment = attachments.get(position);
        String url = (String) attachment.get("url");

        if (url != null) {
            Glide.with(context)
                .load(url)
                .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gallery_image_view);
        }
    }
}
