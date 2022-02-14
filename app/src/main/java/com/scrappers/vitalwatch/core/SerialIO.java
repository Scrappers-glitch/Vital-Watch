package com.scrappers.vitalwatch.core;

import com.scrappers.vitalwatch.data.LocalCache;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class SerialIO {
    public static class DataReader implements BluetoothSPP.OnDataReceivedListener {

        @Override
        public void onDataReceived(byte[] data, String message) {
            /* write the sensors data to a cached data to be accessed later */
            final LocalCache.DataWriter dataWriter = new LocalCache.DataWriter();
            dataWriter.write();
        }
    }
    public static class DataWriter {
        private final BluetoothSPP bluetoothSPP;
        public DataWriter(final BluetoothSPP bluetoothSPP) {
            this.bluetoothSPP = bluetoothSPP;
        }
        public void sendData(byte[] data, boolean CRLF) {
            ThreadDispatcher.initializeThreadPool(5).dispatch(() -> bluetoothSPP.send(data, CRLF));
        }
        public void sendData(String data, boolean CRLF) {
            ThreadDispatcher.initializeThreadPool(5).dispatch(() -> bluetoothSPP.send(data, CRLF));
        }
    }
}
