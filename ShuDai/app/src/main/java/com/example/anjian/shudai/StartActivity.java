package com.example.anjian.shudai;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class StartActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private SousuoActivity sousuoActivity;
    private BookShelfActivity bookShelfActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        initView();
    }

    public void initView(){
        titles.add("书架");
        titles.add("搜索");

        tabLayout= (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager)findViewById(R.id.vpager);

        sousuoActivity = new SousuoActivity();
        bookShelfActivity = new BookShelfActivity();

        fragmentsList.add(bookShelfActivity);
        fragmentsList.add(sousuoActivity);

        FragmentAdapter myPagerAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentsList,titles);
        tabLayout.setTabsFromPagerAdapter(myPagerAdapter);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
