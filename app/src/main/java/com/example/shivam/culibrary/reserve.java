package com.example.shivam.culibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

/**
 * Created by gupta on 28-02-2018.
 */

public class reserve extends Fragment {
    public static final String RESERVELIST="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/reservelist.php";
    public static final String CHECKOUT_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/checkout.php";
    TextView title_textview,author_textview;
    Button checkout;
    String title,author;

    public reserve() {
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
        return inflater.inflate(R.layout.reserve, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title_textview=(TextView)view.findViewById(R.id.titles);
        author_textview=(TextView)view.findViewById(R.id.author);
      /*  checkout=(Button)view.findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(title);
            }
        });*/
        preparedata();
    }

    private void send(final String title) {

        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");

        StringRequest stringRequest1=new StringRequest(Request.Method.POST,CHECKOUT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("title",title);

                return params;
            }};
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);

    }

    private void preparedata() {

        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");

        StringRequest stringRequest1=new StringRequest(Request.Method.POST,RESERVELIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                if (response.equals("0")) {
                    title_textview.setText("NO BOOK RESERVED");
                    author_textview.setText("");
                    //checkout.setVisibility(View.GONE);
                } else {

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject data = object.getJSONObject("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject v = data.getJSONObject(String.valueOf(i));
                            title = v.getString("title");
                             author = v.getString("author");
                            title_textview.setText(title);
                            author_textview.setText(author);

                        }
                        //checkout.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);

                return params;
            }};
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);

    }

}
