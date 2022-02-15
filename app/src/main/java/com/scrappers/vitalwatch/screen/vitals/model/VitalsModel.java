package com.scrappers.vitalwatch.screen.vitals.model;

public class VitalsModel {
    private String sensorData = "";
    private String dataDescription = "";
    private int resourceImage = 0;

    public void setSensorData(String sensorData) {
        this.sensorData = sensorData;
    }

    public void setResourceImage(int resourceImage) {
        this.resourceImage = resourceImage;
    }

    public void setDataDescription(String dataDescription) {
        this.dataDescription = dataDescription;
    }
}
