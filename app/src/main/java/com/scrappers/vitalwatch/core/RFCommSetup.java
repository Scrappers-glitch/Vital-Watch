package com.scrappers.vitalwatch.core;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import com.scrappers.vitalwatch.core.tracker.BluetoothStateTracker;
import com.scrappers.vitalwatch.core.tracker.RFCommTracker;
import com.scrappers.vitalwatch.data.UiModel;
import com.scrappers.vitalwatch.screen.DevicesScreen;

import org.json.JSONException;

import java.io.IOException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Setups an RFComm communication service.
 *
 * @author pavl_g.
 */
public class RFCommSetup {

    public enum RFCommState {
        ON, OFF
    }
    private final ComponentActivity context;
    private BluetoothSPP bluetoothSPP;
    private RFCommTracker rfCommTracker;
    protected RFCommState bluetoothState = RFCommState.OFF;

    public RFCommSetup(final ComponentActivity context) {
        this.context = context;
    }

    /**
     * Turns the bluetooth adapter on if it's off.
     * @return radio frequency instance.
     */
    public RFCommSetup prepare() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            context.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        } else {
            if (rfCommTracker != null) {
                rfCommTracker.onPrepare();
            }
        }
        return this;
    }
    /**
     * Initializes the bluetooth serial communication.
     * Must be called after {@link #prepare()} ()}.
     *
     * @throws UnsupportedOperationException if bluetooth isn't supported.
     * @return radio frequency instance.
     * @param uiModel a model of ui components.
     */
    public RFCommSetup initialize(final UiModel uiModel) throws JSONException, IOException, InterruptedException {
        bluetoothSPP = new BluetoothSPP(context);
        if (!bluetoothSPP.isBluetoothAvailable()) {
            throw new UnsupportedOperationException("Cannot use bluetooth to operate non-service devices !");
        }
        // setup
        bluetoothSPP.setupService();
        bluetoothSPP.startService(BluetoothState.DEVICE_ANDROID);
        /* set the state tracker */
        final BluetoothStateTracker bluetoothStateTracker = new BluetoothStateTracker(context, uiModel).setupCache().prepare();
        bluetoothSPP.setBluetoothConnectionListener(bluetoothStateTracker);
        /* setup the data listener, the data listener should then fill a data model */
        if (rfCommTracker != null) {
            rfCommTracker.onInitialize();
        }
        return this;
    }

    /**
     * Connects to a device after decoupling the data intent.
     *
     * @param data an intent holding the device's mac address.
     */
    public void connect(final Intent data) {
        bluetoothSPP.connect(data);
        if (rfCommTracker != null) {
            rfCommTracker.onConnectionPassed();
            setBluetoothState(RFCommState.ON);
        }
    }
    public void disconnect() {
        if (bluetoothSPP != null) {
            bluetoothSPP.disconnect();
            setBluetoothState(RFCommState.OFF);
        }
    }
     /**
     * Sets up the rfcomm tracker.
     *
     * @param rfCommTracker the tracker instance.
     */
    public void setRfCommTracker(RFCommTracker rfCommTracker) {
        this.rfCommTracker = rfCommTracker;
    }

    public void launchDevicesScreen(final ActivityResultLauncher<Intent> activityResultLauncher) {
        if (activityResultLauncher == null) {
            throw new IllegalStateException("Must register a callback inside onCreate()");
        }
        final Intent intent = new Intent(context, DevicesScreen.class);
        activityResultLauncher.launch(intent);
    }

    public BluetoothSPP getBluetoothSPP() {
        return bluetoothSPP;
    }

    public void setBluetoothState(RFCommState bluetoothState) {
        this.bluetoothState = bluetoothState;
    }

    public RFCommState getBluetoothState() {
        return bluetoothState;
    }
}
