package com.volunteam.activities;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.volunteam.R;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.SearchBarManager;
import com.volunteam.components.User;

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

        //Disable the enabled by default search bar
        SearchBarManager searchBarManager = new SearchBarManager();
        searchBarManager.disableSearchBar(this);

        //PROFILE DETAILS SETUP
        TextView number = findViewById(R.id.large_entry_description);
        TextView name = findViewById(R.id.large_entry_organizer);
        number.setText(""+User.currentUser.voluntariate.size());
        name.setText(User.currentUser.lastName + " " + User.currentUser.firstName);

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
