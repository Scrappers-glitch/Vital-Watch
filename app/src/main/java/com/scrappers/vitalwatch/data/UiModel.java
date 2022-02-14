package com.scrappers.vitalwatch.data;

import android.widget.ImageView;
import android.widget.TextView;

public class UiModel extends DataModel {
    private TextView deviceData;
    private ImageView pairingButton;
    private ImageView isConnected;

    public TextView getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(TextView deviceData) {
        this.deviceData = deviceData;
    }

    public ImageView getPairingButton() {
        return pairingButton;
    }

    public void setPairingButton(ImageView pairingButton) {
        this.pairingButton = pairingButton;
    }

    public ImageView getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(ImageView isConnected) {
        this.isConnected = isConnected;
    }
}
