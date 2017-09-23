package com.foo.umbrella.main;

import com.foo.umbrella.di.AppComponent;

import dagger.Component;

/**
 * Created by mike0 on 9/18/2017.
 */
@WeatherScope
@Component(
        dependencies = {AppComponent.class},
        modules = {WeatherModule.class})
public interface WeatherComponent {

    void inject(MainActivity activity);

}
