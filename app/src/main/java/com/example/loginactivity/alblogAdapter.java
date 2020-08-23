package com.example.loginactivity;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;


public class alblogAdapter extends RecyclerView.Adapter<alblogAdapter.allblogViewHolder> {

    public List<ExampleItem> examplelist;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    public Context context;

    public alblogAdapter(List<ExampleItem> examplelist) {

        this.examplelist = examplelist;


    }

    @NonNull
    @Override
    public alblogAdapter.allblogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.all_blog_recycle, parent, false);

        mAuth = FirebaseAuth.getInstance();

        firebaseUser = mAuth.getCurrentUser();


        context = parent.getContext();
        return new allblogViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final allblogViewHolder holder, int position) {

        String descdata = examplelist.get(position).getDescription();
        String titledata = examplelist.get(position).getTitle();
        String image_url = examplelist.get(position).getImage_url();
        long mili = examplelist.get(position).getTimestamp().getTime();
        String datestring = (String) DateFormat.format("dd/MM/yyyy", new Date(mili));
        holder.setDescText(descdata);
        holder.settitleText(titledata);
        holder.setImage(image_url);
        holder.setDate(datestring);

        final String user_id = examplelist.get(position).getUser_id();



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String display_image = dataSnapshot.child("image").getValue(String.class);

                System.out.println("akash" + display_image);

                holder.setim(display_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                System.out.println("akash  " + user_id);


                Intent profile_intent = new Intent(context, IndivPost.class);
                profile_intent.putExtra("user_id", user_id);
                context.startActivity(profile_intent);


            }
        });


        // Likes Feature


    }

    @Override
    public int getItemCount() {
        return examplelist.size();
    }


    public class allblogViewHolder extends RecyclerView.ViewHolder {

        private View mview;
        private TextView descview;
        private TextView titleview;
        private ImageView blogimageview;
        private TextView blog_date;
        private ImageView likebtn;
        private TextView likeviewcount;
        private CircularImageView img;


        public allblogViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;


        }


        public void setDescText(String desctext) {
            descview = mview.findViewById(R.id.descriptrecycle);
            descview.setText(desctext);
        }

        public void settitleText(String titlii) {
            titleview = mview.findViewById(R.id.userRecycle);
            titleview.setText(titlii);

        }

        public void setImage(String downloaduri) {

            blogimageview = mview.findViewById(R.id.status_photo);
            Glide.with(context).load(downloaduri).into(blogimageview);
        }


        public void setim(String url) {

            img = mview.findViewById(R.id.imagerecycle);
            Glide.with(context).load(url).into(img);
        }

        public void setDate(String data) {

            blog_date = mview.findViewById(R.id.dateRecycle);
            blog_date.setText(data);

        }



    }


}
