package com.example.shivam.culibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

public class waiting extends Fragment {
    RecyclerView recyclerView;
    waiting_dueAdapter waitingDueAdapter;
    waiting_due waitingDue;
    public static final String URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/waitinglist.php";
    private List<waiting_due> waitingList= new ArrayList<>();
    public waiting() {
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
        return inflater.inflate(R.layout.waiting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=(RecyclerView) view.findViewById(R.id.waitingdue_Recyclerview);
        waitingDueAdapter=new waiting_dueAdapter(waitingList,getActivity());
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(waitingDueAdapter);
        //clear();
        waitingList.clear();
        prepareduedata();
    }

    private void prepareduedata() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("user", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject v = data.getJSONObject(String.valueOf(i));
                        String dueDate = v.getString("reservedate");
                        String Title = v.getString("Title");
                        String Author = v.getString("Author");
                        waitingDue = new waiting_due(Title, Author, dueDate);
                        waitingList.add(waitingDue);



                    }
                    waitingDueAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();

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
        int size = waitingList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.waitingList.remove(i);
            }

            waitingDueAdapter.notifyDataSetChanged();
        }
    }
}
