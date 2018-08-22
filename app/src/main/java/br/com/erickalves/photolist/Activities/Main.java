package br.com.erickalves.photolist.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.erickalves.photolist.Adapter.PhotoListAdapter;
import br.com.erickalves.photolist.Model.PhotoItem;
import br.com.erickalves.photolist.R;
import br.com.erickalves.photolist.Util.Cache;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class Main extends AppCompatActivity implements PhotoListAdapter.OnItemClickListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private static int CAMERA = 0;
    private static int GALLERY = 1;
    //PicTake mPicTake;
    private RecyclerView photoList;
    private TextView emptyList;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    RapidFloatingActionHelper rfabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyList = findViewById(R.id.emptyList);
        rfaLayout = findViewById(R.id.activity_main_rfal);
        rfaBtn = findViewById(R.id.activity_main_rfab);

        photoList = findViewById(R.id.photoList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        photoList.setLayoutManager(mLayoutManager);
        photoList.addItemDecoration(  new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<PhotoItem> photoListItems = Cache.getDataFromSharedPreferences(Main.this);

        if(photoListItems!=null && photoListItems.size()>0){
            emptyList.setVisibility(View.GONE);
        }
        PhotoListAdapter mAdapter = new PhotoListAdapter(photoListItems,this);

        photoList.setAdapter(mAdapter);


        configFab();


    }

    private void openTitleDialog(final File image){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.image_title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String  title = input.getText().toString();
                saveImage(image, title);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                    Toast.makeText(this, R.string.permission_message, Toast.LENGTH_SHORT).show();
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
        intent.putExtra(PhotoDetail.PHOTO_EXTRA, item);

        startActivity(intent, options.toBundle());


    }

    private void configFab() {

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("From Camera")
                .setResId(R.drawable.ic_photo_camera)
                .setIconNormalColor(Color.parseColor("#668cff"))
                .setIconPressedColor(Color.parseColor("#3F51B5"))
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("From Gallery")
                .setResId(R.drawable.ic_picture)
                .setIconNormalColor(Color.parseColor("#668cff"))
                .setIconPressedColor(Color.parseColor("#3F51B5"))
                .setLabelSizeSp(14)
                .setWrapper(0)
        );

        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888);

        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        fabClick(position);
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        fabClick(position);
    }

    public void fabClick(int position){
        switch (position){
            case 0:
                openChooserImage(CAMERA);
                break;
            case 1:
                openChooserImage(GALLERY);
                break;
        }
    }
}
