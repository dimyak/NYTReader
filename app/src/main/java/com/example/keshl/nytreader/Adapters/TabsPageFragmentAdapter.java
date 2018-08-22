package com.example.keshl.nytreader.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabsPageFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> tabFragments = new ArrayList<>();
    private List<String> tabFragmentsTitle = new ArrayList<>();


    public TabsPageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabFragmentsTitle.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabFragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        tabFragments.add(fragment);
        tabFragmentsTitle.add(title);
    }

}
