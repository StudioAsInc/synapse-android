package com.synapse.social.studioasinc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.adapter.PublicPostsListAdapter;
import com.synapse.social.studioasinc.model.Post;
import com.synapse.social.studioasinc.repository.HomeFeedRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeFeedRepository.OnHomeFeedLoadedListener, PublicPostsListAdapter.OnItemClickListener, HomeFeedRepository.OnPostCreatedListener, HomeFeedRepository.OnPostActionListener {

    private RecyclerView recyclerView;
    private PublicPostsListAdapter adapter;
    private List<Object> items;
    private HomeFeedRepository repository;
    private String currentFilter = "PUBLIC";
    private SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeLayout = view.findViewById(R.id.swipeLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        items = new ArrayList<>();
        adapter = new PublicPostsListAdapter(getContext(), items);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        repository = new HomeFeedRepository();
        loadHomeFeed();

        swipeLayout.setOnRefreshListener(() -> loadHomeFeed());
    }

    private void loadHomeFeed() {
        swipeLayout.setRefreshing(true);
        repository.getHomeFeed(currentFilter, this);
    }

    @Override
    public void onHomeFeedLoaded(List<Object> newItems) {
        items.clear();
        items.addAll(newItems);
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onPostLikeClick(Post post) {
        repository.likePost(post, this);
    }

    @Override
    public void onPostCommentClick(Post post) {
        // TODO: Show a dialog to get the comment text
        repository.commentOnPost(post, "A comment", this);
    }

    @Override
    public void onPostShareClick(Post post) {
        repository.sharePost(post, this);
    }

    @Override
    public void onUsernameClick(String userId) {
        // TODO: Handle username click
    }

    @Override
    public void onFilterSelected(String filter) {
        currentFilter = filter;
        loadHomeFeed();
    }

    @Override
    public void onPostCreateClick(String postText) {
        repository.createPost(postText, this);
    }

    @Override
    public void onPostCreated() {
        Toast.makeText(getContext(), "Post created!", Toast.LENGTH_SHORT).show();
        loadHomeFeed();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getContext(), "Action successful!", Toast.LENGTH_SHORT).show();
    }
}
