package com.example.shivam.culibrary;



import android.Manifest;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.provider.Settings;

import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean isAppRunning;

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static final String SEARCH_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/Search.php";
    public static final String SEARCHHINT_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/searchhint.php";
    String filtervalue;
    SharedPreferences sharedPreferences;
    String title1[]=new String[100];
    String author1[]=new String[100];
    String[] filters={"ISBN","Title","Author"};
    MenuItem spinnerItem;
    SearchView searchView;
    int type;
   ArrayAdapter<String> arrayAdapter ;
   ArrayAdapter<String> arrayAdapter2;
    String m;
    Spinner spinner;
    ListView listview;
    String[] listItem=new String[]{};
    TextView result;
    LinearLayout linearLayout;
    ArrayList<String> output=new ArrayList<>();
    ListView changelistview;

     List<String> new_list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment=new Fragment();
        fragment=new home();
        android.support.v4.app.FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction().replace(R.id.content_frame, fragment).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        FirebaseMessaging.getInstance().subscribeToTopic("holidays");

        SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String user_login=sharedPreferences.getString("user","");






    }






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            { final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
                popDialog.setMessage("Are you sure you want to Exit?");
                popDialog.setTitle("Exit");

                popDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                              //  System.exit(1);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
      spinnerItem = menu.findItem(R.id.filter);

        View view = spinnerItem.getActionView();
        // ...
        if(view instanceof Spinner) {
             spinner = (Spinner) view;
            // filter adapter
            ArrayAdapter<String> filteradapter=new ArrayAdapter<String>(this,R.layout.spinner_item,filters);

            spinner.setAdapter(filteradapter);
            spinner.getResources().getColor(R.color.white);


          //  type=Integer.parseInt(spinner.getSelectedItem().toString());

        }

        MenuItem search_item = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {


                //checking language arraylist for items containing search keyword
               // String search_value= (String) search_item.getQuery();
                m=spinner.getSelectedItem().toString();
                if(m.equals("ISBN"))
                {
                    type=0;

                }
                else if(m.equals("Title"))
                {
                    type=1;
                }
                else if(m.equals("Author"))
                {
                    type=2;
                }

                //search_request(s,type);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                m=spinner.getSelectedItem().toString();
                if(m.equals("ISBN"))
                {
                    type=0;

                }
                else if(m.equals("Title"))
                {
                    type=1;
                }
                else if(m.equals("Author"))
                {
                    type=2;
                }
                if(s.equals(""))
                {  home h=new home();
                    //Toast.makeText(getApplicationContext(),"NO result",Toast.LENGTH_LONG).show();
                    android.support.v4.app.FragmentManager ft = getSupportFragmentManager();
                    ft.beginTransaction().replace(R.id.content_frame, h).commit();
                }
                else{

                search_request(s,type);}
                return false;
            }
        });







        return true;
    }

    private void searchchange(String s, final int type) {
        final String searchvalue=s;
        int Type=type;
        sharedPreferences=this.getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");

        StringRequest stringRequest=new StringRequest(Request.Method.POST, SEARCHHINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                String Title_change[] = new String[100];
                String author_change[]=new String[100];
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

                changelistview.setAdapter(arrayAdapter);
                int size=0;
                //  if(response==null) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject v = data.getJSONObject(String.valueOf(i));

                         title1[i] = v.getString("Title");
                         author1[i] = v.getString("Author");

                         output.add(title1[i]+"\n"+author1[i]);
                        arrayAdapter2.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // }else{
                //     Toast.makeText(MainActivity.this, "INVALID INPUT", Toast.LENGTH_SHORT).show();
                // }import android.support.v7.widget.Toolbar;
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                params.put("Type", String.valueOf(type));
                params.put("searchvalue",searchvalue);
                return params;
            }
        }
        ;
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int x=0;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
    /*    Toast.makeText(this,"------------------"+ id, Toast.LENGTH_SHORT).show();
        switch(id)
        {
            case R.id.filter:
                Toast.makeText(this, "I am there in filters", Toast.LENGTH_SHORT).show();
                filtervalue = spinnerItem.getTitle().toString();
                x=x+1;
                Toast.makeText(this, filtervalue, Toast.LENGTH_SHORT).show();
                Log.e("ERORRRRRRRRRRR__",filtervalue);
                break;
            case R.id.search:
                Toast.makeText(this, "I am in search", Toast.LENGTH_SHORT).show();
                if(x>0) {
                    if (filtervalue.equals("ISBN")) {
                        type = 0;
                    } else if (filtervalue.equals("Title")) {
                        type = 1;
                    } else if (filtervalue.equals("Author")) {
                        type = 2;
                    }
                    Toast.makeText(this, type+"-"+filtervalue, Toast.LENGTH_SHORT).show();
                }
                else
                { filtervalue="Title";
                    type=1;
                }
                String search_value= (String) searchView.getQuery();
                Toast.makeText(this, search_value, Toast.LENGTH_SHORT).show();
                search_request(search_value,type);
                break;
            default:
                Toast.makeText(this, "Choose the filter", Toast.LENGTH_SHORT).show();


        }*/

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private void search_request(String filtervalue, final int type) {
        sharedPreferences=this.getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");
        final String searchvalue=filtervalue;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, SEARCHHINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String TITLE[] = new String[100];
                String author[]=new String[100];
                String errorResponse=response.substring(0,15);

                    int size=0;
                    if(errorResponse.equals("No result found"))
                    {


                        searchmediate searchm=new searchmediate();
                        Bundle bundle = new Bundle();
                        bundle.putInt("noresult", 1);
                        searchm.setArguments(bundle);
                        android.support.v4.app.FragmentManager ft = getSupportFragmentManager();
                        ft.beginTransaction().replace(R.id.content_frame, searchm).commit();
                    }else {
                        //  if(response==null) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = object.getJSONObject("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject v = data.getJSONObject(String.valueOf(i));
                                String title = v.getString("Title");
                                String Author = v.getString("Author");

                                TITLE[i] = title;
                                author[i] = Author;
                                size = size + 1;
                                //result.setText(title+"\n"+Author);
                         /*   TextView textView=new TextView(getApplicationContext());
                            textView.setText(title+"\n"+Author);
                            linearLayout.addView(textView);*/
                                // Toast.makeText(MainActivity.this, title + ":" + Author, Toast.LENGTH_SHORT).show();

                            }
                            //Toast.makeText(MainActivity.this, TITLE + ":" + author, Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("title", TITLE);
                            bundle.putStringArray("author", author);
                            bundle.putInt("size", size);
                            bundle.putInt("noresult",0);
// set Fragmentclass Arguments
                            searchmediate searchm = new searchmediate();
                       /* Fragment fragment=new Fragment();
                        fragment=searchm;*/
                            searchm.setArguments(bundle);
                            android.support.v4.app.FragmentManager ft = getSupportFragmentManager();
                            ft.beginTransaction().replace(R.id.content_frame, searchm).commit();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
               // }else{
               //     Toast.makeText(MainActivity.this, "INVALID INPUT", Toast.LENGTH_SHORT).show();
               // }import android.support.v7.widget.Toolbar;
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                params.put("Type", String.valueOf(type));
                params.put("searchvalue",searchvalue);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        isAppRunning=false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_profile:
                fragment = new profile();
                break;
            case R.id.nav_home:
                fragment = new home();
                break;
            case R.id.nav_issue:
                fragment = new issue();
                break;
            case R.id.nav_books:
                fragment = new books();
                break;
            case R.id.nav_fine:
                fragment = new fine();
                break;
            case R.id.nav_support:
                fragment = new support();
                break;
            case R.id.nav_suggestbook:
                fragment = new suggest_books();
                break;
            case R.id.nav_logout:
                fragment = new logout();
                break;
            default:  fragment = new home();
                break;
        }

        //replacing the fragment
        if (fragment != null) {

            android.support.v4.app.FragmentManager ft = getSupportFragmentManager();
            ft.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }



}