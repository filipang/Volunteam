package com.volunteam.components;

import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;

import java.util.ArrayList;
import java.util.List;

public class User {

    public static User currentUser;

    public String firstName;
    public String lastName;
    public String email;
    public List<Integer> voluntariate;
    public String id;
    public Integer organizedVolCount;
    public String pozaURL;
    public Drawable drawable;

    public User() {}

    public User(String firstName, String lastName, String email, List<Integer> voluntariat, String id, Integer organizedVolCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.voluntariate = voluntariat;
        this.id = id;
        this.organizedVolCount = organizedVolCount;
    }

    public static ArrayList<Voluntariat> getVolFromUser(DataSnapshot dataSnapshot){
        ArrayList<String> listVol = new ArrayList<String>();
        for(DataSnapshot ds : dataSnapshot.child("Users").child(FirebaseHandler.getFirebaseHandler().getAuth().getUid()).child("voluntariate").getChildren()){
            listVol.add(ds.getValue().toString());
        }
        ArrayList<Voluntariat> voluntariatArrayList = new ArrayList<>();
        for(Voluntariat vol : Voluntariat.getDataSet()){
            if(listVol.contains(vol.getId_vol().toString())){
                voluntariatArrayList.add(vol);
            }
        }
        return voluntariatArrayList;
    }

    public static ArrayList<Voluntariat> getVolFromUser(DataSnapshot dataSnapshot, String userID){
        ArrayList<String> listVol = new ArrayList<String>();
        for(DataSnapshot ds : dataSnapshot.child("Users").child(userID).child("voluntariate").getChildren()){
            listVol.add(ds.getValue().toString());
        }
        ArrayList<Voluntariat> voluntariatArrayList = new ArrayList<>();
        for(Voluntariat vol : Voluntariat.getDataSet()){
            if(listVol.contains(vol.getId_vol().toString())){
                voluntariatArrayList.add(vol);
            }
        }
        return voluntariatArrayList;
    }



    public static void initializeCurrentUser(DataSnapshot userSnapshot){

        Log.d("mydebug",userSnapshot.child("firstName").toString());
        User.currentUser = new User(userSnapshot.child("firstName").getValue().toString(),
                userSnapshot.child("lastName").getValue().toString(),
                userSnapshot.child("email").getValue().toString(),
                new ArrayList<Integer>(),
                userSnapshot.getKey(), Integer.parseInt(userSnapshot.child("organizedVolCount").getValue().toString()));


        for(DataSnapshot x : userSnapshot.child("voluntariate").getChildren()) {
            User.currentUser.voluntariate.add(Integer.parseInt(x.getValue().toString()));
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getVoluntariate() {
        return voluntariate;
    }

    public void setVoluntariate(List<Integer> voluntariate) {
        this.voluntariate = voluntariate;
    }

    public String getPozaURL() {
        return pozaURL;
    }

    public void setPozaURL(String pozaURL) {
        this.pozaURL = pozaURL;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}