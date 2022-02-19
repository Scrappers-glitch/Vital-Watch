package com.scrappers.vitalwatch.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.core.AbstractScreen;
import com.scrappers.vitalwatch.core.RFCommSetup;
import com.scrappers.vitalwatch.core.ThreadDispatcher;
import com.scrappers.vitalwatch.data.SensorDataModel;
import com.scrappers.vitalwatch.data.cache.CacheQuickSetup;
import com.scrappers.vitalwatch.screen.vitals.screen.VitalsScreen;

/**
 * The entry to the application.
 *
 * @author pavl_g.
 */
public class EntryScreen extends AppCompatActivity implements View.OnClickListener{

    protected RFCommSetup rfCommSetup;
    protected AbstractScreen screen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        rfCommSetup = new RFCommSetup(EntryScreen.this).createNewSSPInstance();
        // setup the bluetooth service if the bluetooth is enabled
        rfCommSetup.startBluetoothService();

        CacheQuickSetup.write(getApplicationContext(), new SensorDataModel());

        displayFragment(PairingScreen.class);
        findViewById(R.id.pairingScreenLauncher).setOnClickListener(this);
        findViewById(R.id.vitalScreenLauncher).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pairingScreenLauncher) {
            displayFragment(PairingScreen.class);
        } else if (view.getId() == R.id.vitalScreenLauncher) {
            displayFragment(VitalsScreen.class);
        }
    }

    protected void displayFragment(@NonNull Class<? extends Fragment> window) {
        ThreadDispatcher.initializeThreadPool().dispatch(() -> {
            final Bundle dataBundle = new Bundle();
            dataBundle.putParcelable("RFComm", rfCommSetup);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.layoutHolder, window, dataBundle)
                    .commit();
        });
    }
}
