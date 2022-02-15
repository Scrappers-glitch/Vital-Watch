package com.scrappers.vitalwatch.screen.vitals.container;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.core.SerialIO;
import com.scrappers.vitalwatch.data.LocalCache;
import com.scrappers.vitalwatch.data.SensorDataModel;
import org.json.JSONException;
import java.io.IOException;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class VitalsAdapter extends SerialIO.AbstractReader implements BluetoothSPP.OnDataReceivedListener {

    private final SensorDataModel sensorDataModel = new SensorDataModel();

    public VitalsAdapter(final Context context) {
        super(context);
    }

    @NonNull
    @Override
    public VitalCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VitalCardHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vital_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VitalCardHolder holder, int position) {
        if (sensorDataModel.getTemperature() == null) {
            sensorDataModel.setTemperature("---");
        }
        if (sensorDataModel.getHeartRate() == null) {
            sensorDataModel.setHeartRate("---");
        }
        if (sensorDataModel.getBloodPressure() == null) {
            sensorDataModel.setBloodPressure("---/---");
        }
         if (position == 0) {
             holder.getSensorData().setText(sensorDataModel.getHeartRate());
             holder.getDataDescription().setText("Heart Rate");
             holder.getDescriptiveImage().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart));
         } else if (position == 1) {
             holder.getSensorData().setText(sensorDataModel.getTemperature());
             holder.getDataDescription().setText("Temperature");
             holder.getDescriptiveImage().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_thermo));
         } else if (position == 2) {
             holder.getSensorData().setText(sensorDataModel.getBloodPressure());
             holder.getDataDescription().setText("Blood pressure");
             holder.getDescriptiveImage().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.stetho));
         }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onDataReceived(byte[] data, String message) {
        String sensorData = message.substring(message.indexOf(":") + 1);
        if (message.contains("HR")) {
            sensorData += " Bpm";
            sensorDataModel.setHeartRate(sensorData);
        }
        if (message.contains("BP")) {
            sensorData += " mm.Hg";
            sensorDataModel.setBloodPressure(sensorData);
        }
        if (message.contains("TEMP")) {
            sensorData += " deg";
            sensorDataModel.setTemperature(sensorData);
        }
        final String path = LocalCache.CacheStorage.getCacheStorage(context);
        final LocalCache.DataWriter dataWriter = new LocalCache.DataWriter(path);
        try {
            dataWriter.initialize(context).getSensorData(sensorDataModel).write();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
