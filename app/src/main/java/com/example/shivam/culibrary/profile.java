package com.example.shivam.culibrary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

/**
 * Created by shivam on 3/1/18.
 */

public class profile extends Fragment {
    public static final String Image_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/";
    ImageView imageView;
    Button edit;
    public static final String PROFILE_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/profile.php";
    public static final String PHONE_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/phonechange.php";
    ProgressDialog progressBar;

    TextView name,email_textview,phone_no;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
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
        imageView=(ImageView)view.findViewById(R.id.profileimage);
        edit=(Button)view.findViewById(R.id.editbutton);
        name=(TextView)view.findViewById(R.id.name);
        email_textview=(TextView)view.findViewById(R.id.email);
        phone_no=(TextView)view.findViewById(R.id.phone);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());

                View mView = getLayoutInflater().inflate(R.layout.editlayout, null);
                popDialog.setView(mView);
                final EditText editText=(EditText)mView.findViewById(R.id.ed1);

                popDialog.setTitle("Enter new Phone number");

                popDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String phonenumber=editText.getText().toString();
                               // Toast.makeText(getActivity(), phonenumber, Toast.LENGTH_SHORT).show();
                                confirm(phonenumber);


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog alertDialog = popDialog.create();
                alertDialog.show();


            }
        });
        getActivity().setTitle("Profile");
        adddata();


    }

    private void confirm(final String phonenumber) {
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, PHONE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
              if(response.equals("success"))
              {
                  Toast.makeText(getActivity(), "Successfully number changed", Toast.LENGTH_SHORT).show();

              }
              else{
                  Toast.makeText(getActivity(), "Technical issue ,Try again later!", Toast.LENGTH_SHORT).show();
              }




            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                params.put("phonenumber",phonenumber);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void adddata() {
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    for(int i=0;i<data.length();i++)
                    {
                        JSONObject v=data.getJSONObject(String.valueOf(i));
                        String firstname=v.getString("firstname");
                        String surname=v.getString("surname");
                        String email=v.getString("email");
                        String phone=v.getString("phone");
                        String image=v.getString("Image");
                        name.setText(firstname+" "+surname);
                        email_textview.setText(email);
                        phone_no.setText(phone);
                        String load=Image_URL+image;
                        URL url = null;
                        try {
                            url = new URL(load);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        Bitmap bmp = null;
                        try {
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageView.setImageBitmap(bmp);



                    }
                    if(progressBar.isShowing())
                    {
                        progressBar.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }
                , new Response.ErrorListener() {
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}