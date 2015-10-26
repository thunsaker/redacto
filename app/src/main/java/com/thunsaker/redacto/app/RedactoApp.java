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
    }

    public static RedactoAppComponent getComponent(Context context) {
        return ((RedactoApp) context.getApplicationContext()).component;
    }
}
