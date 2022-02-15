package com.scrappers.vitalwatch.screen;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.core.ThreadDispatcher;
import com.scrappers.vitalwatch.data.LocalCache;
import com.scrappers.vitalwatch.data.SensorDataModel;
import com.scrappers.vitalwatch.screen.vitals.screen.VitalsScreen;

import org.json.JSONException;

import java.io.IOException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class EntryScreen extends AppCompatActivity implements View.OnClickListener{

    protected BluetoothSPP bluetoothSPP;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        bluetoothSPP = new BluetoothSPP(getApplicationContext());

        ThreadDispatcher.initializeThreadPool(5).dispatch(()->{
            // create a dummy local cache
            final String path = LocalCache.CacheStorage.getCacheStorage(getApplicationContext());
            final LocalCache.DataWriter dataWriter = new LocalCache.DataWriter(path);
            final SensorDataModel sensorDataModel = new SensorDataModel();
            sensorDataModel.setUsername("Admin");
            sensorDataModel.setUserAccount("Admin@VitalWatch.com");
            sensorDataModel.setUserPassword("******");

            // fill empty data
            sensorDataModel.setDeviceName("No Device");
            sensorDataModel.setDeviceMacAddress("xxxx:xxxx");
            sensorDataModel.setConnected(false);

            sensorDataModel.setTemperature("No Data");
            sensorDataModel.setBloodPressure("No Data");
            sensorDataModel.setHeartRate("No Data");

            try {
                dataWriter.initialize(getApplicationContext()).getSensorData(sensorDataModel).write();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });


        displayFragment(new PairingScreen(bluetoothSPP));
        findViewById(R.id.pairingScreenLauncher).setOnClickListener(this);
        findViewById(R.id.vitalScreenLauncher).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pairingScreenLauncher) {
            displayFragment(new PairingScreen(bluetoothSPP));
        } else if (view.getId() == R.id.vitalScreenLauncher) {
            displayFragment(new VitalsScreen(bluetoothSPP));
        }
    }

    protected void displayFragment(@NonNull Fragment window) {
        ThreadDispatcher.initializeThreadPool(5).dispatch(() -> {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layoutHolder, window);
            fragmentTransaction.commit();
        });
    }

}
