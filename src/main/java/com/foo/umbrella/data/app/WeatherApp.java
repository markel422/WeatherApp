package com.foo.umbrella.data.app;

import android.app.Application;

/**
 * Created by mike0 on 9/18/2017.
 */

public class WeatherApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
