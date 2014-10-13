package com.example.hcp.home_control_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;



public class MainActivity extends FragmentActivity {
    private static final int pageCount = 3;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_root);

        viewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new SlideViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }


    public void switchViewHandler(View v){
        Intent toggleIntent = new Intent(this, ToggleSwitchFragment.class);
        startActivity(toggleIntent);
    }

    private class SlideViewAdapter extends FragmentStatePagerAdapter {
        public SlideViewAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            switch(i){
                case 0:
                    return new MainPageFragment();
                case 1:
                    return new ToggleLightFragment();
                case 2:
                    return new SettingsFragment();

            }
            return new MainPageFragment();
        }


        @Override
        public int getCount() {
            return pageCount;
        }
    }

    public void switchWiiGeeTestHandler(View v) {
        Intent toggleIntent = new Intent(this, AddGestureActivity.class);
        startActivity(toggleIntent);
    }

}
