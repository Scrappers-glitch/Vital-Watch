package com.scrappers.vitalwatch.core;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dispatches new runnable threads with a core size.
 *
 * @author pavl_g.
 */
public class ThreadDispatcher {
    private static ThreadDispatcher dispatcher;
    private final ExecutorService executorService;
    private static final Object synchronizer = new Object();

    private ThreadDispatcher(){
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Initializes the thread dispatcher.
     * @return a static dispatcher instance.
     */
    public static ThreadDispatcher initializeThreadPool() {
        if (dispatcher == null) {
            synchronized (synchronizer) {
                if (dispatcher == null) {
                    dispatcher = new ThreadDispatcher();
                }
            }
        }
        return dispatcher;
    }

    /**
     * Dispatches a new task on a custom thread with a enabled handler for ui communications.
     * @param runnable a runnable task to be executed.
     */
    public void dispatch(final Runnable runnable) {
        executorService.execute(() -> new Handler(Looper.getMainLooper()).post(runnable));
    }
}
