package com.data.example.appointmentpage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends AppCompatActivity {

  private static final String TAG             = "AppointmentActivity";

  private static final String EXPERT_USERNAME = "roshini@healthifyme.com";
  private static final String VC              = "649";
  private static final String USERNAME        = "jasper141@example.com";
  private static final String API_KEY         = "e353bade4f4bcff96946b76fb75c8f1bedb656bb";
  private static final String DATE_FORMAT     = "yyyy-MM-dd HH:mm:ssX";

  private Call<List<Slot>> call;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_appointment);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.toolbar_text);
    }

    callApi();
  }

  @Override
  protected void onDestroy() {

    call.cancel();
    call = null;

    super.onDestroy();
  }

  private void callApi() {

    showProgress();
    GetListService service = RetrofitInstance.getRetrofitInstance().create(GetListService.class);
    call = service.getSlots(EXPERT_USERNAME, VC, USERNAME, API_KEY);

    call.enqueue(new Callback<List<Slot>>() {

      @Override
      public void onResponse(@NonNull Call<List<Slot>> call, @NonNull Response<List<Slot>> response) {

        List<Slot> body = response.body();
        if (body == null) return;

        Calendar cal                    = Calendar.getInstance();
        TimeZone timeZone               = cal.getTimeZone();
        TreeMap<Integer, SlotDate> map  = new TreeMap<>();

        for (Slot slot: body) {
          SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

          try {
            Date startTime = dateFormat.parse(slot.getStartTime());
            cal.setTime(startTime);
            cal.setTimeZone(timeZone);

            int startDate       = cal.get(Calendar.DATE);
            int startDay        = cal.get(Calendar.DAY_OF_WEEK);
            int hourOfDay       = cal.get(Calendar.HOUR_OF_DAY);
            int month           = cal.get(Calendar.MONTH);
            String startTimeStr = getTimeString(cal);
            Daytime daytime     = Daytime.getFromHourOfDay(hourOfDay);

            Date endTime = dateFormat.parse(slot.getEndTime());
            cal.setTime(endTime);
            cal.setTimeZone(timeZone);
            String endTimeStr = getTimeString(cal);

            SlotDate slotDate = map.get(startDate);
            if (slotDate == null) {
              TreeMap<Daytime, GroupItem> daytimeMap = new TreeMap<>();
              daytimeMap.put(Daytime.MORNING, new GroupItem(Daytime.MORNING.ordinal(),
                  Daytime.MORNING, new ArrayList<ChildItem>()));
              daytimeMap.put(Daytime.AFTERNOON, new GroupItem(Daytime.AFTERNOON.ordinal(),
                  Daytime.AFTERNOON, new ArrayList<ChildItem>()));
              daytimeMap.put(Daytime.EVENING, new GroupItem(Daytime.EVENING.ordinal(),
                  Daytime.EVENING, new ArrayList<ChildItem>()));

              slotDate = new SlotDate(startDate, startDay, month, daytimeMap);
              map.put(startDate, slotDate);
            }

            TreeMap<Daytime, GroupItem> groupItemMap = slotDate.getMap();
            GroupItem groupItem = groupItemMap.get(daytime);

            ChildItem childItem = new ChildItem(slot.getSlotId(),
                startTimeStr + " - " + endTimeStr, !slot.isExpired() && !slot.isBooked());
            groupItem.getChildItemList().add(childItem);

          } catch (ParseException e) {
            Log.e(TAG, "parse exception: " + e.toString());
          }
        }

        setScreen(map);
      }

      @Override
      public void onFailure(@NonNull Call<List<Slot>> call, @NonNull Throwable t) {

        Log.e(TAG, "failed when parsing or getting data");
        showPlaceholder();
      }
    });
  }

  private void showProgress() {

    findViewById(R.id.app_progress).setVisibility(View.VISIBLE);
    findViewById(R.id.app_placeholder_cont).setVisibility(View.GONE);
    findViewById(R.id.app_main_cont).setVisibility(View.GONE);
  }

  private void showPlaceholder() {

    findViewById(R.id.app_progress).setVisibility(View.GONE);
    findViewById(R.id.app_placeholder_cont).setVisibility(View.VISIBLE);
    findViewById(R.id.app_main_cont).setVisibility(View.GONE);

    findViewById(R.id.app_retry).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Context context = AppointmentActivity.this;
        if (isNetworkAvailable(context)) {
          callApi();
          return;
        }

        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void setScreen(final TreeMap<Integer, SlotDate> map) {

    Log.i(TAG, "going to set view pager");

    final List<DateFragment> list = new ArrayList<>();

    for (SlotDate slotDate : map.values()) {
      list.add(DateFragment.newInstance(slotDate));
    }

    ViewPager viewPager     = findViewById(R.id.app_view_pager);
    DateAdapter dateAdapter = new DateAdapter(getSupportFragmentManager(), list);
    viewPager.setAdapter(dateAdapter);

    TabLayout tabLayout = findViewById(R.id.app_tabs);
    tabLayout.setupWithViewPager(viewPager);
    setMonth(map.firstEntry().getValue().getMonth());

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {

        int i = 0;
        for (SlotDate slotDate : map.values()) {
          if (i == position) {
            setMonth(slotDate.getMonth());
            break;
          }

          i++;
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    findViewById(R.id.app_progress).setVisibility(View.GONE);
    findViewById(R.id.app_placeholder_cont).setVisibility(View.GONE);
    findViewById(R.id.app_main_cont).setVisibility(View.VISIBLE);
  }

  private String getTimeString(Calendar cal) {

    int hour  = cal.get(Calendar.HOUR);
    int min   = cal.get(Calendar.MINUTE);
    int amPm  = cal.get(Calendar.AM_PM);

    if (hour == 0 && cal.get(Calendar.AM_PM) == 1) hour = 12;

    StringBuilder sb = new StringBuilder();

    if (hour < 10) sb.append(0);
    sb.append(hour).append(':');
    if (min < 10) sb.append(0);
    sb.append(min).append(' ');
    sb.append(amPm == 1 ? "PM" : "AM");
    return sb.toString();
  }

  private boolean isNetworkAvailable(Context context) {

    ConnectivityManager connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetworkInfo = null;
    if (connectivityManager != null) {
      activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    }

    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  private void setMonth(int month) {

    String monthStr;

    try {
      monthStr = getResources().getStringArray(R.array.month_names)[month];
    } catch (ArrayIndexOutOfBoundsException e) {
      monthStr = Integer.toString(month);
    }

    TextView monthView = findViewById(R.id.app_month);
    monthView.setText(monthStr);
  }
}
