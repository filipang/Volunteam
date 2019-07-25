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

        //Downloads the database and than proceeds to setup the Activity
        //onDataChange gets called when the download is finished and dataSnapshot is the downloaded
        //data



    }

    @Override
    public void onResume() {
        super.onResume();
        final AppCompatActivity context = this;
        FirebaseHandler.getFirebaseHandler().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseHandler.getFirebaseHandler().setData(dataSnapshot);
                FirebaseHandler.loadDataSet(dataSnapshot);
                Log.d("mydebug", dataSnapshot.child("Users").child(FirebaseHandler.getFirebaseHandler().getAuth().getUid()).toString());
                DataSnapshot userSnapshot = dataSnapshot.child("Users").child(FirebaseHandler.getFirebaseHandler().getAuth().getUid());
                User.initializeCurrentUser(userSnapshot);

                //Add side bar to this activity
                navigationManager = new NavigationManager(context);
                navigationManager.createNavBar();

                //Recycler view setup
                RecyclerManager recyclerManager = new RecyclerManager();
                recyclerManager.createRecyclerWithLargeElements(context, Voluntariat.getDataSet(), new SearchBarManager());

                //Sort spinner setup
                SortSpinnerManager sortSpinnerManager = new SortSpinnerManager();
                sortSpinnerManager.createSortSpinnerManager(context, recyclerManager);

                //Search bar setup
                SearchBarManager searchBarManager = new SearchBarManager();
                searchBarManager.createSearchBar(context, recyclerManager, Voluntariat.getDataSet());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Overrides the back button so that it closes the drawer, and if the drawer is closed the back
    //button will function normally
    @Override
    public void onBackPressed() {
        if (navigationManager.isDrawerOpen()) {
            navigationManager.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}

