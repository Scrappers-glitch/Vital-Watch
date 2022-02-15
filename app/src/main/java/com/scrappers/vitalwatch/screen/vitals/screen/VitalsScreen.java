package com.scrappers.vitalwatch.screen.vitals.screen;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.core.AbstractScreen;
import com.scrappers.vitalwatch.screen.vitals.container.ReferenceValuesAdapter;
import com.scrappers.vitalwatch.screen.vitals.container.VitalsAdapter;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Tracks the vital signs of a user.
 *
 * @author pavl_g.
 */
public class VitalsScreen extends AbstractScreen  {
    public VitalsScreen(BluetoothSPP bluetoothSPP) {
        super(bluetoothSPP);
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
        final VitalsAdapter vitalsAdapter = new VitalsAdapter(getContext());
        final RecyclerView userVitalHolder = view.findViewById(R.id.vitalSigns);
        userVitalHolder.setLayoutManager(new GridLayoutManager(getContext(), 1));
        userVitalHolder.setAdapter(vitalsAdapter);

        final ReferenceValuesAdapter referenceValuesAdapter = new ReferenceValuesAdapter(getContext());
        final RecyclerView referenceValuesHolder = view.findViewById(R.id.referenceValues);
        referenceValuesHolder.setLayoutManager(new GridLayoutManager(getContext(), 1));
        referenceValuesHolder.setAdapter(referenceValuesAdapter);

        bluetoothSPP.setOnDataReceivedListener(vitalsAdapter);
    }
}
