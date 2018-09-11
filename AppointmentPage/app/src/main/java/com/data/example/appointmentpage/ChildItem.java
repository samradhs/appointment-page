package com.data.example.appointmentpage;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

class ChildItem {

  private int     id;
  private String  duration;
  private boolean isAvailable;

  ChildItem(int id, String duration, boolean isAvailable) {

    this.id           = id;
    this.duration     = duration;
    this.isAvailable  = isAvailable;
  }

  int getId() {

    return id;
  }

  String getDuration() {

    return duration;
  }

  boolean isAvailable() {

    return isAvailable;
  }
}
