package com.volunteam.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.volunteam.R;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.SearchBarManager;
import com.volunteam.components.SmallRecyclerAdapter;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;

public class ToateVolActivity extends AppCompatActivity{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;
    SmallRecyclerAdapter smallRecyclerAdapter; //Same as mAdapter
    RecyclerView.LayoutManager layoutManager;
    NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toate_vol);

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

        smallRecyclerAdapter = new SmallRecyclerAdapter(Voluntariat.getDataSet());
        recyclerView.setAdapter(smallRecyclerAdapter);

        //Search bar managing

        SearchBarManager searchBarManager = new SearchBarManager();
        searchBarManager.createSearchBar(this, smallRecyclerAdapter, Voluntariat.getDataSet());
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
    //!!!!
}
