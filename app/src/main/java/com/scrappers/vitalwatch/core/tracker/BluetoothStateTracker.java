package com.scrappers.vitalwatch.core.tracker;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.data.UiModel;
import com.scrappers.vitalwatch.screen.PairingScreen;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Alters the stateful ui components for {@link PairingScreen}.
 *
 * @author pavl_g.
 */
public class BluetoothStateTracker implements BluetoothSPP.BluetoothConnectionListener {

    private final UiModel uiModel;
    private TextView deviceData;
    private ImageView isConnected;

    public BluetoothStateTracker(final UiModel uiModel) {
        this.uiModel = uiModel;
    }

    public BluetoothStateTracker prepare() {
        deviceData = uiModel.getDeviceData();
        isConnected = uiModel.getIsConnected();
        return this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDeviceConnected(String name, String address) {
        final String compose = name + "\n" + address;
        deviceData.setText(compose);
        isConnected.setImageDrawable(ContextCompat.getDrawable(deviceData.getContext(), R.drawable.ic_baseline_bluetooth_connected_24));
        Toast.makeText(deviceData.getContext(), compose, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDeviceDisconnected() {
        deviceData.setText("Disconnected");
        isConnected.setImageDrawable(ContextCompat.getDrawable(deviceData.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDeviceConnectionFailed() {
        deviceData.setText("Cannot Connect");
        isConnected.setImageDrawable(ContextCompat.getDrawable(deviceData.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
        isConnected.setImageTintList(ColorStateList.valueOf(Color.RED));
    }
}
