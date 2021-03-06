package com.moyersoftware.contender.game;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.moyersoftware.contender.R;
import com.moyersoftware.contender.util.Util;
import com.viewpagerindicator.CirclePageIndicator;

public class HowToPlayActivity extends AppCompatActivity {

    // Hey Ryan, you can change colors here
    private int[] mColors = new int[]{
            Color.parseColor("#50bb72"),
            Color.parseColor("#50bb72"),
            Color.parseColor("#50bb72"),
            Color.parseColor("#50bb72"),
            Color.parseColor("#50bb72"),
            Color.parseColor("#50bb72"),
            Color.parseColor("#50bb72")
    };
    // ... Images
    public static int[] mImages = new int[]{
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7
    };

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();
    private CirclePageIndicator mPageIndicator;
    private View mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        bindViews();
        initStatusBar();
        initActionBar();
        initViewPager();
        Util.setTutorialShown();
    }

    /**
     * Makes the status bar transparent.
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPageIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);
        mNextBtn = findViewById(R.id.next_btn);
    }

    private void initActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void initViewPager() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if (position < (mAdapter.getCount() - 1) && position < (mColors.length - 1)) {
                    mViewPager.setBackgroundColor((Integer) mArgbEvaluator.evaluate(positionOffset,
                            mColors[position], mColors[position + 1]));
                } else {
                    mViewPager.setBackgroundColor(mColors[mColors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
                mNextBtn.setVisibility(position == mColors.length - 1 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mPageIndicator.setViewPager(mViewPager);
    }

    public void onNextPageClicked(View view) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    public void onSkipButtonClicked(View view) {
        finish();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mColors.length;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return HowToPlayFragment.newInstance(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
