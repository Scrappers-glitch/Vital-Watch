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

public class EntryScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        displayFragment(new PairingScreen());
        findViewById(R.id.pairingScreenLauncher).setOnClickListener(this);
        findViewById(R.id.vitalScreenLauncher).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pairingScreenLauncher) {
            displayFragment(new PairingScreen());
        } else if (view.getId() == R.id.vitalScreenLauncher) {
            displayFragment(new VitalsScreen());
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
