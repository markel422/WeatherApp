package com.foo.umbrella.WeatherMain;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.adapter.SettingsAdapter;
import com.foo.umbrella.data.adapter.WeatherAdapter;
import com.foo.umbrella.data.app.AppModule;
import com.foo.umbrella.data.app.UmbrellaApp;
import com.foo.umbrella.data.model.CurrentObservation;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;
import com.foo.umbrella.WeatherSettings.SettingsActivity;

import org.threeten.bp.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foo.umbrella.R.id.appbar_layout;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";

    SharedPreferences weatherPref;

    RelativeLayout appBar;

    Toolbar myToolbar;

    TextView currentWeatherTV;
    TextView skyTV;

    String currentObservation;
    List<ForecastCondition> weatherDataList;
    List<ForecastCondition> weatherDataList2;

    RecyclerView recyclerView;
    RecyclerView tomRecyclerView;
    WeatherAdapter weatherAdapter;

    GridLayoutManager layoutManager;
    GridLayoutManager layoutManager2;

    @Inject
    MainPresenterImpl presenter;

    String zipcode;

    String formedDate;
    String adapterDate;

    Date date1;
    Date date2;

    boolean checkCelsius = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBar = (RelativeLayout) findViewById(appbar_layout);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        currentWeatherTV = (TextView) findViewById(R.id.current_weather_tv);
        skyTV = (TextView) findViewById(R.id.current_sky_tv);

        getInitialWeatherData();

        setUpRecyclerView();
        setUpDaggerUsersComponent();

        weatherDataList = new ArrayList<>(0);

        presenter.init();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
        formedDate = dateFormat.format(currentTime);

        try {
            date1 = dateFormat.parse(formedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        showWeather(weatherDataList, currentObservation);
        getWeather();

        /*Log.d(TAG, "Current Time: " +  formedDate);
        Log.d(TAG, "Current Time in Date instance: " +  date1);*/
    }

    public void getInitialWeatherData() {
        weatherPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String newValue = weatherPref.getString("zipcodeData", "");

        if (zipcode == null && newValue == "") {
            getInitialZipcode();
        } else if (zipcode == null && newValue != "") {
            zipcode = newValue;
            Log.d(TAG, "onCreate: " + zipcode);
        }

        Log.d(TAG, "Zipcode in MainActivity: " + zipcode);
    }

    public void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.weather_today_recycler_view);

        layoutManager = new GridLayoutManager(MainActivity.this, 4);

        recyclerView.setLayoutManager(layoutManager);
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
                if (checkCelsius == true) {
                    weatherPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = weatherPref.edit();
                    editor.putString("unitsData", "Celsius");
                    editor.apply();
                } else {
                    weatherPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
        dialog.setTitle("Enter A Zipcode");
        dialog.setContentView(R.layout.dialog_zipcode);
        // Get the layout inflater
        dialog.show();

        final EditText zipcodeTV = (EditText) dialog.findViewById(R.id.zipcode_tv);
        Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
        Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipcode = zipcodeTV.getText().toString();
                Toast.makeText(MainActivity.this, "Zipcode Changed", Toast.LENGTH_SHORT).show();

                weatherPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = weatherPref.edit();

                editor.putString("zipcodeData", zipcode);
                editor.apply();

                Log.d(TAG, "Zipcode in Dialog: " + zipcode);
                Log.d(TAG, "Zipcode in Shared Preferences within Dialog: " + weatherPref.getString("zipcodeData", ""));
                getWeather();
                //getWeatherData(zipcode);
                dialog.cancel();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void showError() {
        Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWeather(List<ForecastCondition> forecastList, String observation) {
        weatherAdapter.updateDataSet(forecastList, observation);
    }

    @Override
    public void getWeather() {
        presenter.getWeather(zipcode, myToolbar, weatherDataList, appBar);

    }

    @Override
    public void obtainWeather(String observation, String state, List<ForecastCondition> dataList, String datetime) {
        currentWeatherTV.setText(observation);
        skyTV.setText(state);
        weatherDataList = dataList;
        adapterDate = datetime;
    }

    private void setUpDaggerUsersComponent() {
        DaggerWeatherComponent.builder()
                .appComponent(UmbrellaApp.getAppComponent())
                .weatherModule(new WeatherModule(this, getApplication()))
                .build()
                .inject(this);
    }

    /*private void getWeatherData(String zipCode) {
        checkCelsius = SettingsAdapter.celsiusSelected();
        Integer arrayCount = 0;
        provider.provideWeatherService().forecastForZipCallable(zipCode).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    getSupportActionBar().setTitle(response.body().getCurrentObservation().getDisplayLocation().getFullName());
                    if (checkCelsius == true || weatherPref.getString("unitsData", "") == "Celsius") {
                        currentWeatherTV.setText(response.body().getCurrentObservation().getTempCelsius() + "\u00B0");
                    } else {
                        currentWeatherTV.setText(response.body().getCurrentObservation().getTempFahrenheit() + "\u00B0");
                    }

                    currentObservation = response.body().getCurrentObservation().getIconName();
                    skyTV.setText(currentObservation);
                    weatherDataList = response.body().getForecast();
                    weatherAdapter.updateDataSet(weatherDataList, currentObservation);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy");
                    String formatDateTime = response.body().getForecast().get(arrayCount).getDateTime().format(formatter);
                    String formatDateTime2;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
                    adapterDate = formatDateTime;

                    for (int i = 0; i < weatherDataList.size(); i++) {
                        formatDateTime2 = response.body().getForecast().get(i).getDateTime().format(formatter);
                        try {
                            date2 = dateFormat.parse(formatDateTime2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        *//*String newDate = dateFormat.format(date2);
                        Log.d(TAG, "date2" +  date2);
                        Log.d(TAG, "adapterDate: " + newDate);*//*
                    }

                    *//*Log.d(TAG, "Current Time from Adapter: " +  adapterDate);
                    Log.d(TAG, "Current Time from Adapter in Date instance: " +  date2);*//*

                    double result;
                    result = parseDouble(response.body().getCurrentObservation().getTempFahrenheit());

                    if (result > 60) {
                        appBar.setBackgroundColor(getResources().getColor(R.color.weather_warm));
                    } else if (result < 60) {
                        appBar.setBackgroundColor(getResources().getColor(R.color.weather_cool));
                    }
                } else {
                    Toast.makeText(MainActivity.this, "API Error: ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }*/
}