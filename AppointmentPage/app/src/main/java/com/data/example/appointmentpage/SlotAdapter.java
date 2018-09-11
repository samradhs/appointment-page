package com.data.example.appointmentpage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.List;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

public class SlotAdapter extends AbstractExpandableItemAdapter<SlotAdapter.DaytimeViewHolder,
    SlotAdapter.SlotViewHolder> {

  private static final String TAG = "SlotAdapter";

  private Context         context;
  private List<GroupItem> items;

  SlotAdapter(Context context, List<GroupItem> items) {

    Log.i(TAG, "creating adapter: " + items.size());
    this.context  = context;
    this.items    = items;
    setHasStableIds(true);
  }

  @Override
  public int getGroupCount() {

    return items.size();
  }

  @Override
  public int getChildCount(int groupPosition) {

    return items.get(groupPosition).getChildItemList().size();
  }

  @Override
  public long getGroupId(int groupPosition) {

    return items.get(groupPosition).getId();
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {

    return items.get(groupPosition).getChildItemList().get(childPosition).getId();
  }

  @Override
  public DaytimeViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {

    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view               = inflater.inflate(R.layout.view_daytime, parent, false);
    return new DaytimeViewHolder(view);
  }

  @Override
  public SlotViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view               = inflater.inflate(R.layout.view_slot, parent, false);
    return new SlotViewHolder(view);
  }

  @Override
  public void onBindGroupViewHolder(DaytimeViewHolder holder, int groupPosition, int viewType) {

    GroupItem groupItem = items.get(groupPosition);
    holder.bind(groupItem);
  }

  @Override
  public void onBindChildViewHolder(SlotViewHolder holder, int groupPosition, int childPosition, int viewType) {

    ChildItem childItem = items.get(groupPosition).getChildItemList().get(childPosition);
    holder.bind(childItem);
  }

  @Override
  public boolean onCheckCanExpandOrCollapseGroup(DaytimeViewHolder holder, int groupPosition, int x, int y, boolean expand) {

    GroupItem groupItem = items.get(groupPosition);
    if (groupItem.getChildItemList().isEmpty()) {
      Toast.makeText(context, R.string.no_slots, Toast.LENGTH_SHORT).show();
      return false;
    }

    groupItem.setExpanded(expand);
    return true;
  }

  @SuppressWarnings("WeakerAccess")
  public class DaytimeViewHolder extends AbstractExpandableItemViewHolder {

    private TextView  slotsAvail;
    private TextView  daytimeText;
    private View      daytimeArrow;

    public DaytimeViewHolder(View itemView) {
      super(itemView);

      slotsAvail    = itemView.findViewById(R.id.daytime_slots_avail);
      daytimeText   = itemView.findViewById(R.id.daytime_text);
      daytimeArrow  = itemView.findViewById(R.id.daytime_arrow_ic);
    }

    void bind(GroupItem groupItem) {

      slotsAvail.setText(context.getString(R.string.slot_avail_text, groupItem.getAvailableSlots()));
      daytimeText.setText(groupItem.getDaytime().getName());
      daytimeArrow.setVisibility(groupItem.getChildItemList().isEmpty() ? View.INVISIBLE : View.VISIBLE);
      rotateArrow(!groupItem.isExpanded());
    }

    void rotateArrow(boolean rotateDown) {

      daytimeArrow.setRotation(rotateDown ? 0 : 180);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public class SlotViewHolder extends AbstractExpandableItemViewHolder {

    private View      slotRoot;
    private TextView  slotTime;

    public SlotViewHolder(View itemView) {
      super(itemView);

      slotRoot = itemView;
      slotTime = itemView.findViewById(R.id.slot_time);
    }

    void bind(ChildItem itemData) {

      slotRoot.setBackgroundResource(itemData.isAvailable() ? R.color.white : R.color.grey);
      slotTime.setText(itemData.getDuration());
    }
  }
}
