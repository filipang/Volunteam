package com.volunteam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.Date;
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.LargeRecyclerAdapter;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.RecyclerManager;
import com.volunteam.components.SearchBarManager;
import com.volunteam.components.SortSpinnerManager;
import com.volunteam.components.User;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;
    LargeRecyclerAdapter largeRecyclerAdapter; //Same as mAdapter
    RecyclerView.LayoutManager layoutManager;

    NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        RecyclerManager recyclerManager = new RecyclerManager();
        recyclerManager.createRecyclerView(this);

        SortSpinnerManager sortSpinnerManager = new SortSpinnerManager();
        sortSpinnerManager.createSortSpinnerManager(this, recyclerManager);

        //SEARCH BAR SETUP

        SearchBarManager searchBarManager = new SearchBarManager();
        searchBarManager.createSearchBar(this, recyclerManager, Voluntariat.getDataSet());


        FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(FirebaseHandler.getFirebaseHandler().getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User.currentUser = new User(dataSnapshot.child("firstName").getValue().toString(),
                        dataSnapshot.child("lastName").getValue().toString(),
                        dataSnapshot.child("email").getValue().toString(),
                        new ArrayList<Integer>(),
                        dataSnapshot.getKey());
                for(DataSnapshot x : dataSnapshot.child("voluntariate").getChildren()) {
                    User.currentUser.voluntariate.add(Integer.parseInt(x.getValue().toString()));
                }
                TextView textViewHeader = findViewById(R.id.nav_header_textView);
                textViewHeader.setText(User.currentUser.lastName + " " + User.currentUser.firstName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    //Navigation related
    @Override
    public void onBackPressed() {
        if (navigationManager.isDrawerOpen()) {
            navigationManager.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}

