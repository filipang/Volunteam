package com.volunteam.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.volunteam.R;
import com.volunteam.components.MyAdapter;
import com.volunteam.components.Voluntariat;

import java.util.ArrayList;
import java.util.Arrays;


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

        //Image loading is done on separate slide to avoid NetworkingOnMainThreadException
        //new Thread(new GetImageTask()).start();

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
                        if (!(vol.getDescription().contains(query) || vol.getName().contains(query))) {
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
    //!!!!

    //Task that loads Image solving threading/UI/Networking issues
    /*
    class GetImageTask implements Runnable {

        private Handler handler;

        public GetImageTask() {

            //Handler is set to execute on UI/main thread
            handler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run() {
            for (Voluntariat vol : Voluntariat.getTestList()) {
                vol.setDrawable(Voluntariat.loadDrawableFromURL(vol.getImageURL()));
            }

            //After drawable is loaded, start updating the UI in the UI thread, using a handler
            handler.post(new Runnable() {
                @Override
                public void run() {

                    LinearLayout scrollList  = findViewById(R.id.vol_scroll_list);
                    Voluntariat[] voluntariatList = Voluntariat.getTestList(); // = DatabaseHandler.fetchVoluntariatList(User.me);

                    for (Voluntariat vol : voluntariatList) {

                        //Create entry
                        LayoutInflater inflater = getLayoutInflater();
                        LinearLayout element = (LinearLayout) inflater.inflate(R.layout.vol_entry, scrollList, false);
                        scrollList.addView(element);

                        TextView textView = (TextView) element.getChildAt(1);
                        textView.setText(vol.getDescription());

                        ImageView img = (ImageView) element.getChildAt(2);
                        img.setImageDrawable(vol.getDrawable());

                        TextView textView1 = (TextView) element.getChildAt(3);
                        textView1.setText(vol.getName());

                        ImageView img1 = (ImageView) ((ViewGroup)element.getChildAt(0)).getChildAt(0);
                        img1.setImageDrawable(vol.getDrawable());
                    }
                }
            });
        }

    }
    */
}

