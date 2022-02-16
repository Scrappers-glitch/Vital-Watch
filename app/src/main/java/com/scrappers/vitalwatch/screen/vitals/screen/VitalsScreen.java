package com.scrappers.vitalwatch.screen.vitals.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.core.AbstractScreen;
import com.scrappers.vitalwatch.core.RFCommSetup;
import com.scrappers.vitalwatch.core.SerialIO;
import com.scrappers.vitalwatch.screen.vitals.container.ReferenceValuesAdapter;
import com.scrappers.vitalwatch.screen.vitals.container.VitalsAdapter;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Tracks the vital signs of a user.
 *
 * @author pavl_g.
 */
public class VitalsScreen extends AbstractScreen  {

    public VitalsScreen(RFCommSetup rfCommSetup) {
        super(rfCommSetup);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vital_screen;
    }

    @Override
    public int getAnimationId() {
        return R.anim.thow_up;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        final RecyclerView userVitalHolder = view.findViewById(R.id.vitalSigns);
        final VitalsAdapter vitalsAdapter = new VitalsAdapter(getContext());
        userVitalHolder.setLayoutManager(new GridLayoutManager(getContext(), 1));
        userVitalHolder.setAdapter(vitalsAdapter);
        rfCommSetup.getBluetoothSPP().setOnDataReceivedListener(vitalsAdapter);

        final ReferenceValuesAdapter referenceValuesAdapter = new ReferenceValuesAdapter(getContext());
        final RecyclerView referenceValuesHolder = view.findViewById(R.id.referenceValues);
        referenceValuesHolder.setLayoutManager(new GridLayoutManager(getContext(), 1));
        referenceValuesHolder.setAdapter(referenceValuesAdapter);
    }
}
