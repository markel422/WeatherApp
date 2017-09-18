package com.foo.umbrella.WeatherMain;

import android.app.Application;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foo.umbrella.data.adapter.WeatherAdapter;
import com.foo.umbrella.data.model.ForecastCondition;

import java.util.List;

/**
 * Created by mike0 on 9/17/2017.
 */

public interface MainPresenter {

    void init();

    void getWeather();

    void getWeather(String zipcode, Toolbar myToolbar, List<ForecastCondition> weatherDataList, RelativeLayout appBar);

    Application getApplication();

}
