package com.example.hcp.home_control_prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.hcp.home_control_prototype.Spark.GetDevicesTask;
import com.example.hcp.home_control_prototype.Spark.Spark;


public class MainActivity extends FragmentActivity implements OnTaskCompleted {
    private static final int pageCount = 2;
    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_root);

        viewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new SlideViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        GetDevicesTask gdt = new GetDevicesTask(this);
        showProgressSpinner();
        gdt.execute(Spark.getInstance().getCurrentToken().getValue());

    }

    @Override
    public void onTaskCompleted(Object obj, Context context) {
        Log.i(TAG, "SHIT SON I RECEIVED THE DEVICES ALREADY.");
        hideProgressSpinner();
        startService(new Intent(this,BGRunnerService.class));
    }

    private void showProgressSpinner(){
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Discovering devices...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }
    private void hideProgressSpinner(){
        dialog.hide();
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
