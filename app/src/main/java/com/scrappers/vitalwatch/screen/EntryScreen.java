package com.scrappers.vitalwatch.screen;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.scrappers.vitalwatch.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);


        rfCommSetup = new RFCommSetup(EntryScreen.this);
        rfCommSetup.initialize();

        CacheQuickSetup.write(getApplicationContext(), new SensorDataModel());
        displayFragment(new PairingScreen(rfCommSetup));
        findViewById(R.id.pairingScreenLauncher).setOnClickListener(this);
        findViewById(R.id.vitalScreenLauncher).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pairingScreenLauncher) {
            displayFragment(new PairingScreen(rfCommSetup));
        } else if (view.getId() == R.id.vitalScreenLauncher) {
            displayFragment(new VitalsScreen(rfCommSetup));
        }
    }

    protected void displayFragment(@NonNull Fragment window) {
        ThreadDispatcher.initializeThreadPool().dispatch(() -> {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layoutHolder, window);
            fragmentTransaction.commit();
        });
    }
}
