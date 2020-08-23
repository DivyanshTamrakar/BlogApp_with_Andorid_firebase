package com.example.loginactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Tab2Fragment extends Fragment {
    private FirebaseAuth firebaseAuth;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseReference;
    private Button draft;
    private Button publish;
    private EditText title;
    private EditText description;
    private ProgressBar progressBar;
    int value;
    private ImageButton choose;
    private ImageView newPostimg;
    private String current_userid;
    private Uri postImageuri = null;
    private static final int FILE_SELECT_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab2fragment, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        current_userid = firebaseAuth.getCurrentUser().getUid();


        title = view.findViewById(R.id.editText);
        description = view.findViewById(R.id.editText2);
        publish = view.findViewById(R.id.btnpublish);
        choose = view.findViewById(R.id.btnfolder);
        newPostimg = view.findViewById(R.id.google);
        progressBar = view.findViewById(R.id.tambu);


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosefile();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String imges = postImageuri.toString();

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("image_url", imges);
                user.put("description", description.getText().toString().trim());
                user.put("title", title.getText().toString().trim());
                user.put("timestamp", FieldValue.serverTimestamp());
                user.put("user_id", current_userid);


                // Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                progressBar.setVisibility(View.GONE);


                                new AlertDialog.Builder(getContext()).
                                        setTitle("Published").
                                        setMessage("You can check your blog in All blog section").
                                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                // getFragmentManager().beginTransaction().replace(R.id.fragment_container, new Tab1Fragment()).commit();


                                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.replace(R.id.fragment_container, new Tab1Fragment());
                                                fragmentTransaction.commit();

                                            }
                                        }).
                                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).
                                        setCancelable(true).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                                //Log.w(TAG, "Error adding document", e);
                            }
                        });


                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                String uid = current_user.getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("MY Posts");

                // Create a new user with a first and last name
                HashMap<String, String> individualpost = new HashMap<>();
                individualpost.put("image_url", imges);
                individualpost.put("description", description.getText().toString().trim());
                individualpost.put("title", title.getText().toString().trim());
                individualpost.put("user_id", current_userid);

                databaseReference.child(uid).push().setValue(individualpost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Tamrakar divyansh here", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Pehli fursat", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });


        return view;
    }

    public void chosefile() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).
                setMinCropResultSize(1020, 1020).
                setAspectRatio(49, 33).
                start(getContext(), this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageuri = result.getUri();
                newPostimg.setImageURI(postImageuri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

    public void sendData() {

        DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference userNameRef = myRootRef.child("Posts");

        DatabaseReference aka = userNameRef.child("Post" + value);

        DatabaseReference lk = aka.child("Title");

        DatabaseReference mk = aka.child("Description");

        lk.setValue(title.getText().toString().trim());
        mk.setValue(description.getText().toString().trim());

        value++;

    }

}





















