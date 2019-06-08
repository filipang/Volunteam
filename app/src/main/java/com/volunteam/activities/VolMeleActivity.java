package com.volunteam.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.SearchBarManager;
import com.volunteam.components.SmallRecyclerAdapter;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;

public class VolMeleActivity extends AppCompatActivity{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;
    SmallRecyclerAdapter smallRecyclerAdapter; //Same as mAdapter
    RecyclerView.LayoutManager layoutManager;
    NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_mele);

        //Add side bar to this activity
        navigationManager = new NavigationManager(this);
        navigationManager.createNavBar();

        //DECONECTARE BUTTON SETUP
        View img = findViewById(R.id.imgdeconctare);
        View txt = findViewById(R.id.logout);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        };
        img.setOnClickListener(listener);
        txt.setOnClickListener(listener);




        //RECYCLERVIEW SETUP
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final AppCompatActivity thisActivity = this;
        FirebaseHandler.getFirebaseHandler().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> listVol = new ArrayList<String>();
                for(DataSnapshot ds : dataSnapshot.child("Users").child(FirebaseHandler.getFirebaseHandler().getAuth().getUid()).child("voluntariate").getChildren()){
                    listVol.add(ds.getValue().toString());
                }
                final ArrayList<Voluntariat> voluntariatArrayList = new ArrayList<>();
                for(Voluntariat vol : Voluntariat.getDataSet()){
                    if(listVol.contains(vol.getId_vol().toString())){
                        voluntariatArrayList.add(vol);
                    }
                }
                smallRecyclerAdapter = new SmallRecyclerAdapter((ArrayList<Voluntariat>)voluntariatArrayList.clone());
                recyclerView.setAdapter(smallRecyclerAdapter);

                //Search bar managing

                SearchBarManager searchBarManager = new SearchBarManager();
                searchBarManager.createSearchBar(thisActivity, smallRecyclerAdapter, Voluntariat.getDataSet());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
