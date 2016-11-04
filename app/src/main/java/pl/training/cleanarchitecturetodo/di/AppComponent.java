package pl.training.cleanarchitecturetodo.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.training.cleanarchitecturetodo.api.ApiModule;
import pl.training.cleanarchitecturetodo.domain.DomainModule;
import pl.training.cleanarchitecturetodo.infrastructure.AndroidModule;
import pl.training.cleanarchitecturetodo.infrastructure.InfrastructureModule;
import pl.training.cleanarchitecturetodo.persistance.PersistanceModule;
import pl.training.cleanarchitecturetodo.ui.WallActivity;

@Component(modules = {
        AndroidModule.class,
        InfrastructureModule.class,
        ApiModule.class,
        DomainModule.class,
        PersistanceModule.class
})
@Singleton
public interface AppComponent {
    void inject(WallActivity wallActivity);
}
