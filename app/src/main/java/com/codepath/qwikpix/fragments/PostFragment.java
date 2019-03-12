package com.codepath.qwikpix.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.qwikpix.Post;
import com.codepath.qwikpix.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class PostFragment extends Fragment {

    public static final String TAG = "PostFragment";
    private RecyclerView rvPosts;

    // oncreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);

        //create the adapter
        //create the data source

        //set the adapter on the recycler view
        //set the layout manager on the recycler view
        queryPosts();
    }

    private void queryPosts(){
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }
                for(int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    if(post.getUser() != null) {
                        Log.d(TAG, "Post: " + posts.get(i).getDescription() + " username: " + posts.get(i).getUser().getUsername());
                    }
                }
            }
        });
    }
}
