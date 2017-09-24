package com.foo.umbrella.main;

import android.app.Application;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.foo.umbrella.data.model.ForecastCondition;

import java.util.Date;
import java.util.List;

/**
 * Created by mike0 on 9/17/2017.
 */

public interface MainPresenter {

    void init();

    void getWeather(String zipcode);

    void getDate(Date date1, Date date2, List<ForecastCondition> dataList);

    void getLastHour(int hour);

    Application getApplication();

}
