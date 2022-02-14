package com.scrappers.vitalwatch.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.core.AbstractScreen;
import com.scrappers.vitalwatch.core.RFCommSetup;
import com.scrappers.vitalwatch.core.tracker.RFCommTracker;
import com.scrappers.vitalwatch.data.UiModel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The portal to bluetooth connection initialization and to device pairing.
 * @author pavl_g.
 */
public class PairingScreen extends AbstractScreen implements View.OnClickListener, RFCommTracker, ActivityResultCallback<ActivityResult> {

    private final UiModel uiModel = new UiModel();
    private RFCommSetup rfCommSetup;
    private static final Logger logger = Logger.getLogger(PairingScreen.class.getName());
    /* the new API call */
    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pairing_screen;
    }

    @Override
    public int getAnimationId() {
        return R.anim.thow_up;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rfCommSetup = new RFCommSetup((ComponentActivity) getContext());
        rfCommSetup.setRfCommTracker(this);

        final TextView deviceData = view.findViewById(R.id.deviceData);
        final ImageView pairingButton = view.findViewById(R.id.pairingButton);
        final ImageView isConnected = view.findViewById(R.id.isConnected);
        final ImageView showPairedDevices = view.findViewById(R.id.pairedDevices);
        /* store ui states in a model object */
        uiModel.setDeviceData(deviceData);
        uiModel.setIsConnected(isConnected);
        uiModel.setPairingButton(pairingButton);

        pairingButton.setOnClickListener(this);
        showPairedDevices.setOnClickListener(this);
    }

    @Override
    public void onClick(View view ) {
        if (view.getId() == R.id.pairingButton) {
            if (rfCommSetup.getBluetoothState() == RFCommSetup.RFCommState.ON) {
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
        logger.log(Level.WARNING, "RFComm Destroyed !");
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            rfCommSetup.initialize(uiModel).connect(result.getData());
        }
    }
}
