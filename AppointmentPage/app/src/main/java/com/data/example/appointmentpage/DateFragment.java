package com.data.example.appointmentpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

public class DateFragment extends Fragment implements TabFragment {

  private static final String TAG       = "DateFragment";
  private static final String SLOT_DATE = "slotDate";

  private SlotDate slotDate;

  static DateFragment newInstance(SlotDate slotDate) {

    DateFragment fragment = new DateFragment();
    fragment.slotDate     = slotDate;
    Bundle bundle         = new Bundle(1);

    bundle.putSerializable(SLOT_DATE, slotDate);
    fragment.setArguments(bundle);
    return fragment;
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    slotDate = (SlotDate) getArguments().getSerializable(SLOT_DATE);
  }

  @SuppressWarnings("ConstantConditions")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

    Log.i(TAG, "creating view");

    View root = inflater.inflate(R.layout.fragment_date, container, false);

    RecyclerView dateSlots                        = root.findViewById(R.id.date_slots);
    RecyclerViewExpandableItemManager itemManager = new RecyclerViewExpandableItemManager(null);
    LinearLayoutManager linearLayoutManager       = new LinearLayoutManager(getActivity());

    List<GroupItem> groupList = new ArrayList<>();
    for (GroupItem groupItem : slotDate.getMap().values()) {

      Collections.sort(groupItem.getChildItemList(), new Comparator<ChildItem>() {
        @Override
        public int compare(ChildItem item1, ChildItem item2) {

          return item1.getId() - item2.getId();
        }
      });

      int available = 0;
      for (ChildItem childItem : groupItem.getChildItemList()) {
        if (childItem.isAvailable()) available++;
      }

      groupItem.setAvailableSlots(available);
      groupList.add(groupItem);
    }

    SlotAdapter slotAdapter             = new SlotAdapter(getActivity(), groupList);
    RecyclerView.Adapter wrappedAdapter = itemManager.createWrappedAdapter(slotAdapter);

    GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
    animator.setSupportsChangeAnimations(false);

    dateSlots.setLayoutManager(linearLayoutManager);
    dateSlots.setAdapter(wrappedAdapter);
    dateSlots.setItemAnimator(animator);
    dateSlots.setHasFixedSize(false);

    DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
    dateSlots.addItemDecoration(decoration);

    itemManager.attachRecyclerView(dateSlots);
    return root;
  }

  @Override
  public String getTabTitle() {

    return String.valueOf(slotDate.getDate()) + "\n" + getDayStr(slotDate.getDay());
  }

  private String getDayStr(int day) {

    String[] weekdays = new DateFormatSymbols().getWeekdays();
    return weekdays[day].substring(0, 3);
  }
}
