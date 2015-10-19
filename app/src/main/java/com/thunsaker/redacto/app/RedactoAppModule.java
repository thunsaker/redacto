package com.thunsaker.redacto.app;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class RedactoAppModule {
    private RedactoApp app;

    public RedactoAppModule(RedactoApp app) {
        this.app = app;
    }

    @Provides
    public Application provideApplication() {
        return app;
    }
}
