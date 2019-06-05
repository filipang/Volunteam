package com.volunteam.activities;

import android.content.ContentProvider;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.Date;
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.MyAdapter;
import com.volunteam.components.User;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    MyAdapter myAdapter; //Same as mAdapter
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        Log.d("HOW MANYY??", "THIS MANY: " + Voluntariat.getDataSet().size());
        myAdapter = new MyAdapter(Voluntariat.getDataSet());
        mAdapter = myAdapter;
        recyclerView.setAdapter(mAdapter);


        //Spinner setup
        Spinner spinner = findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.main_spinner_list, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("sort pls", "SORT???");
                switch (position){
                    case 0:
                        //SORT BY POPULARITY
                        Log.d("sort pls", "SORT 0");
                        Comparator<Voluntariat> comparator = new Comparator<Voluntariat>() {
                            @Override
                            public int compare(Voluntariat o1, Voluntariat o2) {
                                return o1.userList.size() - o2.userList.size();
                            }
                        };
                        Collections.sort(myAdapter.mDataSet, comparator);
                        Collections.sort(Voluntariat.getDataSet(), comparator);
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        //SORT ASCENDING BY DATE
                        Log.d("sort pls", "SORT 1");
                        Comparator<Voluntariat> comparator_date_cresc = new Comparator<Voluntariat>() {
                            @Override
                            public int compare(Voluntariat o1, Voluntariat o2) {
                                Date d1 = o1.getDate(); Date d2 = o2.getDate();
                                Date max = Date.max(d1,d2);
                                if(max == null){
                                    return 0;
                                }
                                else {
                                    if(max == d1){
                                        return 1;
                                    }
                                    else{
                                        return -1;
                                    }

                                }
                            }
                        };
                        Collections.sort(myAdapter.mDataSet, comparator_date_cresc);
                        Collections.sort(Voluntariat.getDataSet(), comparator_date_cresc);
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        //SORT DESCENDING BY DATE
                        Log.d("sort pls", "SORT 2");
                        Comparator<Voluntariat> comparator_date_descresc = new Comparator<Voluntariat>() {
                            @Override
                            public int compare(Voluntariat o1, Voluntariat o2) {
                                Date d1 = o1.getDate(); Date d2 = o2.getDate();
                                Date max = Date.max(d1,d2);
                                if(max == null){
                                    return 0;
                                }
                                else {
                                    if(max == d1){
                                        return -1;
                                    }
                                    else{
                                        return 1;
                                    }

                                }
                            }
                        };
                        Collections.sort(myAdapter.mDataSet, comparator_date_descresc);
                        Collections.sort(Voluntariat.getDataSet(), comparator_date_descresc);
                        myAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Image loading is done on separate slide to avoid NetworkingOnMainThreadException
        //new Thread(new GetImageTask()).start();

        //NAVIGATION SETUP
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);

        FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(FirebaseHandler.getFirebaseHandler().getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User.currentUser = new User(dataSnapshot.child("firstName").getValue().toString(),
                        dataSnapshot.child("lastName").getValue().toString(),
                        dataSnapshot.child("email").getValue().toString(),
                        new ArrayList<Integer>(),
                        dataSnapshot.getKey());
                for(DataSnapshot x : dataSnapshot.child("voluntariate").getChildren()) {
                    User.currentUser.voluntariate.add(Integer.parseInt(x.getValue().toString()));
                }
                TextView textViewHeader = findViewById(R.id.nav_header_textView);
                textViewHeader.setText(User.currentUser.lastName + " " + User.currentUser.firstName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //SEARCH BAR SETUP

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setVisibility(View.VISIBLE);
        final RecyclerView.Adapter finalAdapter =  mAdapter;
        final MyAdapter myFinalAdapter = myAdapter;

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
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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
}

