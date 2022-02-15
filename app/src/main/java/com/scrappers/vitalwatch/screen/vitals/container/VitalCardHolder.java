package com.scrappers.vitalwatch.screen.vitals.container;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scrappers.vitalwatch.R;

/**
 *
 * @author pavl_g.
 */
public class VitalCardHolder extends RecyclerView.ViewHolder {
    private final ImageView descriptiveImage;
    private final TextView dataDescription;
    private final TextView sensorData;

    public VitalCardHolder(@NonNull View itemView) {
        super(itemView);

        dataDescription = itemView.findViewById(R.id.descriptiveData);
        descriptiveImage = itemView.findViewById(R.id.vitalIco);
        sensorData = itemView.findViewById(R.id.sensorData);
    }

    public ImageView getDescriptiveImage() {
        return descriptiveImage;
    }

    public TextView getDataDescription() {
        return dataDescription;
    }

    public TextView getSensorData() {
        return sensorData;
    }
}
