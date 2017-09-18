package com.foo.umbrella.data.app;

import android.app.Application;
import android.content.Context;

import com.foo.umbrella.WeatherMain.WeatherModule;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * Created by mike0 on 9/18/2017.
 */
@Module
@Component(modules = {WeatherModule.class})
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @AppScope
    @Provides
    Context provideAppContext() {
        return application;
    }

    public Application getApplication() {
        return application;
    }
}
