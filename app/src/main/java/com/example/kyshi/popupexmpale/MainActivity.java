package com.example.kyshi.popupexmpale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseAnalytics mFirebaseAnalytics;
    Context mContext;

    // Variables for Dots on Popup Window
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    // General Views
    ConstraintLayout popupLayout;   // layout for popupwindow
    Button oneWeekButton, confirmButton;    // Button on popupWindow
    ViewPager viewPager;    // viewPager on popupWindow

    // For PopupWindow
    LayoutInflater layoutInflater;
    PopupWindow popupWindow;
    DisplayMetrics display;
    ArrayList<JSONObject> ad_list;  // Saving advertisements as JsonObject type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        firebaseInitialize();
        getNotice();

        if (!ad_list.isEmpty()) {       // firebase 에서 제대로 불러왔다면
            display = getApplicationContext().getResources().getDisplayMetrics();       // 화면 size 받아오기 위한 변수
            layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            popupLayout = (ConstraintLayout) layoutInflater.inflate(R.layout.popup, null);      // PopupWindow 안에 들어갈 Layout
            popupWindow = new PopupWindow(popupLayout, display.widthPixels * 2 / 3, display.heightPixels * 2 / 3, true);    // PopupWindow

            /* popupWindow.getContentView() 를 쓴 이유는 setContentView 에 표시 되지 않는 친구들이기 때문 */
            oneWeekButton = popupWindow.getContentView().findViewById(R.id.checkoneday);
            confirmButton = popupWindow.getContentView().findViewById(R.id.confirm);
            viewPager = popupWindow.getContentView().findViewById(R.id.viewpager);
            sliderDotspanel = popupWindow.getContentView().findViewById(R.id.SliderDots);

            /* View 가 실제로 화면에 표시되는 시간보다 코드 실행이 먼저기 때문에 이러한 식으로 구현을 함 */
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


            oneWeekButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    * 일정기간
                    * 안 받는
                    * 코드를
                    * 넣어야
                    * 해요.
                    * */
                    popupWindow.dismiss();
                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });


            PopupPagerAdapter popupPagerAdapter = new PopupPagerAdapter(this, ad_list);
            viewPager.setAdapter(popupPagerAdapter);


            // ViewPage 밑에 점 초기화
            dotscount = ad_list.size();
            dots = new ImageView[dotscount];
            for (int i = 0; i < dotscount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.nonactive_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                sliderDotspanel.addView(dots[i], params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.active_dot));

            // ViewPage 넘길 때 밑에 점 조정
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.nonactive_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
    }


    // Firebase Variables Initialization
    public void firebaseInitialize() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    // get Advertisement from Firebase Remote Config
    public void getNotice() {
        long cacheExpiration = 1;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MainActivity.this, "서버와의 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        /* JSON Parsing */
        String adlist = mFirebaseRemoteConfig.getString("adlist");
        String adlist_list[] = adlist.split(", ");      // 광고 리스트 종류 불러와서 Parsing 하기
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
