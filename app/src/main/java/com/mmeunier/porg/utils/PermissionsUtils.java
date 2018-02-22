package com.mmeunier.porg.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

public class PermissionsUtils {
    @TargetApi(23)
    public static void customPermissionsRequest(Activity activity, String[]permissions, int request){
        ActivityCompat.requestPermissions(activity, permissions, request);
    }
}