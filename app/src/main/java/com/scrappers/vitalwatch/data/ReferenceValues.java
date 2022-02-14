package com.scrappers.vitalwatch.data;

/**
 * Represents a reference values.
 * @author pavl_g.
 */
public final class ReferenceValues {
    private final String bloodPressure = "120/80";
    private final String temperature = "37.5 degrees";
    private final String heartRate = "60 - 100 bpm";

    public String getBloodPressure() {
        return bloodPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public String getTemperature() {
        return temperature;
    }
}
