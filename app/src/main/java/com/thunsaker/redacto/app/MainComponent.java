package com.thunsaker.redacto.app;

import com.thunsaker.redacto.MainActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = RedactoAppComponent.class
)
public interface MainComponent {
    void inject(MainActivity activity);
}
