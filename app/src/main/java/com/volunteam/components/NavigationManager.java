package com.volunteam.components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.volunteam.R;
import com.volunteam.activities.MainActivity;
import com.volunteam.activities.ProfilActivity;
import com.volunteam.activities.ToateVolActivity;
import com.volunteam.activities.ValidateActivity;
import com.volunteam.activities.VolMeleActivity;

public class NavigationManager implements NavigationView.OnNavigationItemSelectedListener{
    public AppCompatActivity context;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    public NavigationManager(AppCompatActivity context){
        this.context = context;

    }

   public void createNavBar(){
        Toolbar toolbar = context.findViewById(R.id.toolbar_main);
        context.setSupportActionBar(toolbar);
        drawer = context.findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav = context.findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("click", "SOMETHING SOMETHING AT LEAST!???  " + menuItem.getOrder());
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.menu_exploreaza:
                intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                break;
            case R.id.menu_toate_vol:
                intent = new Intent(context, ToateVolActivity.class);
                context.startActivity(intent);
                break;
            case R.id.menu_vol_mele:
                intent = new Intent(context, VolMeleActivity.class);
                context.startActivity(intent);
                break;
            case R.id.menu_profil:
                intent = new Intent(context, ProfilActivity.class);
                context.startActivity(intent);
                break;
            case R.id.menu_organizeaza_voluntariat:
                intent = new Intent(context, ValidateActivity.class);
                context.startActivity(intent);
                break;
        }
        return true;
    }


    public boolean isDrawerOpen(){
        return drawer.isDrawerOpen(GravityCompat.START);
    }


    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }
    //!!!!
}
