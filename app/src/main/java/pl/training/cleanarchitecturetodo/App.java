package pl.training.cleanarchitecturetodo;

import android.app.Application;
import android.content.Context;

import pl.training.cleanarchitecturetodo.di.AppComponent;
import pl.training.cleanarchitecturetodo.di.DaggerAppComponent;
import pl.training.cleanarchitecturetodo.infrastructure.AndroidModule;

public class App extends Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent
                .builder()
                .androidModule(new AndroidModule(this))
                .build();
    }

    public static AppComponent getComponent(Context context) {
        return ((App) context.getApplicationContext()).component;
    }

}
