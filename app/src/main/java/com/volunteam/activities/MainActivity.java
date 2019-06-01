package com.volunteam.activities;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.volunteam.R;
import com.volunteam.components.Voluntariat;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spinner setup
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.main_spinner_list, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //!!!
        //Image loading is done on separate slide to avoid NetworkingOnMainThreadException
        new Thread(new GetImageTask()).start();

        //NAV STUFF
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(MainActivity.this);
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
        Log.d("click", "AAAA" + menuItem.getItemId());
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
}

