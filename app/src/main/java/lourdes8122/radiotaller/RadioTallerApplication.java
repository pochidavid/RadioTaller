package lourdes8122.radiotaller;

import android.app.Application;

import lourdes8122.radiotaller.id.DaggerDataComponent;
import lourdes8122.radiotaller.id.DataComponent;
import lourdes8122.radiotaller.id.DataModule;

public class RadioTallerApplication extends Application {
    private static RadioTallerApplication app;
    DataComponent dataComponent;

    public static RadioTallerApplication getApp(){
        return app;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        app = this;

        initDataComponent();

        dataComponent.inject(this);
    }

    private void initDataComponent() {
        dataComponent = DaggerDataComponent.builder()
                .dataModule(new DataModule(this))
                .build();
    }

    public DataComponent getDataComponent(){
        return dataComponent;
    }
}
