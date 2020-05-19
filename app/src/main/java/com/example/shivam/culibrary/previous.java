package com.example.shivam.culibrary;


import android.app.ProgressDialog;
import android.content.Context;
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

public class previous extends Fragment {
    RecyclerView recyclerView2;
    books_issued books_issue;
    ProgressDialog progressBar;
    issuedonAdapter issuedonadapter;
    private List<books_issued> issueList= new ArrayList<>();
    public static final String URL2="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/historyissue.php";
    public previous() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.previous, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar  = new ProgressDialog(getActivity());

        progressBar.setMessage("Loading ...");
        // progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       /* progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100*/
        progressBar.setCancelable(false);
        progressBar.show();
        recyclerView2=(RecyclerView)view.findViewById(R.id.issued_recyclerView);
        issuedonadapter=new issuedonAdapter(issueList,getActivity());
        RecyclerView.LayoutManager nlayoutManager=new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(nlayoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(issuedonadapter);
        issueList.clear();
       // clear();
        prepareissuedata();
    }

    private void prepareissuedata() {
       SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");
        StringRequest stringRequest1=new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    for(int i=0;i<data.length();i++)
                    {
                        JSONObject v=data.getJSONObject(String.valueOf(i));
                        String startDate=v.getString("startDate");
                        String Title=v.getString("Title");
                        String Author=v.getString("Author");
                        books_issue=new books_issued(Title,Author,startDate);
                        issueList.add(books_issue);
                        issuedonadapter.notifyDataSetChanged();

                    }
                    if(progressBar.isShowing())
                    {
                        progressBar.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                if(progressBar.isShowing())
                {
                    progressBar.dismiss();
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);

    }
    public void clear() {
        int size = issueList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.issueList.remove(i);
            }

            issuedonadapter.notifyDataSetChanged();
        }
    }
}
