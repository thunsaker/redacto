package com.thunsaker.redacto.app;

import android.app.Application;

import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.CropActivity;
import com.thunsaker.redacto.MainActivity;
import com.thunsaker.redacto.RedactionsAdaptor;

import dagger.Component;

@PerApp
@Component(
        modules = {
                RedactoAppModule.class
        }
)
public interface RedactoAppComponent {
        void inject(MainActivity mainActivity);

        void inject(CropActivity cropActivity);

        void inject(RedactionsAdaptor redactionsAdaptor);

        Application application();

        Picasso picasso();
}
