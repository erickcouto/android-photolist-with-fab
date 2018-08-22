package br.com.erickalves.photolist.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import br.com.erickalves.photolist.Model.PhotoItem;
import br.com.erickalves.photolist.R;

public class PhotoDetail extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);


        PhotoItem photo = (PhotoItem) getIntent().getSerializableExtra("photo");
        setTitle(photo.getName());
        imageView = findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getImageSource());
        imageView.setImageBitmap(bitmap);
    }
}
