package cn.hohn.bankcardrec.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataIOUtils {
    public static File saveImgFile() {
        String storageState = Environment.getExternalStorageState();
        if (!storageState.equals(Environment.MEDIA_MOUNTED)) {
            Log.e("hohnLog", "storage unmounted");
            return null;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
//指定文件名为ocr_日期.jpg
            String name = "ocr_" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + ".jpg";
//指定目录是我的文档的相册创建目录photo_ocr
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "photo_ocr");
//创建指定目录
            dir.mkdir();
//文件的绝对路径
            String path = dir.getAbsolutePath() + File.separator + name;
            File imgFile = new File(path);
            return imgFile;
        }
    }

    public static Uri saveDebugBitmap(Bitmap bm) {
        //指定为公共文件夹下的图片文件夹 下的debugImg目录
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "debugImg");
        //创建自定义的目录
        dir.mkdir();
        //指定格式(文件名)：debug_yyyyMMdd_hhmmss.jpg
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String fileName = "debug_" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + ".jpg";
        //文件的完整路径：目录+/或\+文件名
        File file = new File(dir.getAbsoluteFile() + File.separator + fileName);
        try {
            //定义文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //bm以JPEG的格式压缩比率为100(无损)到输出流
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            //写入文件
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

}
