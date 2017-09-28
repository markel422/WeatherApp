package com.foo.umbrella.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.umbrella.R;
import com.foo.umbrella.data.adapter.WeatherAdapter;
import com.foo.umbrella.data.adapter.WeatherAdapter2;
import com.foo.umbrella.UmbrellaApp;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.settings.SettingsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.foo.umbrella.R.id.appbar_layout;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";

    SharedPreferences weatherPref;

    RelativeLayout appBar;

    Toolbar myToolbar;

    CardView todayCard;

    EditText zipcodeTV;

    TextView currentWeatherTV;
    TextView skyTV;

    List<ForecastCondition> weatherDataList;
    List<ForecastCondition> weatherDataList2;

    RecyclerView recyclerView;
    RecyclerView tomRecyclerView;
    WeatherAdapter weatherAdapter;
    WeatherAdapter2 newAdapter;

    GridLayoutManager layoutManager;
    GridLayoutManager layoutManager2;

    @Inject
    MainPresenterImpl presenter;

    String zipcode;

    String formedDate;
    String adapterDate;

    Date date1, date2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        appBar = (RelativeLayout) findViewById(appbar_layout);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        todayCard = (CardView) findViewById(R.id.today_card);

        currentWeatherTV = (TextView) findViewById(R.id.current_weather_tv);
        skyTV = (TextView) findViewById(R.id.current_sky_tv);

        /*SharedPreferences.Editor editor = weatherPref.edit();
        editor.remove("zipcodeData");
        editor.apply();

        zipcode = null;*/

        checkSharedPref();

        getInitialWeatherData();

        setUpRecyclerView();
        setUpDaggerUsersComponent();

        weatherDataList = new ArrayList<>(0);
        weatherDataList2 = new ArrayList<>(0);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
        formedDate = dateFormat.format(currentTime);

        try {
            date1 = dateFormat.parse(formedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        presenter.init();
        presenter.getWeather(zipcode);
        presenter.getDate(date1, date2, weatherDataList);

        /*Log.d(TAG, "Current Time: " +  formedDate);
        Log.d(TAG, "Current Time in Date instance: " +  date1);*/
    }

    public void checkSharedPref() {
        if (weatherPref.getString("unitsData", "") == null) {
            SharedPreferences.Editor editor = weatherPref.edit();
            editor.putString("unitsData", "Fahrenheit");
            editor.apply();
        }
    }

    public void getInitialWeatherData() {

        String newValue = weatherPref.getString("zipcodeData", "");

        if (zipcode == null && newValue.equals("")) {
            getInitialZipcode();
        } else if (zipcode == null && !newValue.equals("")) {
            zipcode = newValue;
            Log.d(TAG, "onCreate: " + zipcode);
        }

        Log.d(TAG, "Zipcode in MainActivity: " + zipcode);
    }

    public void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.weather_today_recycler_view);
        tomRecyclerView = (RecyclerView) findViewById(R.id.weather_tomorrow_recycler_view);

        layoutManager = new GridLayoutManager(MainActivity.this, 4);
        layoutManager2 = new GridLayoutManager(MainActivity.this, 4);

        recyclerView.setLayoutManager(layoutManager);
        tomRecyclerView.setLayoutManager(layoutManager2);
        weatherAdapter = new WeatherAdapter(MainActivity.this, new ArrayList<>(0));

        recyclerView.setAdapter(weatherAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings_button:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                if (weatherPref.getString("unitsData", "") != null) {
                    if (weatherPref.getString("unitsData", "").equals("Celsius")) {
                        SharedPreferences.Editor editor = weatherPref.edit();
                        editor.putString("unitsData", "Celsius");
                        editor.apply();
                    } else if (weatherPref.getString("unitsData", "").equals("Fahrenheit")) {
                        SharedPreferences.Editor editor = weatherPref.edit();
                        editor.putString("unitsData", "Fahrenheit");
                        editor.apply();
                    }
                } else {
                    SharedPreferences.Editor editor = weatherPref.edit();
                    editor.putString("unitsData", "Fahrenheit");
                    editor.apply();
                }
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getInitialZipcode() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Enter A Zipcode");
        dialog.setContentView(R.layout.dialog_zipcode);
        // Get the layout inflater
        dialog.show();

        zipcodeTV = (EditText) dialog.findViewById(R.id.zipcode_tv);
        Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
        Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipcode = zipcodeTV.getText().toString();

                if (zipcode.length() < 5) {
                    presenter.checkZipcode(zipcode);
                } else {
                    SharedPreferences.Editor editor = weatherPref.edit();

                    editor.putString("zipcodeData", zipcode);
                    editor.apply();

                    Log.d(TAG, "Zipcode in Dialog: " + zipcode);
                    Log.d(TAG, "Zipcode in Shared Preferences within Dialog: " + weatherPref.getString("zipcodeData", ""));

                    presenter.init();
                    presenter.getWeather(zipcode);
                    presenter.getDate(date1, date2, weatherDataList);
                    Toast.makeText(MainActivity.this, "Zipcode Entered", Toast.LENGTH_SHORT).show();
                    //getWeatherData(zipcode);
                    dialog.cancel();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(dialog.getContext())
                        .setMessage("A zipcode must be entered to proceed.\n\nAre you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                //dialog.cancel();
            }
        });
    }

    @Override
    public void showWeather(List<ForecastCondition> forecastList) {
        weatherAdapter.updateDataSet(forecastList);
        newAdapter.updateDataSet(forecastList);
    }

    @Override
    public void setZipcodeError() {
        zipcodeTV.setError(getString(R.string.zip_error));
    }

    @Override
    public void obtainWeather(String zipFullName, String observation, String state, List<ForecastCondition> dataList, String datetime) {
        myToolbar.setTitle(zipFullName);
        currentWeatherTV.setText(observation);
        skyTV.setText(state);
        weatherDataList = dataList;
        adapterDate = datetime;
    }

    @Override
    public void getTempState(double temperature) {
        if (temperature >= 60) {
            appBar.setBackgroundColor(getResources().getColor(R.color.weather_warm));
        } else if (temperature < 60) {
            appBar.setBackgroundColor(getResources().getColor(R.color.weather_cool));
        }
    }

    @Override
    public void getLastHour(int hour) {
        if (hour == 0) {
            todayCard.setVisibility(View.GONE);
        } else {
            todayCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void obtainDate(Date date1, Date date2, List<ForecastCondition> dataList) {

        weatherDataList2 = dataList;
        newAdapter = new WeatherAdapter2(MainActivity.this, weatherDataList2);
        tomRecyclerView.setAdapter(newAdapter);

        //weatherAdapter.removeItemsCount(0);
    }

    private void setUpDaggerUsersComponent() {
        DaggerWeatherComponent.builder()
                .appComponent(UmbrellaApp.getAppComponent())
                .weatherModule(new WeatherModule(this, getApplication()))
                .build()
                .inject(this);
    }
}