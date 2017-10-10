package com.watchmecoding.eazynote.base;

import android.Manifest;
import android.app.Activity;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtility {

    private static final int REQUEST_PERMISSION_WRITE = 5582;
    private CallBack callback;

    public interface CallBack {
        void onGranted();

        void onNotGranted();
    }

    public void request(String perm, Activity context, CallBack callBack) {
        this.callback = callBack;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, perm);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_WRITE);
            } else {
                callback.onGranted();
            }
        } else {
            callBack.onGranted();
        }

    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callback.onGranted();
                } else {
                    callback.onNotGranted();
                }
                break;
        }
    }
}
