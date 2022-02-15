package com.scrappers.vitalwatch.screen.vitals.container;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.scrappers.vitalwatch.R;
import com.scrappers.vitalwatch.data.ReferenceValues;

/**
 * Holds a descriptive list about all reference values of vial signs.
 *
 * @author pavl_g.
 */
public class ReferenceValuesAdapter extends RecyclerView.Adapter<VitalCardHolder> {
    private final ReferenceValues referenceValues = new ReferenceValues();
    private final Context context;
    public ReferenceValuesAdapter(final Context context) {
        this.context = context;
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
        if (position == 0) {
            holder.getSensorData().setText(referenceValues.getHeartRate());
            holder.getDataDescription().setText("Heart Rate");
            holder.getDescriptiveImage().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart));
        } else if (position == 1) {
            holder.getSensorData().setText(referenceValues.getTemperature());
            holder.getDataDescription().setText("Temperature");
            holder.getDescriptiveImage().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_thermo));
        } else if (position == 2) {
            holder.getSensorData().setText(referenceValues.getBloodPressure());
            holder.getDataDescription().setText("Blood pressure");
            holder.getDescriptiveImage().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.stetho));
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
