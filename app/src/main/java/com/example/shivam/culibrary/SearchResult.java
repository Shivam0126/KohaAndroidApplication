package com.example.shivam.culibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;


public class SearchResult extends AppCompatActivity {
    public static final String SEARCH_RESULT="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/searchresult.php";
    ImageView i;
    location  loca;
    RecyclerView recyclerView;
    TextView title,author,publisheras,isbn;
    private List<location> locationList = new ArrayList<>();
    ProgressDialog progressBar;
    locationAdapter locationadapter;
    ImageView imageView;
    Button button;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    int progressStatus;
    String ISBN,username;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        progressBar  = new ProgressDialog(this);

        progressBar.setMessage("Loading ...");
        progressBar.setCancelable(false);
        progressBar.show();
        i=(ImageView)findViewById(R.id.book_image);
        title=(TextView)findViewById(R.id.title);
        author=(TextView)findViewById(R.id.author);
        publisheras=(TextView)findViewById(R.id.publisher);
        isbn=(TextView)findViewById(R.id.isb);
        imageView=(ImageView)findViewById(R.id.book_image);
        recyclerView=(RecyclerView)findViewById(R.id.location_recyclerview);
        locationadapter=new locationAdapter(locationList,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(locationadapter);

        /*button=(Button)findViewById(R.id.confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareLocationData();
            }
        });

        i.setImageResource(R.drawable.angelsndemons);*/
        clear();
        Intent i=getIntent();
        String bookname=i.getStringExtra("bookname");
        prepareLocationData(bookname);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);

    }

    private void prepareLocationData(final String bookname) {


        final SharedPreferences sharedPreferences=this.getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username=sharedPreferences.getString("user","");
        recyclerView.getRecycledViewPool().clear();
        StringRequest stringRequest1=new StringRequest(Request.Method.POST,SEARCH_RESULT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    String Title=data.getString("Title");
                    String image=data.getString("Image");
                    String publisher=data.getString("Publisher");
                    String Author=data.getString("Author");
                    String ISBN=data.getString("ISBN");
                    String load="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/"+image;
                    progressStatus=50;
                    progressBar.setProgress(progressStatus);
                  /*  FileInputStream fs = null;
                    try {
                        fs = new FileInputStream(new File(load));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

// I am using the commons library (https://commons.apache.org/proper/commons-io/)
                    byte[] image1 = new byte[0];
                    try {
                        image1 = IOUtils.toByteArray(fs);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image1, 0, image1.length);
                    imageView.setImageBitmap(bitmap);*/
                    URL url = new URL(load);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    imageView.setImageBitmap(bmp);
                    // imageView.setImageResource(R.drawable.noimage);

                    title.setText(Title);
                    publisheras.setText(publisher);
                    author.setText(Author);
                    isbn.setText(ISBN);
                    progressStatus=75;
                    progressBar.setProgress(progressStatus);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("ISBN", ISBN);
                    editor.commit();
                    if(progressBar.isShowing())
                    {
                        progressBar.dismiss();
                    }

                    for(int i=0;i<data.length();i++)
                    {
                        JSONObject v=data.getJSONObject(String.valueOf(i));
                        String Libraryname=v.getString("LibraryName");
                        String Rack=v.getString("Rack");
                        String Status=v.getString("Status");
                        if(Status.equals("0"))
                        {
                            Status="Available";
                        }else if(Status.equals("1"))
                        {
                            Status="Unavailable";
                        }
                        loca=new location(Libraryname,Rack,Status);
                        locationList.add(loca);
                        locationadapter.notifyDataSetChanged();
                    }
                  /*  progressStatus=100;
                    progressBar.setProgress(progressStatus);
                    if (progressStatus == 100) {
                        // sleeping for 1 second after operation completed
                        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                        // close the progress bar dialog
                        progressBar.dismiss();
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
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
                params.put("title",bookname);
                return params;
            }};
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
      /*  location loca=new location("Main Campus","Rack-25","Available");
        locationList.add(loca);
        loca=new location("BGR campus","Rack-28","Unavailable");
        locationList.add(loca);
        loca=new location("Kengeri campus","Rack-42","Available");
        locationList.add(loca);
        loca=new location("BGR campus","Rack-28","Unavailable");
        locationList.add(loca);
        loca=new location("Kengeri campus","Rack-42","Available");
        locationList.add(loca);
        loca=new location("BGR campus","Rack-28","Unavailable");
        locationList.add(loca);
        locationadapter.notifyDataSetChanged();*/
    }
    public void clear() {
        int size = this.locationList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.locationList.remove(i);
            }

            locationadapter.notifyDataSetChanged();
        }
    }
    public String add_ISBN()
    {
        String passISBN=ISBN;
        return passISBN;
    }
    public String add_user()
    {

        return username;
    }
    public Context contextsend()
    {

        return ctx;
    }

}
