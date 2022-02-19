package com.scrappers.vitalwatch.core;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
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
@SuppressLint("ParcelCreator")
public class RFCommSetup implements Parcelable {
    private final ComponentActivity context;
    private BluetoothSPP bluetoothSPP;
    private RFCommTracker rfCommTracker;

    public RFCommSetup(final ComponentActivity context) {
        this.context = context;
    }

    public RFCommSetup createNewSSPInstance() {
        bluetoothSPP = new BluetoothSPP(context);
        return this;
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
     * @return radio frequency instance.
     */
    public RFCommSetup startBluetoothService() {
        if (bluetoothSPP.isBluetoothEnabled()) {
            // setup service protocol -- bluetooth socket
            bluetoothSPP.setupService();
            bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
            /* setup the data listener, the data listener should then fill a data model */
            if (rfCommTracker != null) {
                rfCommTracker.onInitialize();
            }
        }
        return this;
    }
    public void setupRFCommTracker(final UiModel uiModel) throws InterruptedException, IOException, JSONException {
        /* set the state tracker */
        final BluetoothStateTracker bluetoothStateTracker = new BluetoothStateTracker(context, uiModel).setupCache().prepare();
        bluetoothSPP.setBluetoothConnectionListener(bluetoothStateTracker);
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
        }
    }

    /**
     * Disconnects the device.
     */
    public void disconnect() {
        if (bluetoothSPP != null) {
            bluetoothSPP.disconnect();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
