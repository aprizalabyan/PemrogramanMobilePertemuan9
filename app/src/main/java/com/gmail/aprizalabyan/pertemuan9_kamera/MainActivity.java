package com.gmail.aprizalabyan.pertemuan9_kamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button bt_camera;
    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 1 : mengambil image view dan button camera dari layout
        imageView = findViewById(R.id.imageView);
        bt_camera = findViewById(R.id.bt_camera);

        /*
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 0);
            }
        });
        */

        //TODO 2 : mengecek jika belum memiliki permission untuk mengakses kamera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //TODO 2.1 : button camera didisable
            bt_camera.setEnabled(false);
            //TODO 2.2 : merequest permission untuk mengakses kamera
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    //TODO 3 : method untuk hasil request permission kamera
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //TODO 3.1 : mengecek jika sudah meiliki permission maka button camera dienable
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                bt_camera.setEnabled(true);
            }
        }
    }

    //TODO 4 : method untuk take picture
    public void takePicture(View view) {
        //TODO 4.1 : membuat intent untuk membuka kamera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //TODO 4.2 : membuat file untuk menyimpan data dari file gambar dari method getOutputMediaFile
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        //TODO 4.3 : menjalankan intent dengan nilai request code 0
        startActivityForResult(intent, 0);
    }

    //TODO 5 : method untuk activity hasil take picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //TODO 5.1 : mengecek jika hasilnya benar maka akan menampilkan gambar tersebut pada image view
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
            }
            //TODO 5.2 : jika user membatalkan take picture maka akan memunculkan toast
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO 6 : method untuk menyimpan hasil foto
    private static File getOutputMediaFile(){
        //TODO 6.1 : mengambil direktori pictures dari penyimpanan perangkat
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera");

        //TODO 6.2 : mengecek direktori pictures ada atau tidak
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        //TODO 6.3 : membuat time stamp untuk gambar hasil foto
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //TODO 6.4 : membuat file baru atau menyimpan hasil foto pada direktori yang telah diset tadi
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
}
