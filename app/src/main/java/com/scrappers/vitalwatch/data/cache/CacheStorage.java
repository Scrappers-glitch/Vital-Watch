package com.scrappers.vitalwatch.data.cache;

import android.content.Context;

public class CacheStorage {
    private final static String directory = "/user/";
    private final static String fileName = "database.json";
    private final static String path = directory + fileName;
    private CacheStorage() {

    }
    public static String getCacheStorage(final Context context) {
        return context.getFilesDir() + path;
    }

    public static String getDirectory(final Context context) {
        return context.getFilesDir() + directory;
    }
}
