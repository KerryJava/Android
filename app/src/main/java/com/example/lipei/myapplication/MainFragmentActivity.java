package com.example.lipei.myapplication;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        FragmentManager fm = getSupportFragmentManager();
        MainFragmentActivityFragment main2ActivityFragment = new MainFragmentActivityFragment();
        fm.beginTransaction().replace(R.id.fl_main, main2ActivityFragment).commit();
    }


}
