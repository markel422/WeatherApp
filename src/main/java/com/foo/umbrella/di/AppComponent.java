package com.foo.umbrella.di;

import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.api.WeatherService;
import com.foo.umbrella.di.scopes.AppScope;

import dagger.Component;

/**
 * Created by mike0 on 9/18/2017.
 */
@AppScope
@Component(modules = {
        AppModule.class,
        ApiServicesProvider.class})
public interface AppComponent {

    WeatherService weatherService();

}
