package com.gn.djay.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private String[] contentArray = {"中文","英语","法语","德语","俄语","日语","韩语","西班牙语"};

    private List<MyFragment> fragments = new ArrayList<MyFragment>();

    private ViewPager viewPager;

    private FragmentPagerAdapter fragmentAdapter;

    private ViewPagerIndicator indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)findViewById(R.id.viewager);


        initData();

        fragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        viewPager.setAdapter(fragmentAdapter);

        indicator = (ViewPagerIndicator)findViewById(R.id.indicator);
        indicator.setTabTitles(contentArray);
        indicator.setViewpager(viewPager,0);


    }

    public void initData(){
        for(String str:contentArray){
            MyFragment fragment = MyFragment.generateFragment(str);
            fragments.add(fragment);
        }
    }


}
