package com.example.kyshi.popupexmpale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PopupPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<JSONObject> jsonObjectList;    // Saving Advertisement as JsonObject Type

    public PopupPagerAdapter(Context context, ArrayList<JSONObject> jsonList) {
        this.mContext = context;
        this.jsonObjectList = jsonList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        JSONObject jsonObject = jsonObjectList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.listitem, collection, false);
        TextView adText = layout.findViewById(R.id.adText);


        /******** 원하는 대로 코드를 고치시면 됩니다! *********/
        try {
            adText.setText(jsonObject.getString("imgsrc")); // imgsrc 값 읽어오기
        } catch (JSONException e) {
            e.printStackTrace();
            adText.setText("Error has been occured!");
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

