package com.thunsaker.redacto.app;

import android.app.Application;
import android.content.Context;

public class RedactoApp extends Application {
    private RedactoAppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        component = DaggerRedactoAppComponent.builder()
                .redactoAppModule(new RedactoAppModule(this))
                .build();
        component.inject(this);
    }

    public RedactoAppComponent component() {
        return component;
    }

    public static RedactoApp get(Context context) {
        return (RedactoApp) context.getApplicationContext();
    }
}
