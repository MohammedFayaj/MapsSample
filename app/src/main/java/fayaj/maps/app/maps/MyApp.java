package fayaj.maps.app.maps;

import android.app.Application;
import android.content.Context;

import fayaj.maps.app.maps.dagger.AppModule;
import fayaj.maps.app.maps.dagger.DaggerMyComponent;
import fayaj.maps.app.maps.dagger.MyComponent;
import fayaj.maps.app.maps.dagger.NetModule;


/**
 * Created by imran on 23/01/18.
 */

public class MyApp extends Application {

    MyComponent myComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        myComponent = DaggerMyComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule()).build();

    }

    public MyComponent getMyComponent(Context context) {
        return ((MyApp) context.getApplicationContext()).myComponent;


    }
}
