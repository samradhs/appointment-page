package com.data.example.appointmentpage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

public class Slot {

  @SerializedName("slot_id")
  private int slotId;

  @SerializedName("start_time")
  private String startTime;

  @SerializedName("end_time")
  private String endTime;

  @SerializedName("is_booked")
  private boolean isBooked;

  @SerializedName("is_expired")
  private boolean isExpired;

  @SerializedName("username")
  private String username;

  public Slot(int slotId, String startTime, String endTime, boolean isBooked,
              boolean isExpired, String username) {

    this.slotId     = slotId;
    this.startTime  = startTime;
    this.endTime    = endTime;
    this.isBooked   = isBooked;
    this.isExpired  = isExpired;
    this.username   = username;
  }

  int getSlotId() {

    return slotId;
  }

  String getStartTime() {

    return startTime;
  }

  String getEndTime() {

    return endTime;
  }

  boolean isBooked() {

    return isBooked;
  }

  boolean isExpired() {

    return isExpired;
  }

  String getUsername() {

    return username;
  }
}
