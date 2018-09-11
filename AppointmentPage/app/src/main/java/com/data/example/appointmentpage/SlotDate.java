package com.data.example.appointmentpage;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by SamradhShukla
 * on 11/09/18.
 */

class SlotDate implements Serializable {

  private int                         date;
  private int                         day;
  private int                         month;
  private TreeMap<Daytime, GroupItem> map;

  SlotDate(int date, int day, int month, TreeMap<Daytime, GroupItem> map) {

    this.date   = date;
    this.day    = day;
    this.month  = month;
    this.map    = map;
  }

  int getDate() {

    return date;
  }

  int getDay() {

    return day;
  }

  int getMonth() {

    return month;
  }

  TreeMap<Daytime, GroupItem> getMap() {

    return map;
  }
}
