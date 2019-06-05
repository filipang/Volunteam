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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.volunteam.R;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

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

        //DISABLE SEARCH BAR
        SearchView searchView = findViewById(R.id.search_view);
        if(searchView!=null)searchView.setVisibility(View.GONE);
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
                intent = new Intent(this, ToateVolActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_vol_mele:
                intent = new Intent(this, VolMeleActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_profil:
                //intent = new Intent(this, ProfilActivity.class);
                //startActivity(intent);
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
