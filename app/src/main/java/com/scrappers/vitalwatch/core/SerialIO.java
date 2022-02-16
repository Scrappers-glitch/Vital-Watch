package com.scrappers.vitalwatch.core;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.scrappers.vitalwatch.screen.vitals.container.VitalCardHolder;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * IO for serial communications.
 *
 * @author pavl_g.
 */
public class SerialIO {
    public abstract static class AbstractReader extends RecyclerView.Adapter<VitalCardHolder> {

        protected final Context context;

        public AbstractReader(final Context context) {
            this.context = context;
        }

    }
    public static class DataWriter {
        private final BluetoothSPP bluetoothSPP;
        public DataWriter(final BluetoothSPP bluetoothSPP) {
            this.bluetoothSPP = bluetoothSPP;
        }
        public void sendData(byte[] data, boolean CRLF) {
            ThreadDispatcher.initializeThreadPool().dispatch(() -> bluetoothSPP.send(data, CRLF));
        }
        public void sendData(String data, boolean CRLF) {
            ThreadDispatcher.initializeThreadPool().dispatch(() -> bluetoothSPP.send(data, CRLF));
        }
    }
}
