package com.volunteam.components;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;


public class Voluntariat {

    //Self-explanatory test list
    static private Voluntariat[] testList = {new Voluntariat(3, "Un nume de activitate pentru ca e 5 jumate si nu mai am idei",
            "https://images.pexels.com/photos/207962/pexels-photo-207962.jpeg?cs=srgb&dl=artistic-blossom-bright-207962.jpg&fm=jpg",
            "Cursurile de formare “Management de Proiect”, susținute de formatori acreditați, au venit în întâmplinarea liceenilor.",
            true)};


    //This will include all info about a "Voluntariat" entry
    private Integer id;
    private String name;
    private String imageURL;
    private String description;
    private Boolean amISigned;
    private Drawable drawable;

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

    public Drawable getDrawable() {
        return drawable;
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

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
