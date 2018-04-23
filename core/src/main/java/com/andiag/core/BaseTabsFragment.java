package com.andiag.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTabsFragment<P extends BasePresenter> extends BaseFragment<P> {

    ViewPager mViewPager;
    TabLayout mTabLayout;

    private TabsAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_tabs;
    }

    private void setupTabs() {
        mAdapter = new TabsAdapter(getChildFragmentManager());
        for (BaseTab tab : getTabs()) {
            mAdapter.addTab(tab);
        }
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabLayout();
    }

    private void setupTabLayout() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(getTabs().get(i).getIcon());
        }
    }

    protected void reloadTabs() {
        if (mAdapter != null) {
            mAdapter.clearAll();
            for (BaseTab tab : getTabs()) {
                mAdapter.addTab(tab);
            }
            mAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            setupTabLayout();
            return;
        }
        setupTabs();
    }

    public abstract List<BaseTab> getTabs();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mViewPager = view.findViewById(R.id.viewPager);
            mTabLayout = view.findViewById(R.id.tabLayout);
            setupTabs();
        }
        return view;
    }

    static class TabsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        void addTab(BaseTab tab) {
            mFragments.add(tab.getFragment());
            mFragmentTitles.add(tab.getTitle());
        }

        void clearAll() {
            mFragments.clear();
            mFragmentTitles.clear();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence pageTitle = mFragmentTitles.get(position);
            return pageTitle.equals("") ? pageTitle : null;
        }
    }
}