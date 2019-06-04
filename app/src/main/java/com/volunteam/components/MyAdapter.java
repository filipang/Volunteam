package com.volunteam.components;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.volunteam.R;
import com.volunteam.activities.VoluntariatActivity;

import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Voluntariat> mDataSet;

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

    public MyAdapter(ArrayList<Voluntariat> mDataSet){
        this.mDataSet = (ArrayList<Voluntariat>) mDataSet.clone();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Log.d("error", "(onCreate)The parent has " + parent.getChildCount() + " children!");
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.vol_entry, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final MyViewHolder fholder = holder;
        final int fposition = position;
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {

                mDataSet.get(fposition).setDrawable(Voluntariat.loadDrawableFromURL(mDataSet.get(fposition).getImageURL()));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout element = fholder.layout;
                        final Voluntariat vol = mDataSet.get(fposition);

                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), VoluntariatActivity.class);
                                intent.putExtra("voluntariat", vol);
                                v.getContext().startActivity(intent);
                            }
                        };
                        TextView textView = (TextView) element.getChildAt(1);
                        try {
                            textView.setText(vol.getDescription());
                            ImageView img = (ImageView) element.getChildAt(2);
                            img.setImageDrawable(vol.getDrawable());
                            img.setOnClickListener(listener);

                            TextView textView1 = (TextView) element.getChildAt(3);
                            textView1.setText(vol.getName());
                            textView1.setOnClickListener(listener);

                            ImageView img1 = (ImageView) ((ViewGroup)element.getChildAt(0)).getChildAt(0);
                            img1.setImageDrawable(vol.getDrawable());
                        }catch (Exception e){
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