package com.volunteam.components;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;


public class Voluntariat implements Serializable {

    //Self-explanatory test list
    static private Voluntariat[] testList = {new Voluntariat(3, "5","Un nume de activitate pentru ca e 5 jumate si nu mai am idei",
            "https://images.pexels.com/photos/207962/pexels-photo-207962.jpeg?cs=srgb&dl=artistic-blossom-bright-207962.jpg&fm=jpg", null,
            "Cursurile de formare “Management de Proiect”, susținute de formatori acreditați, au venit în întâmplinarea liceenilor.",
            true, "5", "6", "1999", " asdfasdfasdf ")};


    //This will include all info about a "Voluntariat" entry
    private Integer id_vol;
    private String id_user;
    private String name;
    private String imageURL;
    private List<String> imageList;
    private String description;
    private Boolean amISigned;
    private transient Drawable drawable;
    private transient Date date;
    private String link;

    public Voluntariat(Integer id_vol, String id_user, String name, String imageURL, List<String> imageList, String description, Boolean amISigned, String day, String month, String year, String link) {
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

    public static Voluntariat[] getTestList() {
        return testList;
    }

    public Integer getIdVol() {
        return id_vol;
    }

    public static void setTestList(Voluntariat[] testList) {
        Voluntariat.testList = testList;
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
}
