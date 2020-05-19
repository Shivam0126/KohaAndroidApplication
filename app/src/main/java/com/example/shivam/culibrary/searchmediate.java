package com.example.shivam.culibrary;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shivam on 11/2/18.
 */

public class searchmediate extends Fragment{
ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> output=new ArrayList<>();

    String[] title=new String[]{};
    String[] author=new String[]{};
    int size;
    int noresult;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       title = getArguments().getStringArray("title");
       author=getArguments().getStringArray("author");
       size=getArguments().getInt("size");
       noresult=getArguments().getInt("noresult");
        return inflater.inflate(R.layout.searchmediate,container,false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        output.clear();
        listView=(ListView)view.findViewById(R.id.listview1);
        arrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, output);
        listView.setAdapter(arrayAdapter);
        if(noresult==1)
        {

            output.add("No result Found");
            arrayAdapter.notifyDataSetChanged();
        }
        else {
            for (int i = 0; i < size; i++) {
                output.add(title[i] + "\n" + author[i]);


            }


            arrayAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent i = new Intent(getActivity(), SearchResult.class);
                    i.putExtra("bookname", title[position]);
                    startActivity(i);
                }
            });
        }

    }


}
