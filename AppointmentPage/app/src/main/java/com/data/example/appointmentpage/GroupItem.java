package com.data.example.appointmentpage;

import java.util.List;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

class GroupItem {

  private int             id;
  private Daytime         daytime;
  private List<ChildItem> childItemList;
  private boolean         isExpanded;
  private int             availableSlots;

  GroupItem(int id, Daytime daytime, List<ChildItem> childItemList) {

    this.id             = id;
    this.daytime        = daytime;
    this.childItemList  = childItemList;
  }

  int getId() {

    return id;
  }

  Daytime getDaytime() {

    return daytime;
  }

  List<ChildItem> getChildItemList() {

    return childItemList;
  }

  boolean isExpanded() {

    return isExpanded;
  }

  void setExpanded(boolean expanded) {

    isExpanded = expanded;
  }

  void setAvailableSlots(int availableSlots) {

    this.availableSlots = availableSlots;
  }

  int getAvailableSlots() {

    return availableSlots;
  }
}
