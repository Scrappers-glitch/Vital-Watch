package com.scrappers.vitalwatch.data.cache;

public enum CacheKeys {
    DEVICE_DATA("Device Data"),
    DEVICE_NAME("Device Name"),
    DEVICE_STATE("Device State"),
    DEVICE_MAC_ADDRESS("Device Mac Address"),

    USER_DATA("User Data"),
    USER_NAME("User Name"),
    USER_PASSWORD("User Password"),
    USER_ACC("User Account"),

    VITAL_DATA("Vital Data"),
    BLOOD_PRESSURE("Blood Pressure"),
    HEART_RATE("Heart Rate"),
    TEMPERATURE("Temperature");

    public final String key;

    CacheKeys(final String key) {
        this.key = key;
    }
}
