package com.scrappers.vitalwatch.core;

import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.ComponentActivity;
import androidx.core.content.ContextCompat;


/**
 * A compatible permission handler for Android_11.
 *
 * @author pavl_g.
 */
public class PermissionHandler {

    private static PermissionHandler permissionHandler;
    private static final Object synchronizer = new Object();
    private Runnable ifGrantedAction;

    private PermissionHandler() {

    }

    public static PermissionHandler getInstance() {
        if (permissionHandler == null) {
            synchronized (synchronizer) {
                if (permissionHandler == null) {
                    permissionHandler = new PermissionHandler();
                }
            }
        }
        return permissionHandler;
    }

    public void setIfGrantedAction(Runnable ifGrantedAction) {
        this.ifGrantedAction = ifGrantedAction;
    }

    /**
     * Requests a permission.
     *
     */
    public void requestPermission(final ComponentActivity activity,
                                  final String permission, final int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                if (ifGrantedAction != null) {
                    ifGrantedAction.run();
                }
            } else {
                // You can directly ask for the permission.
                activity.requestPermissions(new String[] {permission},
                        requestCode);
            }
        }
    }
}
