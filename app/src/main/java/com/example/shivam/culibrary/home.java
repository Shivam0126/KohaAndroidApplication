package com.example.shivam.culibrary;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.stringcare.library.SC;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class home extends Fragment  {

    //to specify if this activity is running : notification
    public static boolean isAppRunning;
    ProgressDialog progressBar;
    ViewFlipper flipper1,flipper2;
    String[] book_name=new String[5];
    String[] image_name=new String[5];
    String[] author_name=new String[5];
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    librarypicks lib;
    librarypicksadapter libadapter;
    RecyclerView recyclerView;
    private ImageView[] dots;
    private List<librarypicks> liblist = new ArrayList<>();
    public static final String LIBRARY_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/home.php";
    public static final String Image_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/";
    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home,container,false);


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SC.init(getActivity());
        SC.encryptString(LIBRARY_URL);
        SC.encryptString(Image_URL);
        setRetainInstance(true);
        if (isNetworkAvailable(getActivity())) {


        progressBar = new ProgressDialog(getActivity());

        progressBar.setMessage("Loading ...");
        // progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       /* progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100*/
        progressBar.setCancelable(false);
        progressBar.show();

        recyclerView = (RecyclerView) view.findViewById(R.id.libpickrecyclerview);
        libadapter = new librarypicksadapter(liblist, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(libadapter);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);
        android.support.v4.app.FragmentManager fragmentManager=getFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(),fragmentManager);

        viewPager.setAdapter(viewPagerAdapter);
        // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        // timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
        /*flipper1 = (ViewFlipper)view.findViewById(R.id.flipper1);
        flipper2 = (ViewFlipper)view.findViewById(R.id.flipper2);
        flipper1.startFlipping();
        flipper2.startFlipping();*/


        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String username = sharedpreferences.getString("user", "default");
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, LIBRARY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                   /* String email=data.getString("email");
                   Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();*/

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject v = data.getJSONObject(String.valueOf(i));
                        String Title = v.getString("Title");
                        String image = v.getString("Image");
                        String author = v.getString("author");
                        book_name[i] = Title;
                        image_name[i] = image;
                        author_name[i] = author;
                        lib = new librarypicks(book_name[i], author_name[i], image_name[i]);
                        liblist.add(lib);
                        libadapter.notifyDataSetChanged();

                    }
                  /*  for(int i=0;i<image_name.length;i++)
                    {
                        setFlipperImage(image_name[i]);
                        TextView t1=new TextView(getActivity());
                        t1.setText(book_name[i]);
                        t1.setTextSize(25);
                        t1.setTextColor(Color.WHITE);
                        t1.setGravity(Gravity.CENTER_HORIZONTAL);
                        flipper2.addView(t1);

                        //  This will create dynamic image view and add them to ViewFlipper
                        // setbookname(book_name[i]);
                    }*/
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);
        getActivity().setTitle("Home");

    }else{
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }

    }
   /* public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

         getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

        }
    }*/
   public static boolean isNetworkAvailable(Context context) {
       ConnectivityManager connectivity = (ConnectivityManager) context
               .getSystemService(Context.CONNECTIVITY_SERVICE);
       if (connectivity == null) {
           return false;
       } else {
           NetworkInfo[] info = connectivity.getAllNetworkInfo();
           if (info != null) {
               for (int i = 0; i < info.length; i++) {
                   if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                       return true;
                   }
               }
           }
       }
       return false;
   }
    private void setFlipperImage(String s) {
        String load=Image_URL+s;
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
        ImageView image = new ImageView(getActivity());
        image.setImageBitmap(bmp);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur_position =flipper1.getDisplayedChild();
                Intent k=new Intent(getActivity(),SearchResult.class);
                k.putExtra("bookname",book_name[cur_position]);
                startActivity(k);
            }
        });
        flipper1.addView(image);

    }

   /* private void setbookname(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        TextView t=new TextView(getActivity());
        t.setText(s);
        flipper2.addView(t);
    }*/

    //to see if the activity has been destroyed : notification
  /*    @Override
  public void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }
*/

}