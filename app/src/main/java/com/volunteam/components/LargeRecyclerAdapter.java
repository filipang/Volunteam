package com.volunteam.components;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volunteam.R;
import com.volunteam.activities.VoluntariatActivity;

import android.os.Handler;

import java.util.ArrayList;

public class LargeRecyclerAdapter extends RecyclerView.Adapter<LargeRecyclerAdapter.MyViewHolder> {

    public ArrayList<Voluntariat> mDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        public MyViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }

        public void setLayout(LinearLayout layout) {
            this.layout = layout;
        }
    }

    public LargeRecyclerAdapter(ArrayList<Voluntariat> mDataSet){
        this.mDataSet = (ArrayList<Voluntariat>) mDataSet.clone();
    }

    public void update(){
        this.mDataSet = (ArrayList<Voluntariat>) Voluntariat.getDataSet().clone();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LargeRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Log.d("error", "(onCreate)The parent has " + parent.getChildCount() + " children!");
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.vol_entry, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final MyViewHolder fholder = holder;
        final int fposition = position;
        final Handler handler = new Handler(Looper.getMainLooper());

        final LinearLayout element = fholder.layout;
        final Voluntariat vol = mDataSet.get(fposition);

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VoluntariatActivity.class);
                intent.putExtra("voluntariat", vol.getId_vol());
                v.getContext().startActivity(intent);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {

                mDataSet.get(fposition).setDrawable(Voluntariat.loadDrawableFromURL(mDataSet.get(fposition).getImageURL()));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    try {
                        TextView textView = (TextView) element.findViewById(R.id.large_entry_description);
                        textView.setText(vol.getDescription());

                        TextView textView5 = (TextView) element.findViewById(R.id.large_entry_counter);
                        textView5.setText(FirebaseHandler.getFirebaseHandler().getData().child("Users").child(vol.getId_user()).child("organizedVolCount").getValue().toString() + " voluntariate");

                        TextView textView3 = (TextView) element.findViewById(R.id.large_entry_organizer);
                        textView3.setText("XDDD");
                        textView3.setText(FirebaseHandler.getFirebaseHandler().getData().child("Users").child(vol.getId_user()).child("firstName").getValue().toString() + " " + FirebaseHandler.getFirebaseHandler().getData().child("Users").child(vol.getId_user()).child("lastName").getValue().toString());

                        ImageView img1 = (ImageView) element.findViewById(R.id.large_entry_image);
                        img1.setImageDrawable(vol.getDrawable());
                        img1.setOnClickListener(listener);

                        ImageView img2 = (ImageView) element.findViewById(R.id.large_entry_profile_pic);
                        img2.setImageResource(R.drawable.ic_profile);

                        TextView textView1 = (TextView) element.findViewById(R.id.large_entry_name);
                        textView1.setText(vol.getName());
                        textView1.setOnClickListener(listener);

                        TextView textView2 = (TextView) element.findViewById(R.id.large_entry_date);
                        Date date = vol.getDate();
                        textView2.setText("" + date.day + "/" + date.month + "/" + date.year);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("error", "Failed at " + fposition + ", and the size is " + mDataSet.size());
                    }
                }
            });
            }

        }).start();


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }




}
