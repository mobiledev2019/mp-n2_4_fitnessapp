package com.example.gymproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HomeFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ExplorerFragment();
        } else if (position == 1){
            return new CountFragment();
        } else if (position == 2){
            return new ExplorerFragment();
        } else {
            return new CountFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_explorer);
            case 1:
                return mContext.getString(R.string.category_counting);
            case 2:
                return mContext.getString(R.string.category_remind);
            case 3:
                return mContext.getString(R.string.category_target);
            default:
                return null;
        }
    }

}
