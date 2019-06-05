package com.volunteam.components;

import android.graphics.drawable.Drawable;

import java.util.List;

public class User {

    public static User currentUser;

    public String firstName;
    public String lastName;
    public String email;
    public List<Integer> voluntariate;
    public String id;
    public String pozaURL;
    public Drawable drawable;

    public User() {}

    public User(String firstName, String lastName, String email, List<Integer> voluntariat, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.voluntariate = voluntariat;
        this.id = id;
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