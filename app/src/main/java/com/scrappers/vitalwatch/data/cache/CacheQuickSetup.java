package com.scrappers.vitalwatch.data.cache;

import android.content.Context;
import androidx.annotation.NonNull;
import com.scrappers.vitalwatch.core.ThreadDispatcher;
import com.scrappers.vitalwatch.data.SensorDataModel;
import org.json.JSONException;
import java.io.IOException;

/**
 * Quickly initializes cache files.
 *
 * @apiNote the call to this utility is optional.
 * @author pavl_g.
 */
public class CacheQuickSetup {

    private static final Object synchronizer = new Object();

    /**
     * Shortcut to data write to cache.
     *
     * @apiNote overrides the current device state cache data keeping the other data.
     * @param context the app context
     * @param sensorDataModel the data model to write.
     */
    public static void write(@NonNull final Context context, @NonNull final SensorDataModel sensorDataModel) {
        ThreadDispatcher.initializeThreadPool().dispatch(()->{
            // create a dummy local cache
            final String path = CacheStorage.getCacheStorage(context);
            final CacheManager.DataWriter dataWriter = new CacheManager.DataWriter(path);

            // fill empty data
            sensorDataModel.setDeviceName("No Device");
            sensorDataModel.setDeviceMacAddress("xxxx:xxxx");
            sensorDataModel.setConnected(false);

            // never override the data that already exists
            if (dataWriter.getDataReader() != null) {
                sensorDataModel.setUsername("Admin");
                sensorDataModel.setUserAccount("Admin@VitalWatch.com");
                sensorDataModel.setUserPassword("******");

                sensorDataModel.setTemperature("No Data");
                sensorDataModel.setBloodPressure("No Data");
                sensorDataModel.setHeartRate("No Data");
            }
            try {
                dataWriter.initialize(context).getSensorData(sensorDataModel).write();
            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Initializes a cache reader object and returns its data model.
     * @param context the app context.
     * @return a data model filled with the old cache as a result of read().
     */
    public static SensorDataModel read(final Context context) {
        final SensorDataModel sensorDataModel = new SensorDataModel();
        final String path = CacheStorage.getCacheStorage(context);
        final CacheManager.DataReader dataReader = new CacheManager.DataReader(path);
        synchronized (synchronizer) {
            try {
                dataReader.read().fillSensorModel(sensorDataModel);
            } catch (JSONException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        synchronized (synchronizer) {
            return sensorDataModel;
        }
    }
}
