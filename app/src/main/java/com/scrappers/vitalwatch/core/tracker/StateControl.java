package com.scrappers.vitalwatch.core.tracker;

/**
 * Static bluetooth state.
 *
 * @author pavl_g.
 */
public final class StateControl {
    public enum BluetoothState {
        CONNECTED, DISCONNECTED
    }
    private static BluetoothState bluetoothState = BluetoothState.DISCONNECTED;

    public static void setBluetoothState(BluetoothState bluetoothState) {
        StateControl.bluetoothState = bluetoothState;
    }

    public static BluetoothState getBluetoothState() {
        return bluetoothState;
    }
}
