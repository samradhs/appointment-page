package com.data.example.appointmentpage;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

class DateAdapter extends FragmentStatePagerAdapter {

  private List<DateFragment> fragments;

  DateAdapter(FragmentManager fragmentManager, List<DateFragment> fragments) {
    super(fragmentManager);
    this.fragments = fragments;
  }

  @Override
  public Fragment getItem(int position) {

    return fragments.get(position);
  }

  @Override
  public int getCount() {
    return fragments.size();
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return fragments.get(position).getTabTitle();
  }
}
