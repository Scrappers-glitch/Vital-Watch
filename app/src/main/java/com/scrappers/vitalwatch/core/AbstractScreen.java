package com.scrappers.vitalwatch.core;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.scrappers.vitalwatch.data.LocalCache;
import com.scrappers.vitalwatch.data.SensorDataModel;

import org.json.JSONException;

import java.io.IOException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * A factory pattern based class used to create a new fragment screen and
 * add an entry animation for it.
 *
 * @author pavl_g.
 */
public abstract class AbstractScreen extends Fragment implements LocalCache.DataListener {

    protected LocalCache.DataReader cacheReader;
    protected LocalCache.DataWriter cacheWriter;
    protected final SensorDataModel sensorDataModel = new SensorDataModel();
    protected final BluetoothSPP bluetoothSPP;

    public AbstractScreen(final BluetoothSPP bluetoothSPP) {
        this.bluetoothSPP = bluetoothSPP;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ThreadDispatcher.initializeThreadPool(5).dispatch(() -> {
            try {
                final String path = LocalCache.CacheStorage.getCacheStorage(getLayoutInflater().getContext());
                cacheReader = new LocalCache.DataReader(path);
                cacheWriter = new LocalCache.DataWriter(path).initialize(getActivity());
                cacheReader.setDataListener(this);
                cacheReader.read().fillSensorModel(sensorDataModel);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onReadCompleted(SensorDataModel cacheModel) {
        try {
            cacheWriter.getSensorData(sensorDataModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(getLayoutId(), container, false);
        final CardView animatorView = new CardView(layout.getContext());
        animatorView.setBackground(layout.getBackground());
        animatorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        animatorView.addView(layout);
        if (getAnimationId() != 0) {
            animatorView.startAnimation(AnimationUtils.loadAnimation(layout.getContext(), getAnimationId()));
        }
        return animatorView;
    }

    /**
     * Override and return the path to your layout starting from R.layout.
     * @return the layout id.
     */
    public abstract int getLayoutId();

    /**
     * Override and return the path to your animation xml starting from R.anim.
     * @return the animation id.
     */
    public abstract int getAnimationId();

    public LocalCache.DataReader getCacheReader() {
        return cacheReader;
    }

    public LocalCache.DataWriter getCacheWriter() {
        return cacheWriter;
    }
}
