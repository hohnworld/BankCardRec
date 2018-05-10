package cn.hohn.bankcardrec;


import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;

import cn.hohn.bankcardrec.common.StaticVal;
import cn.hohn.bankcardrec.utils.BitmapHandle;
import cn.hohn.bankcardrec.utils.ContentUriUtil;
import cn.hohn.bankcardrec.utils.DataIOUtils;
import cn.hohn.bankcardrec.utils.PermissionsUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    private static final int REQUEST_PERMISSION = 1;
    private Button btnPhoto, btnPicture;
    private ImageView ivShow;
    private Intent intent;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化OpenCV
        initOpenCV();
        //初始化组件
        initView();
        //初始化监听器
        initListener();
    }

    private void initView() {
        ivShow = findViewById(R.id.iv_show);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnPicture = findViewById(R.id.btnPicture);
        ivShow.setImageBitmap(BitmapHandle.awayOOM("/sdcard/d.jpg", MainActivity.this));
    }

    private void initListener() {
        btnPhoto.setOnClickListener(this);
        btnPicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPhoto:
                selectPic();
                break;
            case R.id.btnPicture:
                if (PermissionsUtils.isGrant(MainActivity.this, REQUEST_PERMISSION)) {
                    takePic();
                }
                break;
        }
    }

    private void takePic() {
        intent = new Intent();
        //打开摄像头
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //获取保存路径
        imgUri = Uri.fromFile(DataIOUtils.saveImgFile());
        //拍摄图片到指定的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }

    private void selectPic() {
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图像"), REQUEST_CAPTURE_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePic();
            } else {
                PermissionsUtils.grant(MainActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            intent = new Intent(MainActivity.this, CardOCRActivity.class);
            if (data == null) {
                intent.putExtra("imgUri", ContentUriUtil.getPath(MainActivity.this, imgUri));
            } else {
                intent.putExtra("imgUri", ContentUriUtil.getPath(MainActivity.this, data.getData()));
                Log.e(StaticVal.TAG, data.getDataString());
                Log.e(StaticVal.TAG, ContentUriUtil.getPath(MainActivity.this, data.getData()));
            }
            startActivity(intent);
        }
    }

    private void initOpenCV() {
        boolean isLoadSuc = OpenCVLoader.initDebug();
        if (isLoadSuc) {
            Log.e("hohnLog", "OpenCV loaded...");
        } else {
            Log.e("hohnLog", "OpenCV load failed");
        }
    }
}
