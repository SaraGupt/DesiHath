package com.example.desihand;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DashboardAdapter extends FragmentPagerAdapter
{
   Context context;
    int totalTabs;


    public DashboardAdapter(Context applicationContext, FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        context = applicationContext;
        this.totalTabs = tabCount;
    }

    @Override
    public int getCount() {

        return totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DesiHandDashboard desiHandDashboard = new DesiHandDashboard();
                return desiHandDashboard;
            case 1:
                RequestItemFragment requestItemFragment = new RequestItemFragment();
                return requestItemFragment;
            case 2:
                HelpOutFragment helpOutFragment = new HelpOutFragment();
                return helpOutFragment;
            default:
        return null;
    }


}
    }
