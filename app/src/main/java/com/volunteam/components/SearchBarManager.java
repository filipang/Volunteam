package com.volunteam.components;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SearchView;

import com.volunteam.R;

import java.util.ArrayList;

public class SearchBarManager {

    public SearchBarManager(){

    }

    //This will disable the search bar (The search bar is enabled by default for some reason)
    //Must be called in all Activities that don't require a search bar
    public void disableSearchBar(AppCompatActivity context){
        SearchView searchView = context.findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
    }

    //This will enable the search bar for Recycler views with large entries
    public void createSearchBar(AppCompatActivity context, final RecyclerManager recyclerManager, final ArrayList<Voluntariat> dataSet){

        //SEARCH BAR SETUP
        SearchView searchView = context.findViewById(R.id.search_view);
        searchView.setVisibility(View.VISIBLE);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                recyclerManager.largeRecyclerAdapter.mDataSet = (ArrayList<Voluntariat>) dataSet.clone();
                recyclerManager.largeRecyclerAdapter.notifyDataSetChanged();
                return false;
            }
        });

        //This filters the list when the user confirms a search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //SORT DATA SET
                ArrayList<Voluntariat> filteredList = (ArrayList<Voluntariat>) dataSet.clone();
                if(!query.equals("")) {
                    for (Voluntariat vol : Voluntariat.getDataSet()) {
                        if (!(vol.getDescription().toLowerCase().contains(query.toLowerCase()) || vol.getName().toLowerCase().contains(query))) {
                            filteredList.remove(vol);
                        }
                    }
                }
                recyclerManager.largeRecyclerAdapter.mDataSet = filteredList;
                recyclerManager.largeRecyclerAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
    }

    //This will enable the search bar for Recycler views with small entries
    public void createSearchBar (AppCompatActivity context, final SmallRecyclerAdapter adapter, final ArrayList<Voluntariat> dataSet) {
        SearchView searchView = context.findViewById(R.id.search_view);
        searchView.setVisibility(View.VISIBLE);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                adapter.mDataSet = (ArrayList<Voluntariat>) dataSet.clone();
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        //This filters the list when the user confirms a search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //SORT DATA SET
                ArrayList<Voluntariat> filteredList = (ArrayList<Voluntariat>) dataSet.clone();
                if(!query.equals("")) {
                    for (Voluntariat vol : Voluntariat.getDataSet()) {
                        if (!(vol.getDescription().toLowerCase().contains(query.toLowerCase()) || vol.getName().toLowerCase().contains(query))) {
                            filteredList.remove(vol);
                        }
                    }
                }
                adapter.mDataSet = filteredList;
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
    }
}
