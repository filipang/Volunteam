package com.volunteam.components;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;

import java.util.ArrayList;

public class AttendantTableManager {

    public void createAttendantTableManager(AppCompatActivity context, final Voluntariat vol){
        final TableLayout table = context.findViewById(R.id.table);
        //TABLE SETUP
        Log.d("std",User.currentUser.id );
        Log.d("std", vol.getId_user());
        if(!User.currentUser.id.equals(vol.getId_user())) {
            table.setVisibility(View.GONE);
        }
        else{
            for (final String user_id : vol.userList) {
                final TableRow row = (TableRow) context.getLayoutInflater().inflate(R.layout.table_row_layout, table, false);
                FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> list = new ArrayList<String>();
                        for (DataSnapshot ds : dataSnapshot.child("voluntariate").getChildren()){
                            list.add(ds.getValue().toString());
                        }
                        if (list.contains(user_id)) {
                            row.removeViewAt(4);
                            row.removeViewAt(3);
                        } else {
                            TextView textFirst = (TextView) row.getChildAt(0);
                            TextView textLast = (TextView) row.getChildAt(1);
                            TextView textEmail = (TextView) row.getChildAt(2);
                            TextView textAcc = (TextView) row.getChildAt(3);
                            TextView textRef = (TextView) row.getChildAt(4);
                            textFirst.setText(dataSnapshot.child("firstName").getValue().toString());
                            textLast.setText(dataSnapshot.child("lastName").getValue().toString());
                            textEmail.setText(dataSnapshot.child("email").getValue().toString());
                            table.removeView(row);
                            table.addView(row,0);

                            View.OnClickListener listener_acc = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(user_id).child("voluntariate").child(vol.getId_vol().toString()).setValue(vol.getId_vol().toString());
                                    //ALSO ADD TO DATABASE
                                    row.removeViewAt(4);
                                    row.removeViewAt(3);
                                }
                            };

                            View.OnClickListener listener_ref = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    vol.userList.remove(user_id);
                                    FirebaseHandler.getFirebaseHandler().getReference().child("Users").child(user_id).child("voluntariate").child(vol.getIdVol().toString()).child(user_id).removeValue();
                                    FirebaseHandler.getFirebaseHandler().getReference().child("Voluntariate").child(vol.getIdVol()+"").child("users").child(user_id).removeValue();
                                    //ALSO REMOVE FROM DATABASE
                                    row.removeViewAt(4);
                                    row.removeViewAt(3);
                                }
                            };

                            textAcc.setOnClickListener(listener_acc);
                            textRef.setOnClickListener(listener_ref);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
    }
}
