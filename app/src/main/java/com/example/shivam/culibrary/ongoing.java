package com.example.shivam.culibrary;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

/**
 * Created by gupta on 28-02-2018.
 */

public class ongoing extends Fragment {
    RecyclerView recyclerView;
    public static final String URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/historydue.php";
    due_dateAdapter due_dateadapter;
    books_Due books_due;
    ProgressDialog progressBar;

    private List<books_Due> dueList= new ArrayList<>();
    public ongoing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.ongoing, container, false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar  = new ProgressDialog(getActivity());

        progressBar.setMessage("Loading ...");
        // progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       /* progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100*/
        progressBar.setCancelable(false);
        progressBar.show();
        recyclerView=(RecyclerView) view.findViewById(R.id.duebooks_Recyclerview);
        due_dateadapter=new due_dateAdapter(dueList,getActivity());

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(due_dateadapter);
        dueList.clear();
       // clear();
        prepareduedata();
    }
    private void prepareduedata() {

         SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("user", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject v = data.getJSONObject(String.valueOf(i));
                        String endDate = v.getString("endDate");
                        String Title = v.getString("Title");
                        String Author = v.getString("Author");
                        String Image=v.getString("Image");
                         books_due = new books_Due(Title, Author, endDate,Image);
                        dueList.add(books_due);


                    }
                    due_dateadapter.notifyDataSetChanged();
                    if(progressBar.isShowing())
                    {
                        progressBar.dismiss();
                    }

                 /*   if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                if(progressBar.isShowing())
                {
                    progressBar.dismiss();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    public void clear() {
        int size = dueList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.dueList.remove(i);
            }

            due_dateadapter.notifyDataSetChanged();
        }
    }

    }
