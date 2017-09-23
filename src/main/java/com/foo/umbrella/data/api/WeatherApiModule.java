package com.foo.umbrella.data.api;

import com.foo.umbrella.di.scopes.AppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by mike0 on 9/19/2017.
 */
@Module
public class WeatherApiModule {

    @AppScope
    @Provides
    WeatherService provideWeatherService(Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }
}
