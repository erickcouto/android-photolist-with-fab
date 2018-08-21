package br.com.erickalves.photolist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mht.pic.PicTake;
//import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class Main extends AppCompatActivity {

    //PicTake mPicTake;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ImagePicker.setMinQuality(600, 600);

       // mPicTake = new PicTake(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mPicTake.build();

                EasyImage.openChooserWithGallery(Main.this, "Selecione", 0);
                //ImagePicker.pickImage(Main.this, "Select your image:");
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        if (ContextCompat.checkSelfPermission(Main.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Main.this,
                    Manifest.permission.CAMERA)) {

                EasyImage.openChooserWithGallery(Main.this, "Select a source", 1);


            } else {

                ActivityCompat.requestPermissions(Main.this,
                        new String[]{Manifest.permission.CAMERA},
                        0);

            }
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EasyImage.openChooserWithGallery(Main.this, "Selecione", 0);


                } else {

                }
                return;
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        String mPath = mPicTake.getImagePathFromResult(this, requestCode, resultCode, data);
//        Log.e("teste", "Image path "+mPath);


//
//        imageView = findViewById(R.id.imageView);
//
//        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
//        imageView.setImageBitmap(bitmap);


        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

                imageView.setImageBitmap(bitmap);
            }
        });


    }
}
