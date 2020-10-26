package com.example.photo_saver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.zip.ZipOutputStream;

public class AfterTakecamActivity extends AppCompatActivity {
    InputStream inputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_takecam);
        final ArrayList<File> files = (ArrayList<File>) getIntent().getExtras().get("files");

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
//        byte [] byte_arr = stream.toByteArray();
//        String image_str = Base64.enc(byte_arr);
        Button finish_take = (Button) findViewById(R.id.finish_take);

        finish_take.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//            final UploadImageTask uploadImage = new UploadImageTask();
//            files.forEach(new Consumer<File>() {
//                @Override
//                public void accept(File file) {
//                    uploadImage.doInBackground(file);
//                }
//            });
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            }
        });
    }

}
