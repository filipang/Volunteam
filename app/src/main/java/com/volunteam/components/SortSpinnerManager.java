package com.volunteam.components;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.volunteam.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortSpinnerManager {

    public static final int POPULARITY = 0, ASC_DATE = 1, DESC_DATE = 2;
    private static int currentSort;

    public static int getCurrentSort(){
        return currentSort;
    }

    public static void sortBy(int sort, ArrayList<Voluntariat> list){
        switch (sort) {
            case 0:


                //SORT BY POPULARITY
                Log.d("sort pls", "SORT 0");
                Comparator<Voluntariat> comparator = new Comparator<Voluntariat>() {
                    @Override
                    public int compare(Voluntariat o1, Voluntariat o2) {
                        return o2.userList.size() - o1.userList.size();
                    }
                };
                Collections.sort(list, comparator);
                Collections.sort(Voluntariat.getDataSet(), comparator);
                break;
            case 1:
                //SORT ASCENDING BY DATE
                Log.d("sort pls", "SORT 1");
                Comparator<Voluntariat> comparator_date_cresc = new Comparator<Voluntariat>() {
                    @Override
                    public int compare(Voluntariat o1, Voluntariat o2) {
                        Date d1 = o1.getDate();
                        Date d2 = o2.getDate();
                        Date max = Date.max(d1, d2);
                        if (max == null) {
                            return 0;
                        } else {
                            if (max == d1) {
                                return 1;
                            } else {
                                return -1;
                            }

                        }
                    }
                };
                Collections.sort(list, comparator_date_cresc);
                Collections.sort(Voluntariat.getDataSet(), comparator_date_cresc);
                break;
            case 2:
                //SORT DESCENDING BY DATE
                Log.d("sort pls", "SORT 2");
                Comparator<Voluntariat> comparator_date_descresc = new Comparator<Voluntariat>() {
                    @Override
                    public int compare(Voluntariat o1, Voluntariat o2) {
                        Date d1 = o1.getDate();
                        Date d2 = o2.getDate();
                        Date max = Date.max(d1, d2);
                        if (max == null) {
                            return 0;
                        } else {
                            if (max == d1) {
                                return -1;
                            } else {
                                return 1;
                            }

                        }
                    }
                };
                Collections.sort(list, comparator_date_descresc);
                Collections.sort(Voluntariat.getDataSet(), comparator_date_descresc);
                break;
        }
    }

    public void createSortSpinnerManager(AppCompatActivity context, final RecyclerManager recyclerManager){
        //Spinner setup
        final Spinner spinner = context.findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.main_spinner_list, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("sort pls", "SORT???");
                switch (position){
                    case POPULARITY:

                        //SORT BY POPULARITY
                        sortBy(POPULARITY, recyclerManager.largeRecyclerAdapter.mDataSet);
                        break;
                    case ASC_DATE:

                        //SORT ASCENDING BY DATE
                        sortBy(ASC_DATE, recyclerManager.largeRecyclerAdapter.mDataSet);
                        break;
                    case 2:

                        //SORT DESCENDING BY DATE
                        sortBy(DESC_DATE, recyclerManager.largeRecyclerAdapter.mDataSet);
                        break;
                }
                currentSort = position;
                recyclerManager.largeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
