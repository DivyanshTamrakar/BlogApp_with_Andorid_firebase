package com.example.loginactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MyBlog extends Fragment {

    private RecyclerView mUsersList;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private ImageView toolimage;

    private FirebaseUser firebaseUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_myblog,container,false);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String tool_image = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("MY Posts").child(tool_image);







// --------------------------------------------------- //
        mUsersList = view.findViewById(R.id.UserRecycleView);

        mUsersList.setHasFixedSize(true);

        mUsersList.setLayoutManager(new LinearLayoutManager(getContext()));




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String crrent_user = firebaseUser.getUid();


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("MY Posts").child(crrent_user)
                .limitToLast(20);

        FirebaseRecyclerOptions<my_post> options = new FirebaseRecyclerOptions.Builder<my_post>().
                setQuery(query, my_post.class).build();

        FirebaseRecyclerAdapter<my_post, MyBlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<my_post, MyBlogViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyBlogViewHolder holder, int position, @NonNull my_post model) {

                holder.setTitle(model.getTitle());
                holder.setdes(model.getDescription());
                holder.imag(model.getImage_url());

            }

            @NonNull
            @Override
            public MyBlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_recyle_layout, parent, false);

                MyBlogViewHolder viewHolder = new MyBlogViewHolder(view);

                return viewHolder;
            }
        };


        mUsersList.setAdapter(firebaseRecyclerAdapter);

        firebaseRecyclerAdapter.startListening();

    }


    public static class MyBlogViewHolder extends RecyclerView.ViewHolder {


        View mview;


        public MyBlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTitle(String title) {

            TextView titleview = mview.findViewById(R.id.userRecycle);
            titleview.setText(title);

        }

        public void setdes(String des) {
            TextView de = mview.findViewById(R.id.descriptrecycle);
            de.setText(des);

        }

        public void imag(String u){

            ImageView ph = mview.findViewById(R.id.status_photo);
            Picasso.get().load(u).into(ph);
        }


    }

}
