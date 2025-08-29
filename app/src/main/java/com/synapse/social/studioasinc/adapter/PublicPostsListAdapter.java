package com.synapse.social.studioasinc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.model.Post;
import com.synapse.social.studioasinc.model.Story;

import java.util.List;

public class PublicPostsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_STORIES = 0;
    private static final int TYPE_POST_COMPOSER = 1;
    private static final int TYPE_FILTER = 2;
    private static final int TYPE_POST = 3;

    private final Context context;
    private final List<Object> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onPostLikeClick(Post post);
        void onPostCommentClick(Post post);
        void onPostShareClick(Post post);
        void onUsernameClick(String userId);
        void onFilterSelected(String filter);
        void onPostCreateClick(String postText);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PublicPostsListAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Stories) {
            return TYPE_STORIES;
        } else if (item instanceof PostComposer) {
            return TYPE_POST_COMPOSER;
        } else if (item instanceof Filter) {
            return TYPE_FILTER;
        } else {
            return TYPE_POST;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case TYPE_STORIES:
                view = inflater.inflate(R.layout.synapse_stories_cv, parent, false);
                return new StoriesViewHolder(view);
            case TYPE_POST_COMPOSER:
                view = inflater.inflate(R.layout.layout_post_composer, parent, false);
                return new PostComposerViewHolder(view);
            case TYPE_FILTER:
                view = inflater.inflate(R.layout.layout_filter_bar, parent, false);
                return new FilterViewHolder(view);
            case TYPE_POST:
            default:
                view = inflater.inflate(R.layout.synapse_post_cv, parent, false);
                return new PostViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_STORIES:
                StoriesViewHolder storiesViewHolder = (StoriesViewHolder) holder;
                Stories stories = (Stories) items.get(position);
                storiesViewHolder.storiesView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                storiesViewHolder.storiesView.setAdapter(new StoriesAdapter(context, stories.getStories()));
                break;
            case TYPE_POST_COMPOSER:
                PostComposerViewHolder postComposerViewHolder = (PostComposerViewHolder) holder;
                postComposerViewHolder.postPublishButton.setOnClickListener(v -> {
                    if (listener != null) {
                        String postText = postComposerViewHolder.postInput.getText().toString();
                        listener.onPostCreateClick(postText);
                    }
                });
                break;
            case TYPE_FILTER:
                FilterViewHolder filterViewHolder = (FilterViewHolder) holder;
                filterViewHolder.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (checkedId == R.id.chip_public) {
                        listener.onFilterSelected("PUBLIC");
                    } else if (checkedId == R.id.chip_local) {
                        listener.onFilterSelected("LOCAL");
                    } else if (checkedId == R.id.chip_followed) {
                        listener.onFilterSelected("FOLLOWED");
                    } else if (checkedId == R.id.chip_favorite) {
                        listener.onFilterSelected("FAVORITE");
                    }
                });
                break;
            case TYPE_POST:
                Post post = (Post) items.get(position);
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                postViewHolder.username.setText(post.getUid()); // Assuming uid is username for now
                postViewHolder.postText.setText(post.getPost_text());

                if (post.getPost_image() != null) {
                    postViewHolder.postImage.setVisibility(View.VISIBLE);
                    Glide.with(context).load(post.getPost_image()).into(postViewHolder.postImage);
                } else {
                    postViewHolder.postImage.setVisibility(View.GONE);
                }

                postViewHolder.likeButton.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPostLikeClick(post);
                    }
                });

                postViewHolder.commentButton.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPostCommentClick(post);
                    }
                });

                postViewHolder.username.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onUsernameClick(post.getUid());
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder classes
    public static class StoriesViewHolder extends RecyclerView.ViewHolder {
        public final RecyclerView storiesView;

        public StoriesViewHolder(View itemView) {
            super(itemView);
            storiesView = itemView.findViewById(R.id.storiesView);
        }
    }

    public static class PostComposerViewHolder extends RecyclerView.ViewHolder {
        public final ImageView profileImage;
        public final EditText postInput;
        public final TextView postPublishButton;

        public PostComposerViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.miniPostLayoutProfileImage);
            postInput = itemView.findViewById(R.id.miniPostLayoutTextPostInput);
            postPublishButton = itemView.findViewById(R.id.miniPostLayoutTextPostPublish);
        }
    }

    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        public final ChipGroup chipGroup;

        public FilterViewHolder(View itemView) {
            super(itemView);
            chipGroup = itemView.findViewById(R.id.filter_chip_group);
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public final TextView username;
        public final TextView postText;
        public final ImageView postImage;
        public final TextView likeCount;
        public final TextView commentCount;
        public final View likeButton;
        public final View commentButton;

        public PostViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userInfoUsername);
            postText = itemView.findViewById(R.id.postMessageTextMiddle);
            postImage = itemView.findViewById(R.id.postImage);
            likeCount = itemView.findViewById(R.id.likeButtonCount);
            commentCount = itemView.findViewById(R.id.commentsButtonCount);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentsButton);
        }
    }

    // Dummy classes for view types
    public static class PostComposer {}
    public static class Stories {
        private final List<Story> stories;

        public Stories(List<Story> stories) {
            this.stories = stories;
        }

        public List<Story> getStories() {
            return stories;
        }
    }
    public static class Filter {}

    // Inner adapter for stories
    private static class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {
        private final Context context;
        private final List<Story> stories;

        StoriesAdapter(Context context, List<Story> stories) {
            this.context = context;
            this.stories = stories;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.synapse_story_cv, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Story story = stories.get(position);
            holder.username.setText(story.getUid());
            // TODO: Load user avatar into holder.avatar
        }

        @Override
        public int getItemCount() {
            return stories.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView avatar;
            final TextView username;
            ViewHolder(View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.storiesMyStoryProfileImage);
                username = itemView.findViewById(R.id.storiesMyStoryTitle);
            }
        }
    }
}
