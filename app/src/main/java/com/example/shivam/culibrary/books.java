package com.example.shivam.culibrary;



import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;


public class books extends Fragment {
    RecyclerView recyclerView,recyclerView2_issue;
    books_issued books_issue;
    ProgressDialog progressBar;
    books_Due books_due;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/historydue.php";
    public static final String URL2="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/historyissue.php";
    SharedPreferences sharedPreferences;
    private List<books_Due> dueList= new ArrayList<>();
    private List<books_issued> issueList= new ArrayList<>();
    due_dateAdapter due_dateadapter;
    issuedonAdapter issuedonadapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books,container,false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Books");
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
       /* recyclerView=(RecyclerView) view.findViewById(R.id.duebooks_Recyclerview);
        due_dateadapter=new due_dateAdapter(dueList,getActivity());
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(due_dateadapter);
        recyclerView2_issue=(RecyclerView)view.findViewById(R.id.issued_recyclerView);
        issuedonadapter=new issuedonAdapter(issueList,getActivity());
        RecyclerView.LayoutManager nlayoutManager=new LinearLayoutManager(getActivity());
        recyclerView2_issue.setLayoutManager(nlayoutManager);
        recyclerView2_issue.setItemAnimator(new DefaultItemAnimator());
        recyclerView2_issue.setAdapter(issuedonadapter);*/
      /*  progressBar  = new ProgressDialog(getActivity());

        progressBar.setMessage("Loading ...");

        progressBar.setCancelable(false);
        progressBar.show();*/
      // prepareduedateData();



    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new ongoing(), "Ongoing");
        adapter.addFragment(new previous(), "Previous");
        adapter.addFragment(new waiting(), "Waiting");
        adapter.addFragment(new reserve(), "Reserve");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private void prepareduedateData() {

        sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    for(int i=0;i<data.length();i++)
                    {
                        JSONObject v=data.getJSONObject(String.valueOf(i));
                        String endDate=v.getString("endDate");
                        String Title=v.getString("Title");
                        String Author=v.getString("Author");
                        String Image=v.getString("Image");
                        books_due=new books_Due(Title,Author,endDate, Image);
                        dueList.add(books_due);


                    }
                    due_dateadapter.notifyDataSetChanged();
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
//for booksissued

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

                } catch (JSONException e) {
                    e.printStackTrace();
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
                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);

    }

}