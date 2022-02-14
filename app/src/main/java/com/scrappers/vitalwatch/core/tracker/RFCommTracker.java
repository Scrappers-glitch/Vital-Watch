package com.scrappers.vitalwatch.core.tracker;

public interface RFCommTracker {
    void onInitialize();
    void onPrepare();
    void onConnectionPassed();
    void onDestroyed();
}
