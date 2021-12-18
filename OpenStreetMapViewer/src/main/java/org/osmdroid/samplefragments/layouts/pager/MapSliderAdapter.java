package org.osmdroid.samplefragments.layouts.pager;

import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.core.app.FragmentStatePagerAdapter;

/**
 * Created by alex on 10/22/16.
 */

public class MapSliderAdapter extends FragmentStatePagerAdapter {
    public MapSliderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SimpleTextFragment();
            case 1:
                return new MapFragment();
            case 2:
                return new WebviewFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
