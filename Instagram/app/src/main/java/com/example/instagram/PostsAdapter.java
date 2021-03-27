package com.example.instagram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.paging.PositionalDataSource;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.List;

public class PostsAdapter extends PagedListAdapter<Post, PostsAdapter.ViewHolder> {


    private static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK = new DiffUtil.ItemCallback<Post>() {

        @Override
        public boolean areItemsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
            return false;
        }
    };
    private static final String TAG = "ADAPTER" ;
    Context context;


    public PostsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

   /* private Context context;

    protected PostsAdapter(@NonNull DiffUtil.ItemCallback<Post> diffCallback) {
        super(diffCallback);
    }

    public PostsAdapter(@NonNull AsyncDifferConfig<Post> config, Context context, List<Post> posts) {
        super(config);
        this.context = context;
        this.posts = posts;
    }
*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = getItem(position);
        if(post != null){
            holder.bind(post);
        }
    }

    // define basic query here
    public ParseQuery<Post> getQuery() {
        return ParseQuery.getQuery(Post.class).orderByDescending("createdAt");
    }


    // Clean all elements of the recycler
    /*public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }*/

   /* @Override
    public int getItemCount() {
        return posts.size();
    }*/

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUsername;
        private final TextView tvDescription;
        private final ImageView ivPostImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
        }

        public void bind(Post post) {
            //Bind the post data to the view elements
            try{
                tvUsername.setText(post.getUser().fetchIfNeeded().getUsername());
            }catch(ParseException e){
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

            tvDescription.setText(post.getDescription());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivPostImage);
            }

        }
    }




}
