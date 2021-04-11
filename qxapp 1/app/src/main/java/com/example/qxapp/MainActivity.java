package com.example.qxapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.qxapp.adapter.pageAdapter;
import com.example.qxapp.fragments.fragmentHome;
import com.example.qxapp.fragments.fragmentProduct;
import com.example.qxapp.fragments.fragmentUs;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

private ViewPager viewPager;
private BottomNavigationBar bottomNavigationBar;
private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = findViewById(R.id.botton_navigator);
        viewPager = findViewById(R.id.viewpager);
        //初始化
        initView();

    }
    //对view page和底部导航栏进行初始化
    private void initView() {
        initPageView();
        initBottomNavigation();
    }
    //配置底部导航栏
    private void initBottomNavigation() {

        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED); //自适应大小
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT); //默认背景
        bottomNavigationBar.setBarBackgroundColor(R.color.linen)
                           .setActiveColor(R.color.deeppink)
                           .setInActiveColor(R.color.black);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.homepage,"Main").setInactiveIconResource(R.drawable.homepage1))
                            .addItem(new BottomNavigationItem(R.drawable.aboutus2,"Account").setInactiveIconResource(R.drawable.aboutus1))
                            .addItem(new BottomNavigationItem(R.drawable.product2,"product").setInactiveIconResource(R.drawable.product1))
                            .setFirstSelectedPosition(0)
                            .initialise();
    }
    //配置view page
    private void initPageView() {

        //配置缓存，有三个导航，main，account和product
        viewPager.setOffscreenPageLimit(3);

        fragments = new ArrayList<Fragment>();
        fragments.add(new fragmentHome());
        fragments.add(new fragmentUs());
        fragments.add(new fragmentProduct());

        viewPager.setAdapter(new pageAdapter(getSupportFragmentManager(),fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);



    }


    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
