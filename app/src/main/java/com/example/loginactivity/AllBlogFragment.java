/*
package com.example.loginactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;



public class AllBlogFragment extends Fragment {
   private RecyclerView blog_list_view;
    private List<ExampleItem> blog_list;
    private alblogAdapter mAdapter;
    public  FirebaseFirestore firebaseFirestore;

    private static final String TAG = "Tab1Fragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_allblog,container,false);





        blog_list = new ArrayList<>();


        blog_list_view = view.findViewById(R.id.poppy);


        mAdapter = new alblogAdapter(blog_list);

        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        blog_list_view.setAdapter(mAdapter);





       firebaseFirestore = FirebaseFirestore.getInstance();




        Query firstpost =  firebaseFirestore.collection("users").orderBy("timestamp",Query.Direction.DESCENDING);// to add the data newest first



        firstpost.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        ExampleItem exampleItem = doc.getDocument().toObject(ExampleItem.class);


                        blog_list.add(exampleItem);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        });




        return view;
    }


}
*/