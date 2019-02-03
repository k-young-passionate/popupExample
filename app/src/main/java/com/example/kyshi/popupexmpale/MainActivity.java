package com.example.kyshi.popupexmpale;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseAnalytics mFirebaseAnalytics;

    ArrayList<JSONObject> ad_list;
    LayoutInflater layoutInflater;
    PopupWindow popupWindow;
    ConstraintLayout layout_main;
    DisplayMetrics display;
    Button oneWeekButton, confirmButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = getApplicationContext().getResources().getDisplayMetrics();
        layoutInflater = (LayoutInflater) this.getSystemService(MainActivity.this.LAYOUT_INFLATER_SERVICE);
        layout_main = (ConstraintLayout) layoutInflater.inflate(R.layout.popup, null);
        popupWindow = new PopupWindow(layout_main, display.widthPixels * 2 / 3, display.heightPixels * 2 / 3, true);


        findViewById(R.id.main).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.setOutsideTouchable(false);
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(false);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                popupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 1, 1);
                popupWindow.update();
            }
        });

        oneWeekButton = popupWindow.getContentView().findViewById(R.id.checkoneday);
        confirmButton = popupWindow.getContentView().findViewById(R.id.confirm);
        listView = popupWindow.getContentView().findViewById(R.id.mylist);

        oneWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIrebase로 신호 보내기
                popupWindow.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        /*
            여기서 광고 가져오는 코드 넣기
         */

        firebaseInitialize();
        getNotice();

        List<String> list = new ArrayList<>();

        for (JSONObject jsonObject : ad_list) {
            try {
                list.add(jsonObject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.listitem, list);
        listView.setAdapter(adapter);

    }

    public void firebaseInitialize() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);


    }

    public void getNotice() {

        long cacheExpiration = 1;

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MainActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        String adlist = mFirebaseRemoteConfig.getString("adlist");
        String adlist_list[] = adlist.split(", ");
        ad_list = new ArrayList<>();
        for (String ad : adlist_list) {
            try {
                ad_list.add(new JSONObject(mFirebaseRemoteConfig.getString(ad)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
