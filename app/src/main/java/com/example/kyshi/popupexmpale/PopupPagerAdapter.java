package com.example.kyshi.popupexmpale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        JSONObject jsonObject = jsonObjectList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.listitem, collection, false);
        TextView adText = layout.findViewById(R.id.adText);
        ImageView adImage = layout.findViewById(R.id.adImage);
        Bitmap bitmap = adImageList.get(position);

        /******** 원하는 대로 코드를 고치시면 됩니다! *********/

        if (bitmap == null) {

        } else {
            adImage.setImageBitmap(adImageList.get(position));
            adImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }
        try {
            adText.setText(jsonObject.getString("name")); // imgsrc 값 읽어오기
        } catch (JSONException e) {
            e.printStackTrace();
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

