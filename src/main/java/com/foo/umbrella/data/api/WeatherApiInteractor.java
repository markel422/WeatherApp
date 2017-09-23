package com.foo.umbrella.data.api;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foo.umbrella.R;
import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.adapter.SettingsAdapter;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;

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

import static java.lang.Double.parseDouble;

/**
 * Created by mike0 on 9/18/2017.
 */

public class WeatherApiInteractor {

    private static final String TAG = WeatherApiInteractor.class.getSimpleName() + "_TAG";

    Application application;

    private WeatherService weatherServiceService;
    private OnWeatherResponseListener listener;

    ApiServicesProvider provider;

    SharedPreferences weatherPref;

    private boolean checkCelsius = false;
    private String formedDate;
    private Date date1, date2;

    private List<ForecastCondition> weatherData = new ArrayList<ForecastCondition>();

    private List<ForecastCondition> dataList, dataList2;

    public interface OnWeatherResponseListener {
        void onWeatherResponseDone(List<ForecastCondition> results);

        void onWeatherResponseError();

        void obtainWeather(String zipFullName,String observation, String weatherState, List<ForecastCondition> dataList, String datetime);

        void getTempState(double temperature);

        void obtainDate(Date date1, Date date2, List<ForecastCondition> dataList);
    }

    @Inject
    public WeatherApiInteractor(WeatherService weatherServiceService) {
        this.weatherServiceService = weatherServiceService;
    }

    public void setOnWeatherResponseListener(OnWeatherResponseListener listener, Application application) {
        this.listener = listener;
        this.application = application;
    }

    public void getWeather(String zipcode) {
        provider = new ApiServicesProvider(application);
        weatherPref = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        checkCelsius = SettingsAdapter.celsiusSelected();
        final String[] zipName = new String[1];
        final String[] weatherTemperature = new String[1];
        final String[] weatherObservation = new String[1];
        provider.provideWeatherService().forecastForZipCallable(zipcode).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    zipName[0] = response.body().getCurrentObservation().getDisplayLocation().getFullName();
                    if (checkCelsius == true || weatherPref.getString("unitsData", "") == "Celsius") {
                        weatherTemperature[0] = response.body().getCurrentObservation().getTempCelsius() + "\u00B0";
                    } else {
                        weatherTemperature[0] = response.body().getCurrentObservation().getTempFahrenheit() + "\u00B0";
                    }

                    weatherData.addAll(response.body().getForecast());

                    weatherObservation[0] = response.body().getCurrentObservation().getIconName();


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy");
                    String formatDateTime = response.body().getForecast().get(0).getDateTime().format(formatter);
                    String formatDateTime2;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");

                    Date currentTime = Calendar.getInstance().getTime();
                    formedDate = dateFormat.format(currentTime);

                    try {
                        date1 = dateFormat.parse(formedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Current Time: " + formedDate);
                    Log.d(TAG, "date1: " + date1);

                    boolean checkCurrentDate = false;
                    int currentDateIndex = 0;
                    boolean checkNewDate = false;
                    int newDateIndex = 0;
                    int subtractDate = 12;

                    for (int i = 0; i < weatherData.size(); i++) {
                        formatDateTime2 = weatherData.get(i).getDateTime().format(formatter);
                        try {
                            date2 = dateFormat.parse(formatDateTime2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //String newDateIndex = dateFormat.format(date2);
                        Log.d(TAG, "date2: " + date2);
                        //Log.d(TAG, "adapterDate: " + adapterDate);

                        if (date1.before(date2)) {
                            if (checkCurrentDate == false) {
                                checkCurrentDate = true;
                                currentDateIndex = i;
                            }
                            dataList = weatherData.subList(0, currentDateIndex);
                            listener.onWeatherResponseDone(dataList);
                        }
                        listener.obtainWeather(zipName[0], weatherTemperature[0], weatherObservation[0], dataList, formatDateTime);

                        if (date2.after(date1)) {
                            if (checkNewDate == false) {
                                checkNewDate = true;
                                newDateIndex = i;
                                subtractDate = subtractDate - newDateIndex;
                            }
                            //Log.d(TAG, "newDateIndex: " + newDateIndex);
                            dataList2 = weatherData.subList(newDateIndex, weatherData.size() - subtractDate);
                            getDate(date1, date2, dataList2);
                        }
                    }

                    double result;
                    result = parseDouble(response.body().getCurrentObservation().getTempFahrenheit());

                    listener.getTempState(result);

                } else {
                    Toast.makeText(application.getApplicationContext(), "API Error: ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void getDate(Date date1, Date date2, List<ForecastCondition> dataList) {
        listener.obtainDate(date1, date2, dataList);
    }
}
