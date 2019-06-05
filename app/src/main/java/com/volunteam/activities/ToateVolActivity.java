package com.volunteam.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.volunteam.R;
import com.volunteam.components.MyAdapter;
import com.volunteam.components.SmallEntryAdapter;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;
import java.util.Arrays;

public class ToateVolActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    SmallEntryAdapter myAdapter; //Same as mAdapter
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toate_vol);

        //RECYCLERVIEW SETUP
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SmallEntryAdapter(Voluntariat.getDataSet());
        recyclerView.setAdapter(mAdapter);

        //NAV STUFF
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);

        //SEARCH BAR SETUP
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setVisibility(View.VISIBLE);
        final RecyclerView.Adapter finalAdapter =  mAdapter;
        final SmallEntryAdapter myFinalAdapter = myAdapter;

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                myFinalAdapter.mDataSet = (ArrayList<Voluntariat>) Voluntariat.getDataSet().clone();
                finalAdapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //SORT DATA SET
                ArrayList<Voluntariat> filteredList = (ArrayList<Voluntariat>) Voluntariat.getDataSet().clone();
                if(!query.equals("")) {
                    for (Voluntariat vol : Voluntariat.getDataSet()) {
                        if (!(vol.getDescription().toLowerCase().contains(query.toLowerCase()) || vol.getName().toLowerCase().contains(query))) {
                            filteredList.remove(vol);
                        }
                    }
                }
                myFinalAdapter.mDataSet = filteredList;
                finalAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
    }

    //Navigation stuff
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        Log.d("click", "XDD1");
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("click", "XDDD2");
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("click", "AAAA" + item.getItemId());
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("click", "SOMETHING SOMETHING AT LEAST!???  " + menuItem.getOrder());
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.menu_exploreaza:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_toate_vol:
                //intent = new Intent(this, ToateVolActivity.class);
                //startActivity(intent);
                break;
            case R.id.menu_vol_mele:
                intent = new Intent(this, VolMeleActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_profil:
                intent = new Intent(this, ProfilActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_organizeaza_voluntariat:
                intent = new Intent(this, ValidateActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //!!!!
}
