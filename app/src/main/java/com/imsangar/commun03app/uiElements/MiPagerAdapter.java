package com.imsangar.commun03app.uiElements;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.imsangar.commun03app.fragments.TabBBDD;
import com.imsangar.commun03app.fragments.TabBTLE;
import com.imsangar.commun03app.fragments.TabLogin;

public class MiPagerAdapter extends FragmentStateAdapter {
    public MiPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    @NonNull
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TabBTLE();
            case 1:
                return new TabBBDD();
            case 2:
                return new TabLogin();
        }
        return null;
    }
}

