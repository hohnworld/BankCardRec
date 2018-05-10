package cn.hohn.bankcardrec;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.hohn.bankcardrec.common.StaticVal;
import cn.hohn.bankcardrec.utils.BitmapHandle;
import cn.hohn.bankcardrec.utils.ContentUriUtil;
import cn.hohn.bankcardrec.utils.DataIOUtils;
import cn.hohn.bankcardrec.utils.FileProvider;
import cn.hohn.bankcardrec.utils.PermissionsUtils;
import cn.hohn.bankcardrec.utils.ProviderUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;
    private Button btnPhoto, btnPicture;
    private ImageView ivShow;
    private Intent intent;
    private Uri imgUri;
    private String mCurrentPhotoPath;

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
                    //takePic();
                    takePic();
                }
                break;
        }
    }

    private void takePic() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
           File file= DataIOUtils.saveImgFile();
            mCurrentPhotoPath = file.getAbsolutePath();
            imgUri = FileProvider.getUriForFile(this, file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(takePictureIntent, REQUEST_CAPTURE_IMAGE);
        }
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
            //拍照
            if (data == null) {
                intent.putExtra("imgUri",mCurrentPhotoPath);
            }
            //相册
            else {
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
