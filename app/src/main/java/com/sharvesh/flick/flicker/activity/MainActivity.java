package com.sharvesh.flick.flicker.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.adapter.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    Typeface typeface;
    private TextView appName;
    private TabLayout tabLayout;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    android.support.design.widget.FloatingActionButton floatingActionButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.appbarlayout_id);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_id_main);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        floatingActionButton = findViewById(R.id.floating_fab_menu);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.floating_fab_menu), "Favorite Movies List", "Added favourite movies will shown here.")
        .tintTarget(false).outerCircleColor(R.color.red));

        appName = findViewById(R.id.app_name);
        typeface = Typeface.createFromAsset(getAssets(), "FTY STRATEGYCIDE NCV.ttf");
        appName.setTypeface(typeface);

        tabLayout = findViewById(R.id.tabs);
        final ViewPager viewPager = findViewById(R.id.viewpager);

        final TabItem popularTabItem = findViewById(R.id.popular);
        final TabItem topRatedTabItem = findViewById(R.id.top_rated);
        final TabItem nowPlayingTabItem = findViewById(R.id.now_playing);
        final TabItem upComingTabItem = findViewById(R.id.up_coming);


        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
                LinearLayout tabs = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabText = (TextView) tabs.getChildAt(1);
                tabText.setTypeface(tabText.getTypeface(), Typeface.NORMAL);
                int colorFrom = ((ColorDrawable) collapsingToolbarLayout.getBackground()).getColor();
                int colorTo = getColorForTab(tab.getPosition());

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(1000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int color = (int) animator.getAnimatedValue();

                        toolbar.setBackgroundColor(color);
                        tabLayout.setBackgroundColor(color);
                        coordinatorLayout.setBackgroundColor(color);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                    }

                });
                colorAnimation.start();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tabs = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabs.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Popular");
        tabLayout.getTabAt(1).setText("Top Rated");
        tabLayout.getTabAt(2).setText("Now Playing");
        tabLayout.getTabAt(3).setText("Up Coming");
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public int getColorForTab(int position) {
        if (position == 0) return ContextCompat.getColor(this, R.color.blue);
        else if (position == 1) return ContextCompat.getColor(this, R.color.red);
        else if (position == 2) return ContextCompat.getColor(this, R.color.green);
        else if (position == 3) return ContextCompat.getColor(this, R.color.yellow);
        else return ContextCompat.getColor(this, R.color.blue);
    }

}