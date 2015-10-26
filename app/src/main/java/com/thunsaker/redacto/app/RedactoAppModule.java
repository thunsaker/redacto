package com.thunsaker.redacto.app;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.BuildConfig;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class RedactoAppModule {
    final RedactoApp app;

    public RedactoAppModule(RedactoApp app) {
        this.app = app;
    }

    @Provides @PerApp RedactoApp provideRedactoApp() {
        return app;
    }

    @Provides @PerApp
    public Application provideApplication() {
        return app;
    }

    @Provides @PerApp
    Picasso providePicasso(Application app) {
        Picasso picasso = Picasso.with(app);
        picasso.setIndicatorsEnabled(BuildConfig.DEBUG);
        return picasso;
    }
}
