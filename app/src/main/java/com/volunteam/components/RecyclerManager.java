package com.volunteam.components;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.volunteam.R;

public class RecyclerManager {
    RecyclerView recyclerView;
    LargeRecyclerAdapter largeRecyclerAdapter; //Same as mAdapter
    RecyclerView.LayoutManager layoutManager;

    public void createRecyclerView(AppCompatActivity context){
        recyclerView = (RecyclerView) context.findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        largeRecyclerAdapter = new LargeRecyclerAdapter(Voluntariat.getDataSet());
        recyclerView.setAdapter(largeRecyclerAdapter);
    }

}
