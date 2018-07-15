package com.sharvesh.flick.flicker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sharvesh.flick.flicker.Fragments.NowPlayingMoviesFragment;
import com.sharvesh.flick.flicker.Fragments.PopularMoviesFragment;
import com.sharvesh.flick.flicker.Fragments.TopRatedMovieFragment;
import com.sharvesh.flick.flicker.Fragments.UpComingMoviesFragment;


public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PagerAdapter(FragmentManager manager, int numOfTabs) {
        super(manager);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new PopularMoviesFragment();
            case 1: return new TopRatedMovieFragment();
            case 2: return new NowPlayingMoviesFragment();
            case 3: return new UpComingMoviesFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
