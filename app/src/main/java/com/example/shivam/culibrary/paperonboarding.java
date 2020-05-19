package com.example.shivam.culibrary;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by gupta on 14-03-2018.
 */

public class paperonboarding extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    ImageButton mNextBtn;
    Button mSkipBtn, mFinishBtn;
    String[] titleof=new String[]{"Search ","Reserve","Notification"};
    String[] contentof=new String[]{"Search through Catalog","Reserve book prior to your visit","Get notified"
    };
    static TextView content;
    ImageView zero, one, two;
    ImageView[] indicators;
    static TextView textView;

    int lastLeftValue = 0;

    CoordinatorLayout mCoordinator;


    static final String TAG = "PagerActivity";

    int page = 0;   //  to track page position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.button));
        }

        setContentView(R.layout.paperonboarding);




        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Button loginbutton;



        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.relative);


        indicators = new ImageView[]{zero, one, two};

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);
        loginbutton=(Button) findViewById(R.id.loginbutton);
        loginbutton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
        final int color1 = ContextCompat.getColor(this, R.color.button);
        final int color2 = ContextCompat.getColor(this, R.color.paper2);
        final int color3 = ContextCompat.getColor(this, R.color.paper3);

        final int[] colorList = new int[]{color1, color2, color3};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 2 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
                switch (position) {
                    case 0:


                        textView.setText(titleof[position]);
                        content.setText(contentof[position]);
                        break;
                    case 1:

                        textView.setText(titleof[position]);
                        content.setText(contentof[position]);
                        break;
                    case 2:

                        textView.setText(titleof[position]);
                        content.setText(contentof[position]);
                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators(page);

                switch (position) {
                    case 0:

                        mViewPager.setBackgroundColor(color1);
                        textView.setText(titleof[position]);
                        content.setText(contentof[position]);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        textView.setText(titleof[position]);
                        content.setText(contentof[position]);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        textView.setText(titleof[position]);
                        content.setText(contentof[position]);
                        break;
                }





            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.nonactive_dot : R.drawable.active_dot
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";

        ImageView img;

        int[] bgs = new int[]{R.drawable.search, R.drawable.reservednew, R.drawable.chat};


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            textView = (TextView) rootView.findViewById(R.id.title_textview);
            content =(TextView)rootView.findViewById(R.id.content);

            img = (ImageView) rootView.findViewById(R.id.imae);
            img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);


            return rootView;
        }



    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0)
            {
                textView.setText(titleof[position]);
                content.setText(contentof[position]);

            }
            else if(position==1)
            {
                textView.setText(titleof[position]);
                content.setText(contentof[position]);
            }
            else if(position==2)
            {
                textView.setText(titleof[position]);
                content.setText(contentof[position]);
            }
            return super.getPageTitle(position);
        }
    }



}
