package com.foo.umbrella.WeatherMain;

import com.foo.umbrella.data.app.AppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by mike0 on 9/18/2017.
 */
@Module
public class WeatherApiModule {

    @AppScope
    @Provides
    WeatherApiModule provideUserApiService(Retrofit retrofit) {
        return retrofit.create(WeatherApiModule.class);
    }
}
