package dzejkobdevelopment.pl.fifamaster;

import android.app.Application;
import android.content.res.Configuration;

import java.util.UUID;

/**
 * Created by Dzejkob on 21.09.2017.
 */

public class FifaMaster extends Application {
    private DatabaseAdapter db;
    private DeviceUuidFactory deviceUuidFactory;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new DatabaseAdapter(getApplicationContext());
        deviceUuidFactory = new DeviceUuidFactory(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public DatabaseAdapter getDb(){
        return db;
    }

    public UUID getDeviceUuid(){
        return deviceUuidFactory.getDeviceUuid();
    }
}
