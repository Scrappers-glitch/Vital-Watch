package com.scrappers.vitalwatch.core;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.scrappers.vitalwatch.data.LocalCache;
import com.scrappers.vitalwatch.data.SensorDataModel;
import com.scrappers.vitalwatch.screen.vitals.container.VitalCardHolder;

import org.json.JSONException;
import java.io.IOException;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * IO for serial communications.
 *
 * @author pavl_g.
 */
public class SerialIO {
    public abstract static class AbstractReader extends RecyclerView.Adapter<VitalCardHolder> implements BluetoothSPP.OnDataReceivedListener {

        protected final Context context;
        private BluetoothSPP.OnDataReceivedListener listener;

        public AbstractReader(final Context context) {
            this.context = context;
        }
        @Override
        public void onDataReceived(byte[] data, String message) {
            /* write the sensors data to a cached data to be accessed later */
            ThreadDispatcher.initializeThreadPool(5).dispatch(()-> {
                if (listener != null) {
                    listener.onDataReceived(data, message);
                }
            });
        }

        public void setListener(BluetoothSPP.OnDataReceivedListener listener) {
            this.listener = listener;
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
