package com.example.kyshi.popupexmpale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class DownloadImages extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap;

        try {
            InputStream is = new URL(strings[0]).openStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e){
            return null;
        }
        return bitmap;
    }
}
