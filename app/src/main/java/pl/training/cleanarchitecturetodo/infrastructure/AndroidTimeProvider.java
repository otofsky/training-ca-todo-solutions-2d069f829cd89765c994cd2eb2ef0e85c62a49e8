package pl.training.cleanarchitecturetodo.infrastructure;

import java.util.Date;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.domain.TimeProvider;

public class AndroidTimeProvider implements TimeProvider {

    @Inject
    public AndroidTimeProvider() {
    }

    @Override
    public Date getCurrentTime() {
        return new Date();
    }
}
