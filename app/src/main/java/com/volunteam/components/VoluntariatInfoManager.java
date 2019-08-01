package com.volunteam.components;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;

public class VoluntariatInfoManager {

    //This function updates the VoluntariatActivity's information
    public void setVoluntariatInfo(final AppCompatActivity context, final  Voluntariat vol){

        //Cod setup data
        TextView textViewData = context.findViewById(R.id.textViewDataVol);
        textViewData.setText(vol.getDate().day + "/" +vol.getDate().month + "/" +vol.getDate().year);


        //Design
        final TextView textViewNume = context.findViewById(R.id.large_entry_organizer);
        TextView text_titlu = context.findViewById(R.id.text_titlu);
        TextView text_descriere = context.findViewById(R.id.text_descriere);
        text_titlu.setText(vol.getName());
        text_descriere.setText(vol.getDescription());


        //!!
        ViewPager viewPager = context.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ImageSlideAdapter(context, vol));

        FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(vol.getId_user()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewNume.setText(dataSnapshot.child("lastName").getValue().toString()+" "+ dataSnapshot.child("firstName").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //BUTTON FORMULAR SETUP
        final Button buttonFormular = context.findViewById(R.id.button_formular);
        buttonFormular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(URLUtil.isValidUrl(vol.getLink())) {
                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(vol.getLink()));
                    buttonFormular.getContext().startActivity(viewIntent);
                }
            }
        });

        //BUTTON DE INSCRIERE SETUP
        final Button buttonInscriere = context.findViewById(R.id.button_inscriere);
        TextView textInscriere = context.findViewById(R.id.text_inscriere);
        if(User.currentUser.id.equals(vol.getId_user())) {
            textInscriere.setText("Esti organizatorul acestui voluntariat!");
            buttonInscriere.setVisibility(View.GONE);
        }
        else {
            if (User.currentUser.voluntariate.contains(vol.getId_vol())) {
                textInscriere.setText("Esti inscris la acest voluntariat!");
                buttonInscriere.setVisibility(View.GONE);
                //textInscriere.setVisibility(View.GONE);
            } else {
                if (vol.userList.contains(User.currentUser.id)) {
                    textInscriere.setText("Inscrierea ta este in asteptare...");
                    buttonInscriere.setVisibility(View.GONE);
                }
                else{
                    buttonInscriere.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseHandler.getFirebaseHandler().getReference().child("Voluntariate").child(vol.getId_vol().toString()).child("users").child(User.currentUser.id).setValue(User.currentUser.id);
                            vol.userList.add(User.currentUser.id);
                            Log.d("lolol", "" + vol.getLink().length());
                            if(URLUtil.isValidUrl(vol.getLink())) {
                                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(vol.getLink()));
                                buttonInscriere.getContext().startActivity(viewIntent);
                            }
                        }
                    });
                    textInscriere.setVisibility(View.GONE);
                }
            }
        }
    }
}
