package com.example.shivam.culibrary;

import android.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.shivam.culibrary.notifications.MyFirebaseInstanceIDService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;


public class LoginActivity extends AppCompatActivity {

    EditText username;
    String refreshedToken;
    LinearLayout login;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String COMPLETED_ONBOARDING_PREF_NAME="onboarding";
    public static final String user = "user" ;
    public static final String pass = "pass" ;
    String token;
    Button loginbtn;
    ImageView logo;
    ProgressDialog progressBar;
    EditText password;
    public static final String URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/login.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
        checkPermission();

        username=(EditText) findViewById(R.id.user);
        password=(EditText) findViewById(R.id.pass);
        login=(LinearLayout)findViewById(R.id.loginAct);
        login.getBackground().setAlpha(100);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        logo=(ImageView)findViewById(R.id.christlogo);
            SharedPreferences.Editor sharedPreferencesEditor =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            sharedPreferencesEditor.putBoolean(
                    COMPLETED_ONBOARDING_PREF_NAME, true);
            sharedPreferencesEditor.apply();
         refreshedToken= FirebaseInstanceId.getInstance().getToken();


          /*  TranslateAnimation slide = new TranslateAnimation(0, 0, 0,-300 );
            slide.setDuration(1000);

            slide.setFillAfter(true);
            username.setAnimation(slide);
            password.setAnimation(slide);
            loginbtn.setAnimation(slide);
            TranslateAnimation slide2 = new TranslateAnimation(0, -150, 0,-100 );
            slide2.setDuration(1000);
            slide2.setFillAfter(true);
            //  RotateAnimation slide3=new RotateAnimation(0,360,0,0);
            //  slide3.setDuration(1000);
            logo.setAnimation(slide2);*/







    }
    @Override
    public void onBackPressed() {

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getApplicationContext());
        // popDialog.setView(inflater.inflate(R.layout.rat,null));
        popDialog.setMessage("Are you sure you want to Exit?");
        popDialog.setTitle("EXIT");

        popDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = popDialog.create();
        alertDialog.show();

    }

    @Override
    protected void onDestroy() {
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void login(View view) {


        final String usern=username.getText().toString();
        final String passw=password.getText().toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.contains("Success"))
                {  sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(user, usern);
                    editor.putString(pass, passw);
                    editor.putBoolean("userloggedin",true);
                    editor.commit();
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                   // MyFirebaseInstanceIDService fb=new MyFirebaseInstanceIDService();
                   // fb.onTokenRefresh();
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
  Toast.makeText(getApplicationContext(),"ERROR ",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("username",usern);
                params.put("password",passw);
                params.put("Token",refreshedToken);
                return params;
            }
        };
      /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
  /*  public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

        private static final String TAG = "MyFirebaseIIDService";


        @Override
        public void onTokenRefresh() {
            // Get updated InstanceID token.
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            SharedPreferences   sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Token",refreshedToken);
            editor.commit();

        }
    }*/
}

