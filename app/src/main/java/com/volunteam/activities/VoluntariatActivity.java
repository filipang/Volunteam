package com.volunteam.activities;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.ImageSlideAdapter;
import com.volunteam.components.User;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class VoluntariatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voluntariat);

        Integer vol_int = Integer.parseInt(getIntent().getExtras().get("voluntariat").toString());
        final Voluntariat vol = Voluntariat.getDataSet().get(vol_int);
        //Design

        TextView text_titlu = findViewById(R.id.text_titlu);
        TextView text_descriere = findViewById(R.id.text_descriere);
        text_titlu.setText(vol.getName());
        text_descriere.setText(vol.getDescription());


        //!!
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ImageSlideAdapter(this, vol));


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

        //BUTTON DE INSCRIERE SETUP
        Button buttonInscriere = findViewById(R.id.button_inscriere);
        TextView textInscriere = findViewById(R.id.text_inscriere);
        if(User.currentUser.id.equals(vol.getId_user())) {
            textInscriere.setText("Esti organizatorul acestui voluntariat!");
            buttonInscriere.setVisibility(View.GONE);
        }
        else {
            if (!User.currentUser.voluntariate.contains(vol.getId_vol())) {
                textInscriere.setVisibility(View.GONE);
                buttonInscriere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseHandler.getFirebaseHandler().getReference().child("Voluntariate").child(vol.getId_vol().toString()).child("users").child(User.currentUser.id).setValue(User.currentUser.id);
                        vol.userList.add(User.currentUser.id);
                    }
                });
            } else {
                if (vol.userList.contains(User.currentUser.id)) {
                    textInscriere.setText("Inscrierea ta este in asteptare...");
                }
                buttonInscriere.setVisibility(View.GONE);
            }
        }



        final TableLayout table = findViewById(R.id.table);
        //TABLE SETUP
        Log.d("std",User.currentUser.id );
        Log.d("std", vol.getId_user());
        if(!User.currentUser.id.equals(vol.getId_user())) {
            table.setVisibility(View.GONE);
        }
        else{
            for (final String user_id : vol.userList) {
                final TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row_layout, table, false);
                FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> list = new ArrayList<String>();
                        for (DataSnapshot ds : dataSnapshot.child("voluntariate").getChildren()){
                            list.add(ds.getValue().toString());
                        }
                        if (list.contains(user_id)) {
                            row.removeViewAt(4);
                            row.removeViewAt(3);
                        } else {
                            TextView textFirst = (TextView) row.getChildAt(0);
                            TextView textLast = (TextView) row.getChildAt(1);
                            TextView textEmail = (TextView) row.getChildAt(2);
                            TextView textAcc = (TextView) row.getChildAt(3);
                            TextView textRef = (TextView) row.getChildAt(4);
                            table.removeView(row);
                            table.addView(row,0);

                            View.OnClickListener listener_acc = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(user_id).child("voluntariate").child(vol.getId_vol().toString()).setValue(vol.getId_vol().toString());
                                    //ALSO ADD TO DATABASE
                                    row.removeViewAt(4);
                                    row.removeViewAt(3);
                                }
                            };

                            View.OnClickListener listener_ref = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    vol.userList.remove(user_id);
                                    FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(user_id).child("voluntariate").child(vol.getId_vol().toString()).child(user_id).removeValue();
                                    //ALSO REMOVE FROM DATABASE
                                    row.removeViewAt(4);
                                    row.removeViewAt(3);
                                }
                            };

                            textAcc.setOnClickListener(listener_acc);
                            textRef.setOnClickListener(listener_ref);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                }
            }
    }


    //UTILITY FUNCTION
    public static ArrayList<String> makeCollection(Iterable<DataSnapshot> iter) {
        ArrayList<String> list = new ArrayList<String>();
        for (Object item : iter) {
            list.add((String)item);
        }
        return list;
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
