package com.foo.umbrella.main;

import android.app.Application;
import android.app.Instrumentation;

import com.foo.umbrella.UmbrellaApp;
import com.foo.umbrella.data.api.WeatherApiInteractor;
import com.foo.umbrella.data.api.WeatherService;
import com.foo.umbrella.data.model.CurrentObservation;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import com.foo.umbrella.data.api.WeatherApiInteractor.OnWeatherResponseListener;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mike0 on 9/18/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterImplTest {

    @InjectMocks
    MainPresenterImpl presenter;

    @Mock
    Application application;

    @Mock
    MainView mainView;

    @Mock
    WeatherApiInteractor interactor;

    @Mock
    WeatherApiInteractor.OnWeatherResponseListener listener;

    private List<ForecastCondition> arrayLists;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        arrayLists = new ArrayList<>(0);
    }

    @Test
    public void getWeather_shouldGetWeather() {
        String zip = "90002";

        presenter.getWeather(zip);
        verify(interactor).getWeather(zip);
    }

    @Test
    public void getDate_shouldObtainDate() {
        Date date1 = Calendar.getInstance().getTime();
        Date date2 = Calendar.getInstance().getTime();

        presenter.getDate(date1, date2, arrayLists);
        verify(interactor).getDate(date1, date2, arrayLists);

        listener.obtainDate(date1, date2, arrayLists);
        presenter.obtainDate(date1, date2, arrayLists);
        verify(mainView).obtainDate(date1, date2, arrayLists);
    }
}