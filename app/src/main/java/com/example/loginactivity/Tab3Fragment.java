package com.example.loginactivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class Tab3Fragment extends Fragment {
    private CircularImageView img;
    private ProgressBar progressBar;
    private EditText setupname;
    public TextView setupbtn,profileName;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    public  String myValue;
    private String current_user;
    String download_url = null;
    private  Uri mainImageuri = null ;
    public static final String MY_PREFS_NAME = "MyPrefsFile";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3fragment,container,false);


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        current_user = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);


        storageReference = FirebaseStorage.getInstance().getReference();



        progressBar = view.findViewById(R.id.pro);

        profileName=view.findViewById(R.id.profilename);

        setupbtn  = view.findViewById(R.id.viewpro);

        img = view.findViewById(R.id.image);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("username").getValue(String.class);
                final String display_image = dataSnapshot.child("image").getValue(String.class);

                if (!display_image.equals("default")) {
                    Picasso.get().load(display_image).networkPolicy(NetworkPolicy.OFFLINE).into(img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                            Picasso.get().load(display_image).into(img);// picassso online
                        }
                    });

                }
                profileName.setText(display_name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosefile();
            }
        });





        setupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                progressBar.setVisibility(View.VISIBLE);


                if(mainImageuri != null){

                    final StorageReference ref = storageReference.child("picture.jpg");

                    ref.putFile(mainImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Profile photo saved", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){


                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUrl = uri;
                                }
                            });

                        }
                    });


                }else{
                    progressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(getContext()).setTitle("Profile Photo not selected ").
                            setMessage("You did not select a profile.Please select a profile photo").
                            setPositiveButton("choose", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    choosefile();
                                }
                            }).setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).show();
                }


            }
        });






        if(storageReference!= null) {

            storageReference.child("picture.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into(img);

                }
            });

        }






        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageuri = result.getUri();


                String current_uid = firebaseUser.getUid();


                final StorageReference ref = storageReference.child("picture.jpg").child(current_uid + ".jpg");


                ref.putFile(mainImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Toast.makeText(getContext(), "Profile photo saved", Toast.LENGTH_SHORT).show();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;

                                download_url = downloadUrl.toString();

                                databaseReference.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        Toast.makeText(getActivity(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });

                            }
                        });

                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

    public void choosefile(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(1,1).
                start(getContext(),this);
    }






}

