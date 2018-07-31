package com.sharvesh.flick.flicker.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sharvesh.flick.flicker.BuildConfig;
import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.adapter.MovieAdapter;
import com.sharvesh.flick.flicker.adapter.PagerAdapter;
import com.sharvesh.flick.flicker.api.Client;
import com.sharvesh.flick.flicker.api.Services;
import com.sharvesh.flick.flicker.database.FavoriteDbHelper;
import com.sharvesh.flick.flicker.model.MovieResponse;
import com.sharvesh.flick.flicker.model.Movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class MainActivity extends AppCompatActivity {

    Typeface typeface;
    private TextView appName;
    private TabLayout tabLayout;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton floatingActionButtonSettings, floatingActionButtonFav;
    CoordinatorLayout coordinatorLayout;
    private String[] titles = {"POPULAR", "TOP RATED", "NOW PLAYING", "UP COMING"};

    CollapsingToolbarLayout collapsingToolbarLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_id_main);

//        ImageView img = findViewById(R.id.img);
//        Picasso.with(this).load("http://image.tmdb.org/t/p/w1280////gBmrsugfWpiXRh13Vo3j0WW55qD.jpg").into(img);

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        floatingActionMenu = findViewById(R.id.floating_fab_menu);
        floatingActionButtonSettings = findViewById(R.id.fab_action_button_settings);
        floatingActionButtonFav = findViewById(R.id.fab_action_button_fav);

        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (floatingActionMenu.isOpened()) {
                    coordinatorLayout.setVisibility(View.GONE);
                } else {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        appName = findViewById(R.id.app_name);
        typeface = Typeface.createFromAsset(getAssets(), "FTY STRATEGYCIDE NCV.ttf");
        appName.setTypeface(typeface);

        tabLayout = findViewById(R.id.tabs);
        final TabItem popularTabItem = findViewById(R.id.popular);
        final TabItem topRatedTabItem = findViewById(R.id.top_rated);
        final TabItem nowPlayingTabItem = findViewById(R.id.now_playing);
        final TabItem upComingTabItem = findViewById(R.id.up_coming);
        final ViewPager viewPager = findViewById(R.id.viewpager);


        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }

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

                int colorFrom = ((ColorDrawable) toolbar.getBackground()).getColor();
                int colorTo = getColorForTab(tab.getPosition());

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(1000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int color = (int) animator.getAnimatedValue();

                        toolbar.setBackgroundColor(color);
                        tabLayout.setBackgroundColor(color);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                    }

                });
                colorAnimation.start();
                toolbar.setTitle(titles[tab.getPosition()].toUpperCase());
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
        onClickSettingFab();
        onClickFavFab();

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

    private void onClickSettingFab() {
        floatingActionButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onClickFavFab() {
        floatingActionButtonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });
    }

    public int getColorForTab(int position) {
        if (position == 0) return ContextCompat.getColor(this, R.color.blue);
        else if (position == 1) return ContextCompat.getColor(this, R.color.red);
        else if (position == 2) return ContextCompat.getColor(this, R.color.green);
        else if (position == 3) return ContextCompat.getColor(this, R.color.yellow);
        else return ContextCompat.getColor(this, R.color.blue);
    }

}