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
import android.widget.TextView;

import com.volunteam.R;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.User;
import com.volunteam.components.Voluntariat;

import org.w3c.dom.Text;

public class ProfilActivity extends AppCompatActivity{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

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

        //DISABLE SEARCH BAR
        SearchView searchView = findViewById(R.id.search_view);
        if(searchView!=null)searchView.setVisibility(View.GONE);

        //PROFILE DETAILS SETUP
        TextView number = findViewById(R.id.textViewNoVoluntariate);
        TextView name = findViewById(R.id.textProfileName);
        number.setText(""+User.currentUser.voluntariate.size());
        name.setText(User.currentUser.lastName + " " + User.currentUser.firstName);

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
