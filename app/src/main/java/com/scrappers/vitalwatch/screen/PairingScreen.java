package com.scrappers.vitalwatch.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.core.AbstractScreen;
import com.scrappers.vitalwatch.core.RFCommSetup;
import com.scrappers.vitalwatch.core.tracker.RFCommTracker;
import com.scrappers.vitalwatch.core.tracker.StateControl;
import com.scrappers.vitalwatch.data.SensorDataModel;
import com.scrappers.vitalwatch.data.UiModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The portal to bluetooth connection initialization and to device pairing.
 * @author pavl_g.
 */
public class PairingScreen extends AbstractScreen implements View.OnClickListener, RFCommTracker, ActivityResultCallback<ActivityResult> {

    private final UiModel uiModel = new UiModel();
    private RFCommSetup rfCommSetup;
    private TextView deviceName;
    private TextView macAddress;
    private ImageView isConnected;
    private static final Logger logger = Logger.getLogger(PairingScreen.class.getName());
    /* the new API call */
    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);

    public PairingScreen(final RFCommSetup rfCommSetup) {
        super(rfCommSetup);
        this.rfCommSetup = rfCommSetup;
        rfCommSetup.setRfCommTracker(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pairing_screen;
    }

    @Override
    public int getAnimationId() {
        return R.anim.thow_up;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        deviceName = view.findViewById(R.id.deviceName);
        macAddress = view.findViewById(R.id.deviceMac);

        final ImageView pairingButton = view.findViewById(R.id.pairingButton);
        isConnected = view.findViewById(R.id.isConnected);
        final ImageView showPairedDevices = view.findViewById(R.id.pairedDevices);
        pairingButton.setOnClickListener(this);
        showPairedDevices.setOnClickListener(this);
        /* store ui states in a model object */
        uiModel.setDeviceData(deviceName);
        uiModel.setIsConnected(isConnected);
        uiModel.setPairingButton(pairingButton);
        uiModel.setDeviceAddress(macAddress);
        try {
            rfCommSetup.setupRFCommTracker(uiModel);
        } catch (InterruptedException | IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadCompleted(SensorDataModel cacheModel) {
        super.onReadCompleted(cacheModel);
        deviceName.setText(cacheModel.getDeviceName());
        macAddress.setText(cacheModel.getDeviceMacAddress());
        if (StateControl.getBluetoothState() == StateControl.BluetoothState.CONNECTED) {
            isConnected.setImageDrawable(ContextCompat.getDrawable(deviceName.getContext(), R.drawable.ic_baseline_bluetooth_connected_24));
        } else {
            isConnected.setImageDrawable(ContextCompat.getDrawable(deviceName.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
        }
    }

    @Override
    public void onClick(View view ) {
        if (view.getId() == R.id.pairingButton) {
            if (StateControl.getBluetoothState() == StateControl.BluetoothState.CONNECTED) {
                rfCommSetup.disconnect();
            } else {
                rfCommSetup.prepare();
            }
        } else if (view.getId() == R.id.pairedDevices) {
            //TODO
            Toast.makeText(view.getContext(), "This feature isn't available yet !", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitialize() {
        logger.log(Level.WARNING, "Bluetooth initialized !");
    }

    @Override
    public void onPrepare() {
        logger.log(Level.WARNING, "RFComm Ready to pair !");
        rfCommSetup.launchDevicesScreen(launcher);
    }

    @Override
    public void onConnectionPassed() {
        logger.log(Level.WARNING, "RFComm trying to connect !");
    }

    @Override
    public void onDestroyed() {
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            rfCommSetup.connect(result.getData());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.log(Level.WARNING, "PairingScreen goes to idle mode !");
    }
}
