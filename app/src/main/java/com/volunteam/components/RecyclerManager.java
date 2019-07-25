package com.volunteam.components;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.volunteam.R;

import java.util.ArrayList;

public class RecyclerManager {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    public SmallRecyclerAdapter smallRecyclerAdapter;
    LargeRecyclerAdapter largeRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    public void createRecyclerWithLargeElements(final AppCompatActivity context, ArrayList<Voluntariat> dataSet, final SearchBarManager searchBarManager){
        recyclerView = context.findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        largeRecyclerAdapter = new LargeRecyclerAdapter(dataSet);
        final LargeRecyclerAdapter finalLargeRecyclerAdapter = largeRecyclerAdapter;
        recyclerView.setAdapter(largeRecyclerAdapter);

        final RecyclerManager finalThis = this;
        swipeRefreshLayout = context.findViewById(R.id.refreshLayout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d("check", "AAAAAAAAAA");
                    FirebaseHandler.refreshLargeAdapter(largeRecyclerAdapter, swipeRefreshLayout, searchBarManager);
                    largeRecyclerAdapter.notifyDataSetChanged();
                    //searchBarManager.createSearchBar(context, finalThis, (ArrayList<Voluntariat>) finalLargeRecyclerAdapter.mDataSet);
                }

            });
        }
    }

    public void createRecyclerWithSmallElements(final AppCompatActivity context, ArrayList<Voluntariat> dataSet, final SearchBarManager searchBarManager){
        recyclerView = context.findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        smallRecyclerAdapter = new SmallRecyclerAdapter(dataSet);
        final SmallRecyclerAdapter finalSmallRecyclerAdapter = smallRecyclerAdapter;
        recyclerView.setAdapter(smallRecyclerAdapter);

        swipeRefreshLayout = context.findViewById(R.id.refreshLayout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    FirebaseHandler.refreshSmallAdapter(smallRecyclerAdapter, swipeRefreshLayout, searchBarManager);
                    smallRecyclerAdapter.notifyDataSetChanged();
                    //searchBarManager.createSearchBar(context, smallRecyclerAdapter, (ArrayList<Voluntariat>) finalSmallRecyclerAdapter.mDataSet);
                }

            });
        }
    }


}
