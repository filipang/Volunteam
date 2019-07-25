package com.volunteam.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.AttendantTableManager;
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.ImageSlideAdapter;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.SearchBarManager;
import com.volunteam.components.User;
import com.volunteam.components.Voluntariat;
import com.volunteam.components.VoluntariatInfoManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class VoluntariatActivity extends AppCompatActivity{

    SearchBarManager searchBarManager;
    NavigationManager navigationManager;
    Voluntariat vol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voluntariat);

        //Add side bar to this activity
        navigationManager = new NavigationManager(this);
        navigationManager.createNavBar();

        //Disable the enabled by default search bar
        searchBarManager = new SearchBarManager();
        searchBarManager.disableSearchBar(this);

        //Gets the voluntariatID from the intent, and uses it to get the Voluntariat object
        Integer vol_int = Integer.parseInt(getIntent().getExtras().get("voluntariat").toString());
        Log.d("lol", "Tried get vol with id " + vol_int);
        vol = Voluntariat.getVoluntariatWithId(vol_int);
        VoluntariatInfoManager infoManager = new VoluntariatInfoManager();
        infoManager.setVoluntariatInfo(this, vol);

        AttendantTableManager tableManager = new AttendantTableManager();
        tableManager.createAttendantTableManager(this, vol);
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
