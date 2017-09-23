package com.foo.umbrella.di;

import android.app.Application;
import android.content.Context;

import com.foo.umbrella.UmbrellaApp;
import com.foo.umbrella.di.scopes.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mike0 on 9/18/2017.
 */
@Module
public class AppModule {

    private UmbrellaApp application;

    public AppModule(UmbrellaApp application) {
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
