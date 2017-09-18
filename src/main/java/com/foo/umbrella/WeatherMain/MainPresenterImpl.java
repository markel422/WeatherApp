package com.foo.umbrella.WeatherMain;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.umbrella.R;
import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.adapter.SettingsAdapter;
import com.foo.umbrella.data.adapter.WeatherAdapter;
import com.foo.umbrella.data.api.WeatherApiInteractor;
import com.foo.umbrella.data.app.AppScope;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;

import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import dagger.Provides;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;

/**
 * Created by mike0 on 9/17/2017.
 */

public class MainPresenterImpl implements MainPresenter, WeatherApiInteractor.OnWeatherResponseListener {

    MainView mainView;
    private Application application;
    @Inject
    WeatherApiInteractor interactor;

    ApiServicesProvider provider;

    SharedPreferences weatherPref;

    private boolean checkCelsius = false;

    @Inject
    public MainPresenterImpl(MainView mainView, Application application) {
        this.mainView = mainView;
        this.application = application;
    }

    @Override
    public void init() {
        interactor.setOnRandomResponseListener(this);
    }

    @Override
    public void getWeather(String zipcode, Toolbar myToolbar, List<ForecastCondition> weatherDataList, RelativeLayout appBar) {
        weatherPref = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        checkCelsius = SettingsAdapter.celsiusSelected();
        final String[] weatherTemperature = new String[1];
        final String[] weatherObservation = new String[1];
        final List<ForecastCondition>[] dataList = new List[1];
        Integer arrayCount = 0;
        provider.getWeatherService().forecastForZipCallable(zipcode).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    myToolbar.setTitle(response.body().getCurrentObservation().getDisplayLocation().getFullName());
                    if (checkCelsius == true || weatherPref.getString("unitsData", "") == "Celsius") {
                        weatherTemperature[0] = response.body().getCurrentObservation().getTempCelsius() + "\u00B0";
                    } else {
                        weatherTemperature[0] = response.body().getCurrentObservation().getTempFahrenheit() + "\u00B0";
                    }

                    weatherObservation[0] = response.body().getCurrentObservation().getIconName();
                    dataList[0] = response.body().getForecast();


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy");
                    String formatDateTime = response.body().getForecast().get(arrayCount).getDateTime().format(formatter);
                    String formatDateTime2;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
                    String adapterDate = formatDateTime;

                    mainView.obtainWeather(weatherTemperature[0], weatherObservation[0],dataList[0], formatDateTime);

                    for (int i = 0; i < weatherDataList.size(); i++) {
                        formatDateTime2 = response.body().getForecast().get(i).getDateTime().format(formatter);
                        /*try {
                            date2 = dateFormat.parse(formatDateTime2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/
                        //String newDate = dateFormat.format(date2);
                        //Log.d(TAG, "date2" +  date2);
                        Log.d(TAG, "adapterDate: " + adapterDate);
                    }

                    /*Log.d(TAG, "Current Time from Adapter: " +  adapterDate);
                    Log.d(TAG, "Current Time from Adapter in Date instance: " +  date2);*/

                    double result;
                    result = parseDouble(response.body().getCurrentObservation().getTempFahrenheit());

                    if (result > 60) {
                        appBar.setBackgroundColor(application.getResources().getColor(R.color.weather_warm));
                    } else if (result < 60) {
                        appBar.setBackgroundColor(application.getResources().getColor(R.color.weather_cool));
                    }
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

    @Override
    public void onWeatherResponseDone(List<ForecastCondition> results) {
        mainView.showWeather(results);
    }

    @Override
    public void onWeatherResponseError() {
        mainView.showError();
    }

    @AppScope
    @Provides
    public Application getApplication() {
        return application;
    }
}
