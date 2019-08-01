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
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.NavigationManager;
import com.volunteam.components.SearchBarManager;
import com.volunteam.components.User;
import com.volunteam.components.Voluntariat;

import java.lang.reflect.Array;
import java.util.Arrays;


public class ValidateActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    public FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    public FirebaseDatabase mDatabase;
    public DatabaseReference volDatabase;
    public DatabaseReference lastOne;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationManager navigationManager;
    SearchBarManager searchBarManager;

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


        //Disable the enabled by default search bar
        searchBarManager = new SearchBarManager();
        searchBarManager.disableSearchBar(this);


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



        //Upload button handling
        final Button buttonImage1 = findViewById(R.id.button_image_1);
        buttonImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


    }

    public void storeVolInDatabase() {


        volDatabase.child("LastVolID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String vol_id = dataSnapshot.getValue().toString();
                Log.d("pula", "" + dataSnapshot.getValue());
                volDatabase.child("LastVolID").setValue(Integer.parseInt(vol_id )+1);
                volDatabase.child("Voluntariate").child(vol_id).child("id_vol").setValue(Integer.parseInt(vol_id));

                LinearLayout layout = findViewById(R.id.editLayout);
                for(int i = 0; i < layout.getChildCount(); i++){
                    if(layout.getChildAt(i).getTag()!=null) {
                        if (Arrays.asList(Voluntariat.fieldArray).contains(layout.getChildAt(i).getTag().toString())) {
                            FirebaseHandler.setVoluntariatValue(volDatabase, vol_id, layout.getChildAt(i).getTag().toString(), ((EditText) layout.getChildAt(i)).getText().toString());
                        }
                    }
                }
                FirebaseHandler.setVoluntariatValue(volDatabase, vol_id, "organizer", User.currentUser.id);
                FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(User.currentUser.id).child("organizedVolCount").setValue(""+(User.currentUser.organizedVolCount+1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        FirebaseHandler.uploadImageToFirebase(intent, this);
    }

    @Override
    protected void onActivityResult(
            int aRequestCode, int aResultCode, Intent aData
    ) {
        switch (aRequestCode) {
            case PICK_IMAGE_REQUEST:
                FirebaseHandler.uploadImageToFirebase(aData, this);
                break;
            /*case SOME_OTHER_REQUEST:
                handleSomethingElse(aData);
                break;*/
        }
        super.onActivityResult(aRequestCode, aResultCode, aData);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.post) {
            storeVolInDatabase();
            startActivity(new Intent(ValidateActivity.this, MainActivity.class));
        }
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

