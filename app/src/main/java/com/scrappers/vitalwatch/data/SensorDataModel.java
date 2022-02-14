package com.scrappers.vitalwatch.data;

/**
 * The user data model.
 *
 * @author pavl_g.
 */
public class SensorDataModel extends DataModel {
    private String bloodPressure;
    private String temperature;
    private String heartRate;

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }
}
