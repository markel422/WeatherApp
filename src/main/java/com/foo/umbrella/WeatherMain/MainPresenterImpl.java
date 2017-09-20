package com.foo.umbrella.WeatherMain;

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
import com.foo.umbrella.data.api.WeatherApiInteractor;
import com.foo.umbrella.data.api.WeatherService;
import com.foo.umbrella.data.app.UmbrellaApp;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;

import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

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



    SharedPreferences weatherPref;

    String zipcode;
    Toolbar myToolbar;
    List<ForecastCondition> weatherDataList;
    RelativeLayout appBar;

    private boolean checkCelsius = false;

    @Inject
    public MainPresenterImpl(MainView mainView, Application application) {
        this.mainView = mainView;
        this.application = application;
    }

    @Override
    public void init() {
        interactor.setOnWeatherResponseListener(this, application);
    }

    @Override
    public void getWeather(String zipcode, Toolbar myToolbar, List<ForecastCondition> weatherDataList, RelativeLayout appBar) {
        interactor.getWeather(zipcode, myToolbar, weatherDataList, appBar);
    }

    @Override
    public void obtainWeather(String observation, String weatherState, List<ForecastCondition> dataList, String datetime) {
        mainView.obtainWeather(observation, weatherState, dataList, datetime);
    }

    @Override
    public void getWeather(String zipcode) {

    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void onWeatherResponseDone(List<ForecastCondition> results) {
        mainView.showWeather(results);
    }

    @Override
    public void onWeatherResponseError() {
        mainView.showError();
    }
}
