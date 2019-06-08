package com.volunteam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.User;


public class ValidateActivity extends AppCompatActivity implements View.OnClickListener {

    public FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    public FirebaseDatabase mDatabase;
    public DatabaseReference volDatabase;
    public DatabaseReference lastOne;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationManager navigationManager;

    public EditText name, imageURL, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, description,
            link, day, month, year;
    public Button getConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        //Add side bar to this activity
        navigationManager = new NavigationManager(this);
        navigationManager.createNavBar();

        mDatabase = FirebaseDatabase.getInstance();
        volDatabase = mDatabase.getReference();
        lastOne = mDatabase.getReference();
        name = (EditText)findViewById(R.id.name);
        imageURL = (EditText)findViewById(R.id.imageURL);
        imageURL1 = (EditText)findViewById(R.id.imageURL1);
        imageURL2 = (EditText)findViewById(R.id.imageURL2);
        imageURL3 = (EditText)findViewById(R.id.imageURL3);
        imageURL4 = (EditText)findViewById(R.id.imageURL4);
        imageURL5 = (EditText)findViewById(R.id.imageURL5);
        description = (EditText)findViewById(R.id.description);
        getConfirmation = (Button)findViewById(R.id.post);
        day = (EditText)findViewById(R.id.day);
        month = (EditText)findViewById(R.id.month);
        year = (EditText)findViewById(R.id.year);
        link = (EditText)findViewById(R.id.link);
        getConfirmation.setOnClickListener(this);

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

    }

    public void storeVolInDatabase() {


        volDatabase.child("LastVolID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getValue();
                Log.d("pula", "" + dataSnapshot.getValue());
                volDatabase.child("LastVolID").setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + 1);
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("id_vol").setValue(Integer.parseInt(dataSnapshot.getValue().toString()));
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("name").setValue(name.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("imageURL").setValue(imageURL.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("imageURL1").setValue(imageURL1.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("imageURL2").setValue(imageURL2.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("imageURL3").setValue(imageURL3.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("imageURL4").setValue(imageURL4.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("imageURL5").setValue(imageURL5.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("description").setValue(description.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("day").setValue(day.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("month").setValue(month.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("year").setValue(year.getText().toString());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("organizer").setValue(usr.getUid());
                volDatabase.child("Voluntariate").child(dataSnapshot.getValue().toString()).child("link").setValue(link.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.post) {
            storeVolInDatabase();
            startActivity(new Intent(ValidateActivity.this, MainActivity.class));
        }
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

