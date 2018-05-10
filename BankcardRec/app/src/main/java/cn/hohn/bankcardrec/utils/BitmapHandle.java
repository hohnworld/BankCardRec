package cn.hohn.bankcardrec.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.hohn.bankcardrec.common.StaticVal;

public class BitmapHandle {
    public static Bitmap awayOOM(String strContent, Context context) {
        //获取图像选项
        BitmapFactory.Options options = new BitmapFactory.Options();
        //图像选项设置：不解析内容
        options.inJustDecodeBounds = true;
        //指定到解析的图像
        BitmapFactory.decodeFile(strContent,options);
        //图像的宽高
        int bmWidth = options.outWidth;
        int bmHeight = options.outHeight;

        //屏幕宽高
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        int screenWidth = point.x;
        int screenHeight = point.y;

        //图像与屏幕的比重
        int scaleWidth = bmWidth / screenWidth;
        int scaleHeight = bmHeight / screenHeight;

        int inSample = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        //恢复到解析内容
        options.inJustDecodeBounds = false;
        //避免图片尺寸小于屏幕,设置为1
        if(inSample==0){
            inSample=1;
        }
        options.inSampleSize = inSample;
        options.inPreferredConfig=Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(strContent,options);

    }
}
