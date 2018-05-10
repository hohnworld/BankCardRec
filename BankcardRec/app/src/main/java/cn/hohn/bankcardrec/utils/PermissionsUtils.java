package cn.hohn.bankcardrec.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import cn.hohn.bankcardrec.MainActivity;

public final class PermissionsUtils {
    //申请的权限数组
    private static  String[] PERMISSION_CAMERA_AND_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static boolean isGrant(Activity activity, int requestCode) {
        // 当前系统版本大于android6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查存储权限
            int storagePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 检查照相权限
            int cameraPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            // 没有授权返回false
            if (storagePermission != PackageManager.PERMISSION_GRANTED ||
                    cameraPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(PERMISSION_CAMERA_AND_STORAGE, requestCode);
                return false;
            }
        }
        return true;
    }

    public static void grant(final Context context) {
        Toast.makeText(context, "授权异常，请手动授权", Toast.LENGTH_SHORT).show();
        //打开程序设置权限页面
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}