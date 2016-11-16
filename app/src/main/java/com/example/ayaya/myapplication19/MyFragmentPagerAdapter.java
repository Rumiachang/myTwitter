package com.example.ayaya.myapplication19;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by ayaya on 2016/11/16.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public MyFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentOfUserTimeLineList();
            case 1:
                return new FragmentOfUserMediasGrid();
            default:
                return new FragmentOfUsersFavoritesList();
        }

    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int pos){
        switch (pos){
            case 0:
                return "ツイート";
            case 1:
                return "メディア";
            default:
                return "お気に入り";
        }
    }

}
