package com.scrappers.vitalwatch.core.tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.data.cache.CacheManager;
import com.scrappers.vitalwatch.data.SensorDataModel;
import com.scrappers.vitalwatch.data.UiModel;
import com.scrappers.vitalwatch.data.cache.CacheStorage;
import com.scrappers.vitalwatch.screen.PairingScreen;
import org.json.JSONException;
import java.io.IOException;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Alters the stateful ui components for {@link PairingScreen}.
 *
 * @author pavl_g.
 */
public class BluetoothStateTracker implements BluetoothSPP.BluetoothConnectionListener {

    private final UiModel uiModel;
    private final Context context;
    private TextView deviceData;
    private TextView deviceAddress;
    private ImageView isConnected;
    private CacheManager.DataWriter cacheWriter;
    private CacheManager.DataReader cacheReader;
    private final SensorDataModel sensorDataModel = new SensorDataModel();

    public BluetoothStateTracker(final Context context, final UiModel uiModel) {
        this.context = context;
        this.uiModel = uiModel;
    }

    public BluetoothStateTracker setupCache() throws JSONException, IOException, InterruptedException {
        final String path = CacheStorage.getCacheStorage(context);
        cacheWriter = new CacheManager.DataWriter(path).initialize(context);
        cacheReader = new CacheManager.DataReader(path);
        cacheReader.read().fillSensorModel(sensorDataModel);
        return this;
    }

    public BluetoothStateTracker prepare() {
        deviceData = uiModel.getDeviceData();
        isConnected = uiModel.getIsConnected();
        deviceAddress = uiModel.getDeviceAddress();
        return this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDeviceConnected(String name, String address) {
        deviceData.setText(name);
        deviceAddress.setText(address);
        isConnected.setImageDrawable(ContextCompat.getDrawable(deviceData.getContext(), R.drawable.ic_baseline_bluetooth_connected_24));
        isConnected.setImageTintList(ColorStateList.valueOf(Color.GREEN));

        // put data into data writer and write the new data only
        sensorDataModel.setDeviceName(name);
        sensorDataModel.setDeviceMacAddress(address);
        sensorDataModel.setConnected(true);
        StateControl.setBluetoothState(StateControl.BluetoothState.CONNECTED);
        try {
            cacheWriter.initialize(context).getSensorData(sensorDataModel).write();
        } catch (IOException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDeviceDisconnected() {
        deviceData.setText("Disconnected");
        deviceAddress.setText("xxxx:xxxx");
        isConnected.setImageDrawable(ContextCompat.getDrawable(deviceData.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
        isConnected.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        sensorDataModel.setDeviceName("No Device");
        sensorDataModel.setDeviceMacAddress("xxxx:xxxx");
        sensorDataModel.setConnected(false);
        StateControl.setBluetoothState(StateControl.BluetoothState.DISCONNECTED);
        try {
            cacheWriter.initialize(context).getSensorData(sensorDataModel).write();
        } catch (IOException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDeviceConnectionFailed() {
        deviceData.setText("Cannot Connect");
        isConnected.setImageDrawable(ContextCompat.getDrawable(deviceData.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
        isConnected.setImageTintList(ColorStateList.valueOf(Color.RED));

        sensorDataModel.setDeviceName("No Devices");
        sensorDataModel.setDeviceMacAddress("xxxx:xxxx");
        sensorDataModel.setConnected(false);
        StateControl.setBluetoothState(StateControl.BluetoothState.DISCONNECTED);
        try {
            cacheWriter.initialize(context).getSensorData(sensorDataModel).write();
        } catch (IOException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CacheManager.DataWriter getCacheWriter() {
        return cacheWriter;
    }

    public CacheManager.DataReader getCacheReader() {
        return cacheReader;
    }
}
