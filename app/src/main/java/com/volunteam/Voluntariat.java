package com.volunteam;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class Voluntariat {

    //Self-explanatory test list
    static private Voluntariat[] testList = {new Voluntariat(3, "Jucarii pentru copii",
            "https://images.pexels.com/photos/207962/pexels-photo-207962.jpeg?cs=srgb&dl=artistic-blossom-bright-207962.jpg&fm=jpg",
            "Cursurile de formare “Management de Proiect”, susținute de formatori acreditați, au venit în întâmplinarea liceenilor.",
            true)};


    //This will include all info about a "Voluntariat" entry
    private Integer id;
    private String name;
    private String imageURL;
    private String description;
    private Boolean amISigned;

    public Voluntariat(Integer id, String name, String imageURL, String description, Boolean amISigned) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.description = description;
        this.amISigned = amISigned;
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

    //Alternative version for method
    /*
    public static Drawable loadDrawableFromURL(String url){
        try {
            Bitmap x;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            x = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(Resources.getSystem(), x);
        }catch (Exception e){
            return null;
        }
    }
    */

    public static Voluntariat[] getTestList() {
        return testList;
    }

    public Integer getId() {
        return id;
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

    public void setId(Integer id) {
        this.id = id;
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
}
