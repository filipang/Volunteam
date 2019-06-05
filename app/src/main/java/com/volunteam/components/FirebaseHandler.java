package com.volunteam.components;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
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
        //DOWNLOAD DATABASE HERE//

        firebaseHandler.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Voluntariat> voluntariatArrayList = new ArrayList<>();
                Log.d("HOW MANYY??", "THIS MANY: " + dataSnapshot.child("Voluntariate").getChildren());
                for (DataSnapshot ds : dataSnapshot.getChildren()) Log.d("debugare", ds.getKey());
                for(DataSnapshot ds : dataSnapshot.child("Voluntariate").getChildren()){
                    Voluntariat vol = new Voluntariat();

                    vol.setId_vol(Integer.parseInt(ds.child("id_vol").getValue().toString()));
                    vol.setName(ds.child("name").getValue().toString());
                    vol.setImageList(new ArrayList<String>());
                    if(URLUtil.isValidUrl(ds.child("imageURL").getValue().toString())) {
                        vol.setImageURL(ds.child("imageURL").getValue().toString());
                        vol.getImageList().add(vol.getImageURL());
                    }
                    else{
                        vol.setImageURL("https://twt-thumbs.washtimes.com/media/image/2017/12/04/charity_139519124_c0-27-1000-610_s885x516.jpg?949041b076b454e3e5f1da61a91f9d631dac3264");
                        vol.getImageList().add("https://twt-thumbs.washtimes.com/media/image/2017/12/04/charity_139519124_c0-27-1000-610_s885x516.jpg?949041b076b454e3e5f1da61a91f9d631dac3264");
                    }
                    if(URLUtil.isValidUrl(ds.child("imageURL1").getValue().toString()))vol.getImageList().add(ds.child("imageURL1").getValue().toString());
                    if(URLUtil.isValidUrl(ds.child("imageURL2").getValue().toString()))vol.getImageList().add(ds.child("imageURL2").getValue().toString());
                    if(URLUtil.isValidUrl(ds.child("imageURL3").getValue().toString()))vol.getImageList().add(ds.child("imageURL3").getValue().toString());
                    if(URLUtil.isValidUrl(ds.child("imageURL4").getValue().toString()))vol.getImageList().add(ds.child("imageURL4").getValue().toString());
                    if(URLUtil.isValidUrl(ds.child("imageURL5").getValue().toString()))vol.getImageList().add(ds.child("imageURL5").getValue().toString());
                    vol.setDescription(ds.child("description").getValue().toString());
                    vol.setDate(new Date(ds.child("day").getValue().toString(), ds.child("month").getValue().toString(),  ds.child("year").getValue().toString()));
                    vol.setId_user(ds.child("organizer").getValue().toString());
                    ArrayList<String> user_list = new ArrayList<>();
                    if(ds.child("users").getChildren()!=null) {
                        for (DataSnapshot snapshot : ds.child("users").getChildren()) {
                            user_list.add(snapshot.getValue().toString());
                        }
                    }
                    vol.userList = user_list;
                    //vol.setLink(ds.child("link").getValue().toString());
                    vol.setDrawable(Voluntariat.loadDrawableFromURL(vol.getImageURL()));
                    voluntariatArrayList.add(vol);
                }
                Voluntariat.setDataSet(voluntariatArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
}
