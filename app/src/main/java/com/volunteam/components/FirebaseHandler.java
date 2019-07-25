package com.volunteam.components;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.JsonReader;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHandler {

    static private FirebaseHandler firebaseHandler;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    public DataSnapshot data;

    public FirebaseHandler(FirebaseAuth auth, FirebaseUser user, FirebaseDatabase database, DatabaseReference reference) {
        this.auth = auth;
        this.user = user;
        this.database = database;
        this.reference = reference;
    }

    public static FirebaseHandler getFirebaseHandler() {
        return firebaseHandler;
    }

    public static void setFirebaseHandler(FirebaseHandler firebaseHandler) {
        FirebaseHandler.firebaseHandler = firebaseHandler;
        downloadDataSet();
    }

    public static void downloadDataSet(){
        //DOWNLOAD DATABASE HERE//

        firebaseHandler.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadDataSet(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static void loadDataSet(DataSnapshot dataSnapshot){
        ArrayList<Voluntariat> voluntariatArrayList = new ArrayList<>();
        Log.d("HOW MANYY??", "THIS MANY: " + dataSnapshot.child("Voluntariate").getChildren());
        for (DataSnapshot ds : dataSnapshot.getChildren()) Log.d("debugare", ds.getKey());
        for(DataSnapshot ds : dataSnapshot.child("Voluntariate").getChildren()){
            Log.d("Children", "WE GOT +ONE!");
            Voluntariat vol = new Voluntariat();

            Log.d("testingthis", ds.child("id_vol").getValue().toString());
            vol.setId_vol(Integer.parseInt(ds.child("id_vol").getValue().toString()));
            vol.setName(checkNull(ds.child("name").getValue().toString()));
            vol.setImageList(new ArrayList<String>());
            if(URLUtil.isValidUrl(ds.child("imageURL").getValue().toString())) {
                vol.setImageURL(ds.child("imageURL").getValue().toString());
                vol.getImageList().add(vol.getImageURL());
            }
            else{
                vol.setImageURL("https://twt-thumbs.washtimes.com/media/image/2017/12/04/charity_139519124_c0-27-1000-610_s885x516.jpg?949041b076b454e3e5f1da61a91f9d631dac3264");
                vol.getImageList().add("https://twt-thumbs.washtimes.com/media/image/2017/12/04/charity_139519124_c0-27-1000-610_s885x516.jpg?949041b076b454e3e5f1da61a91f9d631dac3264");
            }
            if(URLUtil.isValidUrl(ds.child("imageURL1").getValue().toString())) vol.getImageList().add(ds.child("imageURL1").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL2").getValue().toString())) vol.getImageList().add(ds.child("imageURL2").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL3").getValue().toString())) vol.getImageList().add(ds.child("imageURL3").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL4").getValue().toString())) vol.getImageList().add(ds.child("imageURL4").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL5").getValue().toString())) vol.getImageList().add(ds.child("imageURL5").getValue().toString());
            vol.setDescription(checkNull(ds.child("description").getValue().toString()));
            vol.setDate(new Date(ds.child("day").getValue().toString(), ds.child("month").getValue().toString(),  ds.child("year").getValue().toString()));
            vol.setId_user(checkNull(ds.child("organizer").getValue().toString()));
            vol.setLink(ds.child("link").getValue().toString());
            ArrayList<String> user_list = new ArrayList<>();
            if(ds.child("users").getChildren()!=null) {
                for (DataSnapshot snapshot : ds.child("users").getChildren()) {
                    user_list.add(checkNull(snapshot.getValue().toString()));
                }
            }
            vol.userList = user_list;
            //vol.setLink(ds.child("link").getValue().toString());
            vol.setDrawable(Voluntariat.loadDrawableFromURL(vol.getImageURL()));
            voluntariatArrayList.add(vol);
        }
        Voluntariat.setDataSet(voluntariatArrayList);
    }

    public static void refreshSmallAdapter(final SmallRecyclerAdapter adapter, final SwipeRefreshLayout refreshLayout, final  SearchBarManager searchBarManager){
        firebaseHandler.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadDataSet(dataSnapshot);
                adapter.update();
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(), (ArrayList<Voluntariat>) adapter.mDataSet);
                searchBarManager.searchSmall(SearchBarManager.getCurrentQuery(), Voluntariat.getDataSet(), adapter);
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(), (ArrayList<Voluntariat>) adapter.mDataSet);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void refreshLargeAdapter(final LargeRecyclerAdapter adapter, final SwipeRefreshLayout refreshLayout, final SearchBarManager searchBarManager){
        firebaseHandler.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadDataSet(dataSnapshot);
                adapter.update();
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(), (ArrayList<Voluntariat>) adapter.mDataSet);
                searchBarManager.searchLarge(SearchBarManager.getCurrentQuery(), Voluntariat.getDataSet(), adapter);
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(), (ArrayList<Voluntariat>) adapter.mDataSet);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void setVoluntariatValue(DatabaseReference reference, String id, String field, Object value){
        reference.child("Voluntariate").child(id).child(field).setValue(value);
    }

    public DataSnapshot getData() {
        return data;
    }

    public void setData(DataSnapshot data) {
        this.data = data;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public static String checkNull(String s){
        return s==null?"":s;
    }
}
