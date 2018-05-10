package cn.hohn.bankcardrec.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;

import cn.hohn.bankcardrec.utils.BitmapHandle;
import cn.hohn.bankcardrec.utils.DataIOUtils;

public class CarRecProcess {
    private static Bitmap bmSrc;

    public static Bitmap findCard(Uri imgUri) {
        return null;
    }

    public static Bitmap toGray(String strUriImg , Context context,Bitmap bitmap) {
        if(strUriImg.isEmpty()){
            bmSrc=bitmap;
        }else{
            bmSrc=BitmapFactory.decodeFile(strUriImg);
        }

        Mat src=new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bmSrc,src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        ///取反操作
        //创建图片大小的字节数组(8位无符号单通道，矩阵的1个元素就是1字节，矩阵大小就是cols()*rows()字节)
        byte[] data = new byte[dst.cols() * dst.rows()];
        //灰度图读取到data数组
        Log.e("hohnLog", String.valueOf(dst.size()));
        dst.get(0, 0, data);
        //灰度图(单通道)的宽、高
        int width = dst.cols();
        int height = dst.rows();
        //遍历行列，每个值取反，黑的变白的，白的变黑的
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                data[row * width + col] = (byte) (~data[row * width + col]);
            }
        }
        //data数组装到到gray
        dst.put(0, 0, data);

        Utils.matToBitmap(dst,bmSrc);
        //保存到文件
        DataIOUtils.saveDebugBitmap(bmSrc);
        return bmSrc;
    }
}
