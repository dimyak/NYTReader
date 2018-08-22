package com.example.keshl.nytreader.activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.keshl.nytreader.R;
import com.example.keshl.nytreader.Adapters.TabsPageFragmentAdapter;
import com.example.keshl.nytreader.fragments.FavoritesFragment;
import com.example.keshl.nytreader.fragments.MostEmailedFragment;
import com.example.keshl.nytreader.fragments.MostSharedFragment;
import com.example.keshl.nytreader.fragments.MostViewedFragment;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolBar);

        initToolBar();
        initTabs();
    }

    private void initToolBar() {
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    private void initTabs() {
        initViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initViewPager() {
        TabsPageFragmentAdapter pageFragmentAdapter = new TabsPageFragmentAdapter(getSupportFragmentManager());
        pageFragmentAdapter.addFragment(new MostEmailedFragment(), getString(R.string.most_emailed_fragment));
        pageFragmentAdapter.addFragment(new MostSharedFragment(), getString(R.string.most_shared_fragment));
        pageFragmentAdapter.addFragment(new MostViewedFragment(), getString(R.string.most_viewed_fragment));
        pageFragmentAdapter.addFragment(new FavoritesFragment(), getString(R.string.favorites_fragment));
        viewPager.setAdapter(pageFragmentAdapter);
    }


}
