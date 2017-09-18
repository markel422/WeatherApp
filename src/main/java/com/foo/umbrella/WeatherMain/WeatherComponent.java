package com.foo.umbrella.WeatherMain;

import com.foo.umbrella.data.app.AppComponent;

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
