package com.data.example.appointmentpage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SamradhShukla
 * on 10/09/18.
 */

interface GetListService {

  @GET("/api/v2/booking/slots/all/")
  Call<List<Slot>> getSlots(@Query("expert_username") String expertName,
                            @Query("vc") String vc,
                            @Query("username") String username,
                            @Query("api_key") String apiKey);
}
