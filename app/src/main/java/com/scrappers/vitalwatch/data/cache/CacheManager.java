package com.scrappers.vitalwatch.data.cache;

import android.content.Context;
import androidx.annotation.NonNull;
import com.scrappers.vitalwatch.data.SensorDataModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages local database.
 *
 * @author pavl_g.
 */
public final class CacheManager {
    /**
     * An order based data writer.
     */
    public static final class DataWriter {
        private CacheManager.DataReader dataReader;
        private final SensorDataModel cachedData = new SensorDataModel();
        private final String path;
        private JSONObject database;
        private final JSONObject dataHolder = new JSONObject();
        private static final Object synchronizer = new Object();

        public DataWriter(final String path) {
            this.path = path;
        }

        /**
         * Initializes a Json database object.
         * @return this instance for chaining.
         */
        public final DataWriter initialize(final Context context) throws IOException, JSONException, InterruptedException {
            synchronized (synchronizer) {
                database = new JSONObject();
                try {
                    dataHolder.put("data", database);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final File pathFolder = new File(path);
                if (!pathFolder.exists()) {
                    new File(CacheStorage.getDirectory(context)).mkdirs();
                } else {
                    // fill old data model with read data
                    dataReader = new DataReader(path);
                    dataReader.read().fillSensorModel(cachedData);
                }
            }
            return this;
        }

        /**
         * Retrieves all the data from a sensor model state and saves it into database json object.
         * @param cache the sensor model.
         * @return this instance for chaining.
         * @throws JSONException if file not found or operation not supported.
         */
        public final DataWriter getSensorData(@NonNull final SensorDataModel cache) throws JSONException, InterruptedException {
            if (database == null) {
                throw new UnsupportedOperationException("Cannot operate on a null data reference !");
            }
            synchronized (synchronizer) {
                // device data
                final JSONObject deviceData = new JSONObject();
                // check if there is vial data
                if (cache.getDeviceName() != null && cache.getDeviceName().length() > 0) {
                    deviceData.put(CacheKeys.DEVICE_NAME.key, cache.getDeviceName());
                } else {
                    deviceData.put(CacheKeys.DEVICE_NAME.key, cachedData.getDeviceName());
                }

                if (cache.isConnected() != cachedData.isConnected()) {
                    deviceData.put(CacheKeys.DEVICE_STATE.key, cache.isConnected());
                } else {
                    deviceData.put(CacheKeys.DEVICE_STATE.key, cachedData.isConnected());
                }

                if (cache.getDeviceMacAddress() != null && cache.getDeviceMacAddress().length() > 0) {
                    deviceData.put(CacheKeys.DEVICE_MAC_ADDRESS.key, cache.getDeviceMacAddress());
                } else {
                    deviceData.put(CacheKeys.DEVICE_MAC_ADDRESS.key, cachedData.getDeviceMacAddress());
                }

                // user data
                final JSONObject userData = new JSONObject();
                if (cache.getUsername() != null && cache.getUsername().length() > 0) {
                    userData.put(CacheKeys.USER_NAME.key, cache.getUsername());
                } else {
                    userData.put(CacheKeys.USER_NAME.key, cachedData.getUsername());
                }

                if (cache.getUserAccount() != null && cache.getUserAccount().length() > 0) {
                    userData.put(CacheKeys.USER_ACC.key, cache.getUserAccount());
                } else {
                    userData.put(CacheKeys.USER_ACC.key, cachedData.getUserAccount());
                }

                if (cache.getUserPassword() != null && cache.getUserPassword().length() > 0) {
                    userData.put(CacheKeys.USER_PASSWORD.key, cache.getUserPassword());
                } else {
                    userData.put(CacheKeys.USER_PASSWORD.key, cachedData.getUserPassword());
                }

                // vital signs data inside user data
                final JSONObject vitalSigns = new JSONObject();
                if (cache.getBloodPressure() != null && cache.getBloodPressure().length() > 0) {
                    vitalSigns.put(CacheKeys.BLOOD_PRESSURE.key, cache.getBloodPressure());
                } else {
                    vitalSigns.put(CacheKeys.BLOOD_PRESSURE.key, cachedData.getBloodPressure());
                }

                if (cache.getHeartRate() != null && cache.getHeartRate().length() > 0) {
                    vitalSigns.put(CacheKeys.HEART_RATE.key, cache.getHeartRate());
                } else {
                    vitalSigns.put(CacheKeys.HEART_RATE.key, cachedData.getHeartRate());
                }

                if (cache.getTemperature() != null && cache.getTemperature().length() > 0) {
                    vitalSigns.put(CacheKeys.TEMPERATURE.key, cache.getTemperature());
                } else {
                    vitalSigns.put(CacheKeys.TEMPERATURE.key, cachedData.getTemperature());
                }

                // put vital data inside user data
                userData.put(CacheKeys.VITAL_DATA.key, vitalSigns);

                // now save these nodes in their respective parent nodes
                database.put(CacheKeys.DEVICE_DATA.key, deviceData);
                database.put(CacheKeys.USER_DATA.key, userData);
            }
            return this;
        }

        public DataReader getDataReader() {
            return dataReader;
        }

        /**
         * Writes all the data to the local cache.
         */
        public void write() throws IOException, JSONException {
            synchronized (synchronizer) {
                try (final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)))) {
                    writer.write(dataHolder.getString("data"));
                }
            }
        }
    }
    public static final class DataReader {
        private DataListener dataListener;
        private JSONObject database;
        private final String path;
        private final static Object synchronizer = new Object();

        public DataReader(final String path) {
            this.path = path;
        }

        public DataReader read() throws JSONException, IOException {
            synchronized (synchronizer) {
                try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    database = new JSONObject(reader.readLine());
                }
            }
            return this;
        }

        public void fillSensorModel(@NonNull final SensorDataModel cache) throws JSONException, InterruptedException {
            if (database == null) {
                throw new UnsupportedOperationException("Cannot operate on a non-initialized database !");
            }
            synchronized (synchronizer) {
                // fill this data model using its memory reference
                final JSONObject deviceData = database.getJSONObject(CacheKeys.DEVICE_DATA.key);
                final JSONObject userData = database.getJSONObject(CacheKeys.USER_DATA.key);
                // get vital data from user data
                final JSONObject vitalSigns = userData.getJSONObject(CacheKeys.VITAL_DATA.key);

                // set device data
                cache.setDeviceName(deviceData.getString(CacheKeys.DEVICE_NAME.key));
                cache.setConnected(deviceData.getBoolean(CacheKeys.DEVICE_STATE.key));
                cache.setDeviceMacAddress(deviceData.getString(CacheKeys.DEVICE_MAC_ADDRESS.key));

                // set user data
                cache.setUsername(userData.getString(CacheKeys.USER_NAME.key));
                cache.setUserAccount(userData.getString(CacheKeys.USER_ACC.key));
                cache.setUserPassword(userData.getString(CacheKeys.USER_PASSWORD.key));

                // set vital data
                cache.setBloodPressure(vitalSigns.getString(CacheKeys.BLOOD_PRESSURE.key));
                cache.setHeartRate(vitalSigns.getString(CacheKeys.HEART_RATE.key));
                cache.setTemperature(vitalSigns.getString(CacheKeys.TEMPERATURE.key));

                if (dataListener != null) {
                    dataListener.onReadCompleted(cache);
                }
            }
        }

        public void setDataListener(DataListener dataListener) {
            this.dataListener = dataListener;
        }
    }
}
