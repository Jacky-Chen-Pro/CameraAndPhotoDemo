package org.jackyonline.cameraandphotodemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.trinea.android.common.util.FileUtils;

/**
 * Created by Jacky on 2015/12/1.
 */
public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int ALBUM_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;

    private ImageView mIvPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvPhoto = (ImageView)findViewById(R.id.iv_photo);
        findViewById(R.id.bt_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择"), ALBUM_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        findViewById(R.id.bt_takePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //将拍照的图片保存到指定的地址，获取的时候就从fileUri中获取，否则因为设定了输出图片的位置，在onActivityResult()方法中，获取到的data是空的
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        findViewById(R.id.bt_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TableActivity.class));
            }
        });

        findViewById(R.id.bt_recycleView).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecycleViewActivity.class));
            }
        });
}

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        }else {
            mediaStorageDir = new File(getCacheDir(), "MyCameraApp");
        }

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Action Capture Photo Success", Toast.LENGTH_SHORT).show();
                //直接根据Uri地址加载图片，ImageLoader提供的功能
                ImageLoader.getInstance().displayImage(fileUri.toString(), mIvPhoto);
            }else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Capture Photo Canceled", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Action Capture Photo Failed", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == ALBUM_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                try{
                    Uri fileUri = data.getData();
                    //直接根据Uri地址加载图片，ImageLoader提供的功能
                    ImageLoader.getInstance().displayImage(Uri.fromFile(new File(getRealPathFromURI(fileUri))).toString(), mIvPhoto);
                    Toast.makeText(this, "Action Album Photo Success", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(this, "Action Album Photo do not exist real", Toast.LENGTH_SHORT).show();
                }
            }else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Album Photo Canceled", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Action Album Photo Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 并不建议这么做，因为这个操作需要查询数据库，另外一种就是直接获取Uri中的内容，也就是图片流
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        Log.d("path=====", result);
        return result;
    }
}
