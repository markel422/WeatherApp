package com.foo.umbrella.data.app;

import com.foo.umbrella.WeatherMain.MainPresenter;
import com.foo.umbrella.WeatherMain.WeatherApiModule;
import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.api.WeatherService;

import dagger.Component;

/**
 * Created by mike0 on 9/18/2017.
 */
@AppScope
@Component(modules = {
        AppModule.class,
        ApiServicesProvider.class,
        WeatherApiModule.class})
public interface AppComponent {

    WeatherService weatherService();

}
