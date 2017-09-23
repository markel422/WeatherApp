package com.foo.umbrella.main;

import com.foo.umbrella.data.model.ForecastCondition;

import java.util.Date;
import java.util.List;

/**
 * Created by mike0 on 9/17/2017.
 */

public interface MainView {

    void showWeather(List<ForecastCondition> forecastList);

    void showError();

    void obtainWeather(String zipFullName, String observation, String weatherState,List<ForecastCondition> dataList, String datetime);

    void getTempState(double temperature);

    void obtainDate(Date date1, Date date2, List<ForecastCondition> dataList);

}
