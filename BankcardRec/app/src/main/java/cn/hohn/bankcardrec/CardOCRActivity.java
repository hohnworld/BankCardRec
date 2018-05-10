package cn.hohn.bankcardrec;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import cn.hohn.bankcardrec.common.CarRecProcess;
import cn.hohn.bankcardrec.utils.BitmapHandle;
import cn.hohn.bankcardrec.utils.ContentUriUtil;
import cn.hohn.bankcardrec.utils.DataIOUtils;
import cn.hohn.bankcardrec.utils.PermissionsUtils;

public class CardOCRActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private ImageView ivShow;
    private Button btnRec;
    private String strUriImg;
    private Bitmap bmGray;
    private Bitmap bmToGraySrc;
    private Uri uriGraySrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_ocr);
        //Uri.fromFile(new File(getIntent().getStringExtra("imgUri")).getAbsolutePath());
        strUriImg=getIntent().getStringExtra("imgUri");

        //初始化组建
        initView();
        //初始化监听器
        initListener();
        //载入默认图像
        displaySelectedImage();
    }

    private void initListener() {
        btnRec.setOnClickListener(this);
    }

    private void initView() {
        ivShow = findViewById(R.id.iv_result);
        btnRec = findViewById(R.id.btn_rec);
    }

    //显示
    @Override
    public void onClick(View v) {
        if(PermissionsUtils.isGrant(CardOCRActivity.this,REQUEST_CODE)){
            //ivShow.setImageBitmap(CarRecProcess.toGray(strUriImg,CardOCRActivity.this));
            ivShow.setImageBitmap(CarRecProcess.toGray("",CardOCRActivity.this,bmToGraySrc));

        }
    }

    private void displaySelectedImage() {
        bmToGraySrc=BitmapHandle.awayOOM(strUriImg,CardOCRActivity.this);
        uriGraySrc=DataIOUtils.saveDebugBitmap(bmToGraySrc);
        ivShow.setImageBitmap(bmToGraySrc);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE&&grantResults[0]!= PackageManager.PERMISSION_GRANTED){
            PermissionsUtils.grant(CardOCRActivity.this);
        }else{
            bmGray=CarRecProcess.toGray(strUriImg,CardOCRActivity.this,null);
            ivShow.setImageBitmap(bmGray);
        }
    }
}