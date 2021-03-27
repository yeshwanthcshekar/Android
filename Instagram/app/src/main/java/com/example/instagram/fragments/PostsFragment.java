package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.ParseDataSourceFactory;
import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PostsFragment";

    protected PostsAdapter postsAdapter;
    ArrayList<Post> allPosts;
    private static RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    LiveData<PagedList<Post>> posts;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext());
        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        //initial page size to fetch can be configured here
        PagedList.Config pagedListConfig = new PagedList.Config.Builder().setEnablePlaceholders(true)
                                                                            .setPrefetchDistance(10)
                                                                            .setInitialLoadSizeHint(10)
                                                                            .setPageSize(10)
                                                                            .build();

        ParseDataSourceFactory parseDataSourceFactory = new ParseDataSourceFactory();
        posts = new LivePagedListBuilder<>(parseDataSourceFactory, pagedListConfig).build();
        posts.observe(this, new Observer<PagedList<Post>>() {
            @Override
            public void onChanged(PagedList<Post> posts) {
                postsAdapter.submitList(posts);
            }
        });

        swipeContainer = view.findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.observe((LifecycleOwner) getContext(), new Observer<PagedList<Post>>() {
                    @Override
                    public void onChanged(PagedList<Post> posts) {
                       // parseDataSourceFactory.dataSource.invalidate();
                        postsAdapter.submitList(posts);
                        swipeContainer.setRefreshing(false);
                    }
                });
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public static void smoothScroll() {
        if (rvPosts != null) {
            rvPosts.post(new Runnable() {
                @Override
                public void run() {
                    rvPosts.smoothScrollToPosition(0);
                }
            });
        }
    }


    protected void queryPosts() {

        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        // Specify the object id
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> thePosts, ParseException e) {
                if(e != null){
                    //Log.e(TAG, " Issue with getting posts",e);
                    return;
                }
                for(Post post: thePosts){
                    // Log.i(TAG, "Post:" +  post.getDescription() + "Username: " + post.getUser().getUsername());
                }

                allPosts.clear();
                allPosts.addAll(thePosts);
                postsAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }


}