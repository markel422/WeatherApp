package com.foo.umbrella.WeatherMain;

import android.app.Application;

import com.foo.umbrella.data.app.AppModule;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * Created by mike0 on 9/18/2017.
 */

@Module
public class WeatherModule {

    private final MainView mainView;
    private final Application application;

    public WeatherModule(MainView mainView, Application application) {
        this.mainView = mainView;
        this.application = application;
    }

    @WeatherScope
    @Provides
    Application provideApplication() {
        return application;
    }

    @WeatherScope
    @Provides
    MainView provideUsersView() {
        return mainView;
    }

    @WeatherScope
    @Provides
    public MainPresenter providesMainPresenter() {
        return new MainPresenterImpl(mainView, application);
    }
}
