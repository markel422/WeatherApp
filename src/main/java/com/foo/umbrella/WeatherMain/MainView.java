package com.foo.umbrella.WeatherMain;

import com.foo.umbrella.data.model.ForecastCondition;

import java.util.List;

/**
 * Created by mike0 on 9/17/2017.
 */

public interface MainView {

    void showWeather(List<ForecastCondition> forecastList, String observation);

    void getWeather();

    void showError();

    void obtainWeather(String observation, String weatherState,List<ForecastCondition> dataList, String datetime);

}
