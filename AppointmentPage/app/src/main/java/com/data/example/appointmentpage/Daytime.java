package com.data.example.appointmentpage;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

enum Daytime {

  MORNING("Morning"),
  AFTERNOON("Afternoon"),
  EVENING("Evening");

  private String name;

  Daytime(String name) {

    this.name = name;
  }

  String getName() {

    return name;
  }

  static Daytime getFromHourOfDay(int hour) {

    Daytime daytime;
    if (hour < 12) {
      daytime = Daytime.MORNING;

    } else if (hour < 16) {
      daytime = Daytime.AFTERNOON;

    } else {
      daytime = Daytime.EVENING;
    }

    return daytime;
  }
}
