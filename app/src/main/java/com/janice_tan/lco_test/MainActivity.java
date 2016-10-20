package com.janice_tan.lco_test;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    FragmentPlace fragmentPlace;
    FragmentHome fragmentHome;
    FragmentMap fragmentMap;
    FragmentItenerary fragmentItenerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentPlace = new FragmentPlace();
        fragmentHome = new FragmentHome();
        fragmentMap = new FragmentMap();
        fragmentItenerary = new FragmentItenerary();


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                switch (tabId) {
                    case R.id.tab_home:
                        fragmentTransaction.replace(R.id.frameLayout, fragmentHome);
                        break;
                    case R.id.tab_places:
                        fragmentTransaction.replace(R.id.frameLayout, fragmentPlace);
                        break;
                    case R.id.tab_map:
                        fragmentTransaction.replace(R.id.frameLayout, fragmentMap);
                        break;
                    case R.id.tab_itenerary:
                        fragmentTransaction.replace(R.id.frameLayout, fragmentItenerary);
                        break;
                }

                fragmentTransaction.commit();
            }
        });


    }

}