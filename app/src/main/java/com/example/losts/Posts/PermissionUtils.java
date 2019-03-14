package com.example.losts.Posts;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;


public class PermissionUtils {
    Activity context;

    public PermissionUtils(Activity context) {
        this.context = context;
    }

    public boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public boolean hasPermission(Activity activity, String permission) {
//        if (useRunTimePermissions()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
//        }
//        return true;
        return false;
    }


    public void requestPermissions(Activity activity, String[] permission, int requestCode) {
        if (useRunTimePermissions()) {
            activity.requestPermissions(permission, requestCode);
        }
    }

    public void requestPermissions(Fragment fragment, String[] permission, int requestCode) {
        if (useRunTimePermissions()) {
            fragment.requestPermissions(permission, requestCode);
        }
    }

    public boolean  checkGrantResults(int[] grantResults){
        if (grantResults.length == 0){
            return false;
        }else {
            boolean status = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED){ status = false;}
            }
            return status;
        }
    }

}