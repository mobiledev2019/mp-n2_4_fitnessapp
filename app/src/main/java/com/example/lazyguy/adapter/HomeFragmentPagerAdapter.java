package com.example.lazyguy.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lazyguy.fragment.CountFragment;
import com.example.lazyguy.fragment.ExploreFragment;
import com.example.lazyguy.R;
import com.example.lazyguy.fragment.CustomFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HomeFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ExploreFragment();
        } else if (position == 1){
            return new CustomFragment();
        } else {
            return new CountFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_explorer);
            case 1:
                return mContext.getString(R.string.category_target);
            case 2:
                return mContext.getString(R.string.category_counting);
            default:
                return null;
        }
    }

}

