package com.malkoti.capstone.mycommunity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BottomBarFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();

    public BottomBarFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }


    /**
     * Add fragment instances to the adapter
     * @param fragment Fragment to add
     */
    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    /**
     *
     * @param fragment
     * @return
     */
    public int getFragmentPosition(Fragment fragment) {
        if(fragments==null) return -1;

        return fragments.indexOf(fragment);
    }
}


// https://gist.github.com/nesquena/c715c9b22fb873b1d259#file-smartfragmentstatepageradapter-java
/**
Extension of FragmentStatePagerAdapter which intelligently caches
all active fragments and manages the fragment lifecycles.
Usage involves extending from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
*/
abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    // Sparse array to keep track of registered fragments in memory
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Register the fragment when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}