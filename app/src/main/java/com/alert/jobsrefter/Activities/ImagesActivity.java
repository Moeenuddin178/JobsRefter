package com.alert.jobsrefter.Activities;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.alert.jobsrefter.Adapters.Images_Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;

import com.alert.jobsrefter.Models.Jobs_Model;
import com.alert.jobsrefter.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    // ArrayList for person names
    private static final int storagePermission = 1100;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    RecyclerView recyclerView;
    Images_Adapter recyclerViewAdapter;
    DatabaseReference jobsDb;
    Jobs_Model jobModel;
    TextView txt_heading;
    ImageView btn_back;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    SearchView SearchView;
    List<Jobs_Model> itemsList;
    String cat, category, heading;
    FirebaseRecyclerOptions<Jobs_Model> recyclerOptions;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSION_REQUEST_CODE);
            }


        itemsList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_images);
        txt_heading = findViewById(R.id.txt_heading);
        SearchView = findViewById(R.id.Search_view);
        btn_back = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressbar);
        linearLayout = findViewById(R.id.linearlayout);
        jobsDb = FirebaseDatabase.getInstance().getReference().child("Jobs");

//get Intent Extra-----------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        try {
            cat = bundle.getString("cat");
            category = bundle.getString("category");
            heading = bundle.getString("heading");
        } catch (Exception e) {
            cat="";
            category="";
            heading="";

        }
        txt_heading.setText(cat);

        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                recyclerViewAdapter.getFilter().filter(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {
                recyclerViewAdapter.getFilter().filter(queryString);
                return false;
            }
        });
        if (category.equals("latest")) {
            txt_heading.setText(heading);
            getSortData();
            // set a LinearLayoutManager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
          //  linearLayoutManager.setReverseLayout(true);
           // linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);

        } else if (category.equals("Search")) {
            linearLayout.setVisibility(View.GONE);
            SearchView.setVisibility(View.VISIBLE);
            getSortData();
            // set a LinearLayoutManager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
             linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        else {
            getData();
            // set a LinearLayoutManager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
            recyclerView.setLayoutManager(linearLayoutManager);

        }


        //back btn click----------------------------------------------------------
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewAdapter = new Images_Adapter(this, itemsList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }


    public void getData() {

        jobsDb.orderByChild(category).equalTo(cat).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {

                    jobModel = snapshot.getValue(Jobs_Model.class);
                    itemsList.add(jobModel);
                    recyclerViewAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
//                    nodata.setVisibility(View.GONE);
//                    data.setVisibility(View.VISIBLE);
//                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                String key=snapshot.getKey();
//
//                for(int i=0;i<itemsList.size();i++){
//                    jobModel=itemsList.get(i);
//                    if(jobModel.getJobid().equals(key)){
//                        itemsList.remove(i);
//                    }
//
//                }
//
//                recyclerView.setAdapter(recyclerViewAdapter);
//                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void getSortData() {

        jobsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {

                    jobModel = snapshot.getValue(Jobs_Model.class);
                    itemsList.add(jobModel);
                    Collections.reverse(itemsList);
                    recyclerViewAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
//                    nodata.setVisibility(View.GONE);
//                    data.setVisibility(View.VISIBLE);
//                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                String key=snapshot.getKey();
//
//                for(int i=0;i<itemsList.size();i++){
//                    jobModel=itemsList.get(i);
//                    if(jobModel.getJobid().equals(key)){
//                        itemsList.remove(i);
//                    }
//
//                }
//
//                recyclerView.setAdapter(recyclerViewAdapter);
//                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == storagePermission) {

            // getPosts(false);

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //permission not granted
            }

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted

                recyclerViewAdapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
                //loadImageIntent();
            }

        }
    }
}