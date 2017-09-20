package com.foo.umbrella.data.app;

import android.app.Application;

import com.foo.umbrella.data.ApiServicesProvider;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class UmbrellaApp extends Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiServicesProvider(new ApiServicesProvider(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
