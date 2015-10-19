package com.thunsaker.redacto.app;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                RedactoAppModule.class
        }
)
public interface RedactoAppComponent {
        void inject(RedactoApp app);

        // Interactors and Managers
}
