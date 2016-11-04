package pl.training.cleanarchitecturetodo.api;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import pl.training.cleanarchitecturetodo.application.RemoteClientFacade;

@Module
public abstract class ApiModule {

    @Binds
    public abstract RemoteClientFacade remoteClientFacade(RemoteClientFacadeImpl i);

    @Provides
    @Singleton
    public static WallAPI wallAPI() {
        return ApiFactory.createApi();
    }
}
