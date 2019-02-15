package com.example.kyshi.popupexmpale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PopupPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<JSONObject> jsonObjectList;    // Saving Advertisement as JsonObject Type
    private List<Bitmap> adImageList;         // Saving Real Advertisements as Drawable
    private String url;

    public PopupPagerAdapter(Context context, ArrayList<JSONObject> jsonList, ArrayList<Bitmap> bitmaps) {
        this.mContext = context;
        this.jsonObjectList = jsonList;
        this.adImageList = bitmaps;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final JSONObject jsonObject = jsonObjectList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.listitem, collection, false);
        ImageView adImage = layout.findViewById(R.id.adImage);
        Bitmap bitmap = adImageList.get(position);
        ConstraintLayout itemLayout = layout.findViewById(R.id.itemLayout);

//        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(10, 10));

        /******** 원하는 대로 코드를 고치시면 됩니다! *********/

        adImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = null;
                Toast.makeText(mContext, "hi", Toast.LENGTH_SHORT).show();
                try {
                    webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObject.getString("imgsrc"))); //  여기 imgsrc 말고 눌렀을 때 넘어갈 url 넣어주면 됨
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                webIntent.setPackage("com.android.chrome");         // chrome 으로 지정해 놨는데 바꾸고 싶으면 바꿔도 됨
                mContext.startActivity(webIntent);
            }
        });

        if (bitmap == null) {

        } else {
            adImage.setImageBitmap(adImageList.get(position));
            adImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }
        /******** 원하는 대로 코드를 고치시면 됩니다! *********/


        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return jsonObjectList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {
            return jsonObjectList.get(position).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

