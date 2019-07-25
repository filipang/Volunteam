package com.volunteam.components;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Voluntariat implements Serializable {

    //Voluntariat dataSet
    static private ArrayList<Voluntariat> dataSet;
    static public String[] fieldArray = {"id_vol", "name", "imageURL", "imageURL1", "imageURL2", "imageURL3", "imageURL4", "imageURL5", "description", "day", "month", "year", "organizer", "link"};

    //This will include all info about a "Voluntariat" entry
    private Integer id_vol;
    private String id_user;
    private String name;
    private String imageURL;
    private List<String> imageList;
    private String description;
    private Boolean amISigned;
    private transient Drawable drawable;
    private Date date;
    private String link;
    private User user;
    public ArrayList<String> userList;

    public Voluntariat(){
        userList = new ArrayList<>();
    }

    public Voluntariat(Integer id_vol, String id_user, String name, String imageURL, List<String> imageList, String description, Boolean amISigned, String day, String month, String year, String link) {
        userList = new ArrayList<>();
        this.id_vol = id_vol;
        this.id_user = id_user;
        this.name = name;
        this.imageURL = imageURL;
        this.imageList = imageList;
        this.description = description;
        this.amISigned = amISigned;
        this.date = new Date(day, month, year);
        this.link = link;
    }

    //Returns drawable image found at URL (Method will be moved to Database Handler class)
    public static Drawable loadDrawableFromURL(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    //Returns the Voluntariat object with the given id
    public static Voluntariat getVoluntariatWithId(Integer id){
        for(Voluntariat voluntariat : dataSet){
            if (voluntariat.id_vol == id){
                return voluntariat;
            }
        }
        return null;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static ArrayList<Voluntariat> getDataSet() {
        return dataSet;
    }

    public Integer getIdVol() {
        return id_vol;
    }

    public static void setDataSet(ArrayList<Voluntariat> dataSet) {
        if(Voluntariat.dataSet != null) {
            Voluntariat.dataSet.clear();
            Voluntariat.dataSet.addAll(dataSet);
        }
        else {
            Voluntariat.dataSet = dataSet;
        }
    }

    public Integer getId_vol() {
        return id_vol;
    }

    public void setId_vol(Integer id_vol) {
        this.id_vol = id_vol;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getAmISigned() {
        return amISigned;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmISigned(Boolean amISigned) {
        this.amISigned = amISigned;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void fetchUserData(){
        FirebaseHandler firebaseHandler = FirebaseHandler.getFirebaseHandler();
        final DatabaseReference database = firebaseHandler.getReference().child(id_user);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User new_user = new User();
                new_user.setFirstName(dataSnapshot.child("firstName").getValue().toString());
                new_user.setEmail(dataSnapshot.child("email").getValue().toString());
                new_user.setLastName(dataSnapshot.child("lastName").getValue().toString());
                new_user.setPozaURL(dataSnapshot.child("pozaProfil").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
