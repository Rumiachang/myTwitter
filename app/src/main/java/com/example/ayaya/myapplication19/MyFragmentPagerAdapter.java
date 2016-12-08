package com.example.ayaya.myapplication19;

<<<<<<< HEAD
import android.content.Context;
=======
>>>>>>> origin/master
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by ayaya on 2016/11/16.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private long userId;
    private Bundle args = new Bundle();
<<<<<<< HEAD
    private Context context;
=======
>>>>>>> origin/master
    public MyFragmentPagerAdapter(FragmentManager fm, long userId){
        super(fm);
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        args.putLong("USER_ID", userId);
<<<<<<< HEAD

=======
>>>>>>> origin/master
        switch (position){

            case 0:
                FragmentOfUserTimeLineList fragment0 = new FragmentOfUserTimeLineList();
                fragment0.setArguments(args);
                return fragment0;

            case 1:
                FragmentOfUserMediasGrid fragment1 = new FragmentOfUserMediasGrid();
                fragment1.setArguments(args);
                return fragment1;
            default:
                FragmentOfUserMediasGrid fragment2 = new FragmentOfUserMediasGrid();
                fragment2.setArguments(args);
                return fragment2;

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
