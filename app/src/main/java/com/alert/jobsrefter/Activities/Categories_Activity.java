package com.alert.jobsrefter.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alert.jobsrefter.MainActivity;
import com.google.android.gms.ads.LoadAdError;
import com.alert.jobsrefter.Adapters.Categories_Adapter;
import com.alert.jobsrefter.Models.Categories_Model;
import com.alert.jobsrefter.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Categories_Activity extends AppCompatActivity {
    //Views declaration--------------------------------------------------
    GridView grid_cats;
    ImageView btn_back;
    LinearLayout categories;
    ProgressBar progressBar;
    TextView txt_heading;
    ImageView btn_search;

    List<String> list =new ArrayList<>();
    Categories_Model model;
    DatabaseReference jobDb;
    Categories_Adapter adapter;
    String category;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d("admoberror",adError.getMessage().toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        //views init method calling----------------------------------------------
        initializeViews();

        jobDb= FirebaseDatabase.getInstance().getReference().child("Jobs");

        //get Intent Extra-----------------------------------------------------
        final Intent intent = getIntent();
        String heading = intent.getStringExtra("heading");
         category= intent.getStringExtra("category");
        txt_heading.setText(heading);


        //calling get data model--------------------------------------------------
        getData();

        //Set data to gridview----------------------------------------------------
        adapter=new Categories_Adapter(this,list);
        grid_cats.setAdapter(adapter);
        categories.setVisibility(View.VISIBLE);


        //back btn click----------------------------------------------------------
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories_Activity.this, ImagesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category", "Search");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        grid_cats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String d=list.get(position);
                Intent i=new Intent(Categories_Activity.this,ImagesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("cat", d);
                bundle.putString("category",category);
                i.putExtras(bundle);
                startActivity(i);
            }
        });



    }

    private void initializeViews(){
        txt_heading=findViewById(R.id.txt_heading);
        grid_cats=findViewById(R.id.grid_cats);
        progressBar=findViewById(R.id.progressbar);
        categories=findViewById(R.id.categories);
        btn_back=findViewById(R.id.btn_back);
        btn_search = findViewById(R.id.btn_search);
    }


    public  void getData(){

        jobDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String dpt=snapshot.child(category).getValue(String.class);
                    if(list.contains(dpt)){

                    }
                    else {
                        list.add(dpt);

                    }

                   adapter.notifyDataSetChanged();
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
//                for(int i=0;i<list.size();i++){
//                   model=list.get(i);
//                    if(model.getJobid().equals(key)){
//                        itemsList.remove(i);
//                    }
//
//                }



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
    public void onBackPressed() {
        super.onBackPressed();
    }
}