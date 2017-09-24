package com.foo.umbrella.main;

import android.app.Application;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.foo.umbrella.data.api.WeatherApiInteractor;
import com.foo.umbrella.data.model.ForecastCondition;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by mike0 on 9/17/2017.
 */

public class MainPresenterImpl implements MainPresenter, WeatherApiInteractor.OnWeatherResponseListener {

    MainView mainView;
    private Application application;

    List<ForecastCondition> dataList;

    @Inject
    WeatherApiInteractor interactor;

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
    public void getWeather(String zipcode) {
        interactor.getWeather(zipcode);
    }

    @Override
    public void getTempState(double temperature) {
        mainView.getTempState(temperature);
    }


    @Override
    public void obtainWeather(String zipFullName, String observation, String weatherState, List<ForecastCondition> dataList, String datetime) {
        mainView.obtainWeather(zipFullName ,observation, weatherState, dataList, datetime);
    }

    @Override
    public void getDate(Date date1, Date date2, List<ForecastCondition> dataList) {
        interactor.getDate(date1, date2, dataList);
    }

    @Override
    public void getLastHour(int hour) {
        mainView.getLastHour(hour);
    }

    @Override
    public void obtainDate(Date date1, Date date2, List<ForecastCondition> dataList) {
        mainView.obtainDate(date1, date2, dataList);
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
