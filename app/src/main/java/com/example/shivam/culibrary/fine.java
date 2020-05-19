package com.example.shivam.culibrary;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

/**
 * Created by shivam on 3/1/18.
 */

public class fine extends Fragment {
    TableLayout fine_table;
   float total=0;
    public static final String FINE_RESULT="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/fine.php";
    ProgressDialog progressBar;
    RecyclerView recyclerView;
    bookfine bfine;
    TextView Outstanding;
    fineadapter fadapter;
    private List<bookfine> finelist = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fine,container,false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("fine");
        //fine_table=(TableLayout)view.findViewById(R.id.fine_table);
        progressBar  = new ProgressDialog(getActivity());

        progressBar.setMessage("Loading ...");
        // progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       /* progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100*/
        progressBar.setCancelable(false);
        progressBar.show();
        Outstanding=(TextView)view.findViewById(R.id.outstanding_amount);
        recyclerView=(RecyclerView)view.findViewById(R.id.finerecycl);
        fadapter=new fineadapter(finelist,getActivity());
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(fadapter);
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");

        StringRequest stringRequest1=new StringRequest(Request.Method.POST,FINE_RESULT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("No fine Found"))
                {
                    Toast.makeText(getActivity(), "No result found", Toast.LENGTH_SHORT).show();
                }
                else {
                    //   Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject data = object.getJSONObject("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject v = data.getJSONObject(String.valueOf(i));
                            String date = v.getString("date");
                            String description = v.getString("description");
                            String fineAmount = v.getString("fineAmount");

                            if(description.equals("not paid"))
                            {
                                total=total+Float.parseFloat(fineAmount);

                            }


                            bfine=new bookfine(date,description,fineAmount);

                            finelist.add(bfine);
                            fadapter.notifyDataSetChanged();
                           /* TableRow row1 = new TableRow(getActivity());
                            TextView date1 = new TextView(getActivity());
                            date1.setText(date);
                            date1.setPadding(20, 10, 0, 0);
                            date1.setTextSize(20);
                            row1.addView(date1);
                            TextView Description = new TextView(getActivity());
                            Description.setText(description);
                            Description.setPadding(20, 10, 0, 0);
                            Description.setTextSize(20);
                            row1.addView(Description);
                            TextView FineAmount = new TextView(getActivity());
                            FineAmount.setPadding(20, 10, 0, 0);
                            FineAmount.setText(fineAmount);
                            FineAmount.setTextSize(20);
                            row1.addView(FineAmount);
                            fine_table.addView(row1);*/
                        }

                        Outstanding.setText("OUTSTANDING AMOUNT   "+String.valueOf(total));
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                Map<String, String> params = new HashMap<>();
                params.put("username", username);

                return params;
            }};
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);
        /*TableRow row1=new TableRow(getActivity());


         TextView date=new TextView(getActivity());
         date.setText("15-5-17");
         row1.addView(date);
         TextView Description=new TextView(getActivity());
         Description.setText("Fine,Kaun hai");
         row1.addView(Description);
         TextView FineAmount=new TextView(getActivity());
         FineAmount.setText("25");
         row1.addView(FineAmount);
         fine_table.addView(row1);*/


    }

}