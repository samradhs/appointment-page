package com.data.example.appointmentpage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

class RetrofitInstance {

  private static Retrofit retrofit;
  private static final String URL = "http://x123healthifyme.com";

  static Retrofit getRetrofitInstance() {

    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }

    return retrofit;
  }
}
