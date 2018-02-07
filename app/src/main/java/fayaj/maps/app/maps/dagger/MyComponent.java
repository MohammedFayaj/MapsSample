package fayaj.maps.app.maps.dagger;

import dagger.Component;
import fayaj.maps.app.maps.search.SearchLocationActivity;

@PerApplication
@Component(modules = {AppModule.class,NetModule.class})

public interface MyComponent {
    void injectMe(SearchLocationActivity searchLocationActivity);
}