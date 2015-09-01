package com.ulrik.rides.view.fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 30/03/2015
 * Time: 14:37
 */
public class NavegationFragment {

    private List<Fragment> fragments;
    private List<String> titles;

    public NavegationFragment() {
        fragments = new LinkedList<Fragment>();
        titles = new LinkedList<String>();
    }

    public void addTitles(String... titles) {
        Collections.addAll(this.titles, titles);
    }

    public void addFragments(Fragment... fragments) {
        Collections.addAll(this.fragments, fragments);
    }

    public void makeTabs(final FragmentManager fragmentManager, ViewPager viewPager, ActionBar actionBar) {
        ViewPagerListener viewPagerListener = new ViewPagerListener(viewPager, actionBar);
        viewPager.setOnPageChangeListener(viewPagerListener);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String title : titles) {
            actionBar.addTab(actionBar.newTab().setText(title).setTabListener(viewPagerListener));
        }

        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

    }

    private class ViewPagerListener extends ViewPager.SimpleOnPageChangeListener implements ActionBar.TabListener {
        private ViewPager viewPager;
        private ActionBar actionBar;

        public ViewPagerListener(ViewPager viewPager, ActionBar actionBar) {
            this.viewPager = viewPager;
            this.actionBar = actionBar;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onPageSelected(int position) {
            actionBar.setSelectedNavigationItem(position);
        }
    }
}
