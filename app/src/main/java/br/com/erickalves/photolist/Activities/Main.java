package br.com.erickalves.photolist.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.erickalves.photolist.Adapter.PhotoListAdapter;
import br.com.erickalves.photolist.Model.PhotoItem;
import br.com.erickalves.photolist.R;
import br.com.erickalves.photolist.Util.Cache;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class Main extends AppCompatActivity implements PhotoListAdapter.OnItemClickListener {

    private static int CAMERA = 0;
    private static int GALLERY = 1;
    //PicTake mPicTake;
    private RecyclerView photoList;
    private TextView emptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //imageView = findViewById(R.id.imageView);


        

        photoList = findViewById(R.id.photoList);
        emptyList = findViewById(R.id.emptyList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        photoList.setLayoutManager(mLayoutManager);

        photoList.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // specify an adapter (see also next example)
//        PhotoListAdapter mAdapter = new PhotoListAdapter(myDataset);
//        photoList.setAdapter(mAdapter);

        List<PhotoItem> photoListItems = Cache.getDataFromSharedPreferences(Main.this);

        if(photoListItems!=null && photoListItems.size()>0){
            emptyList.setVisibility(View.GONE);
        }
        PhotoListAdapter mAdapter = new PhotoListAdapter(photoListItems,this);

        photoList.setAdapter(mAdapter);

        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openChooserImage(CAMERA);

            }
        });

    }

    private void openTitleDialog(final File image){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String  title = input.getText().toString();
                saveImage(image, title);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void saveImage(File imageFile, String title){
        PhotoItem photo = new PhotoItem();
        photo.setImageSource(imageFile.getPath());
        photo.setName(title);

        Date date = new Date();
        photo.setCreated(date);
        Cache.addPhotoToList(Main.this,photo);

        List<PhotoItem> photoListItems = Cache.getDataFromSharedPreferences(Main.this);
        PhotoListAdapter mAdapter = new PhotoListAdapter(photoListItems,Main.this);

        photoList.setAdapter(mAdapter);
        emptyList.setVisibility(View.GONE);

    }
    private void openChooserImage(int type) {
        if (type == GALLERY) {
            EasyImage.openGallery(Main.this, 0);
        } else if (type == CAMERA) {
            openCameraPermissionCheck();
        }

    }

    private void openCameraPermissionCheck() {
        if (ContextCompat.checkSelfPermission(Main.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Main.this, Manifest.permission.CAMERA)) {

                EasyImage.openCamera(Main.this, 1);

            } else {

                ActivityCompat.requestPermissions(Main.this, new String[]{Manifest.permission.CAMERA}, 0);

            }
        } else {
            EasyImage.openCamera(Main.this, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EasyImage.openCamera(Main.this, 1);

                } else {
                    Toast.makeText(this, "We need your permission to access your pictures", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(Main.this, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                openTitleDialog(imageFile);

            }
        });


    }

    @Override
    public void onClick(PhotoItem item, ImageView iv) {
        Intent intent = new Intent(Main.this, PhotoDetail.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(Main.this, iv,  ViewCompat.getTransitionName(iv));
        intent.putExtra("photo", (Serializable) item);

        startActivity(intent, options.toBundle());


    }
}
