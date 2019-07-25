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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.RecyclerManager;
import com.volunteam.components.SearchBarManager;
import com.volunteam.components.SmallRecyclerAdapter;
import com.volunteam.components.User;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;

public class VolMeleActivity extends AppCompatActivity{


    SmallRecyclerAdapter smallRecyclerAdapter;
    NavigationManager navigationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_mele);


        //Add side bar to this activity
        navigationManager = new NavigationManager(this);
        navigationManager.createNavBar();


        //RecyclerView managing
        RecyclerManager recyclerManager = new RecyclerManager();
        recyclerManager.createRecyclerWithSmallElements(this, User.getVolFromUser(FirebaseHandler.getFirebaseHandler().getData()), new SearchBarManager());


        //Search bar managing
        SearchBarManager searchBarManager = new SearchBarManager();
        searchBarManager.createSearchBar(this, recyclerManager.smallRecyclerAdapter, Voluntariat.getDataSet());


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
