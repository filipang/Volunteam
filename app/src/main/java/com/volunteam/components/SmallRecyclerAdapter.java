package com.volunteam.components;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volunteam.R;
import com.volunteam.activities.VoluntariatActivity;

import android.os.Handler;

import java.util.List;

public class SmallRecyclerAdapter extends RecyclerView.Adapter<SmallRecyclerAdapter.MyViewHolder> {

    public List<Voluntariat> mDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        public MyViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public SmallRecyclerAdapter(List<Voluntariat> mDataSet){
        this.mDataSet = mDataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SmallRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.vol_entry_small, parent, false);
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
                                intent.putExtra("voluntariat", vol.getId_vol());
                                v.getContext().startActivity(intent);
                            }
                        };

                        TextView textView = (TextView) ((ViewGroup)((ViewGroup)element.getChildAt(0)).getChildAt(1)).getChildAt(0);
                        textView.setText(vol.getName());
                        textView.setOnClickListener(listener);

                        TextView textViewOrganiser = (TextView) ((ViewGroup)((ViewGroup)element.getChildAt(0)).getChildAt(1)).getChildAt(1);
                        //textViewOrganiser.setText(vol.getName());//TEXT WILL BE THE NAME OF THE USER THAT POSTED THIS
                        textViewOrganiser.setOnClickListener(listener);

                        ImageView img = (ImageView) ((ViewGroup)element.getChildAt(0)).getChildAt(0);
                        img.setImageDrawable(vol.getDrawable());
                        img.setOnClickListener(listener);
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
