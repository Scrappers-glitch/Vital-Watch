package com.scrappers.vitalwatch.data.cache;

import com.scrappers.vitalwatch.data.SensorDataModel;

public interface DataListener {
    void onReadCompleted(final SensorDataModel cacheModel);
}
