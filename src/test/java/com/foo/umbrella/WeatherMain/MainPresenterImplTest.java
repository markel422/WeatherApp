package com.foo.umbrella.WeatherMain;

import android.app.Application;
import android.support.v7.widget.Toolbar;

import com.foo.umbrella.data.api.WeatherApiInteractor;
import com.foo.umbrella.data.model.CurrentObservation;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;

import com.foo.umbrella.data.api.WeatherApiInteractor.OnWeatherResponseListener;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mike0 on 9/18/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterImplTest {

    Application application;

    @Mock
    MainPresenter presenter;

    @Mock
    MainView mainView;

    @Mock
    WeatherApiInteractor interactor;

    private List<ForecastCondition> weatherDataList;
    private ArgumentCaptor<OnWeatherResponseListener> listener;

    @Before
    public void setUpUsersPresenterTest() {
        MockitoAnnotations.initMocks(this);
        List<WeatherData> list;
        listener = ArgumentCaptor.forClass(OnWeatherResponseListener.class);
        presenter.init();
    }

    @Test
    public void getApplication_shouldGetApplication() {
        when(presenter.getApplication()).thenReturn(application);
    }
}