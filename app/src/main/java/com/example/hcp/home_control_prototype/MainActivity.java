package com.example.hcp.home_control_prototype;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.Spark.Spark;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;

import java.util.List;


public class MainActivity extends FragmentActivity {
    private static final int pageCount = 2;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_root);

        viewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new SlideViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        Spark spark = Spark.getInstance();
        spark.login("garygrantgraham@gmail.com", "coin0nioc");
        spark.findDevices();
        startService(new Intent(this,BGRunnerService.class));
        //startService(new Intent(this,BGGestureService.class));
    }



    private class SlideViewAdapter extends FragmentStatePagerAdapter {
        public SlideViewAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            switch(i){
                case 0:
                    return new ToggleLightFragment();
                case 1:
                    return new SettingsFragment();
                default:
                    return new ToggleLightFragment();
            }
        }


        @Override
        public int getCount() {
            return pageCount;
        }
    }



}
