package com.byagowi.persiancalendar.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.byagowi.persiancalendar.Constants;
import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.entity.CityEntity;
import com.byagowi.persiancalendar.service.BroadcastReceivers;
import com.byagowi.persiancalendar.util.Utils;
import com.github.praytimes.Coordinate;
import com.github.praytimes.PrayTime;

import java.util.concurrent.TimeUnit;

public class AthanActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textAlarmName;
    private AppCompatImageView athanIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String prayerKey = getIntent().getStringExtra(Constants.KEY_EXTRA_PRAYER_KEY);
        Utils utils = Utils.getInstance(getApplicationContext());

        utils.changeAppLanguageAndFontScale(this);
        utils.loadLanguageResource();

        setContentView(R.layout.activity_athan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textAlarmName = (TextView) findViewById(R.id.athan_name);
        TextView textCityName = (TextView) findViewById(R.id.place);
        athanIconView = (AppCompatImageView) findViewById(R.id.background_image);
        athanIconView.setOnClickListener(this);

        setPrayerView(prayerKey);

        CityEntity cityEntity = utils.getCityFromPreference();
        if (cityEntity != null) {
            String cityName = utils.getAppLanguage().equals("en") ? cityEntity.getEn() : cityEntity.getFa();
            textCityName.setText(getString(R.string.in_city_time) + " " + cityName);
        } else {
            Coordinate coordinate = utils.getCoordinate();
            textCityName.setText(getString(R.string.in_city_time) + " "
                    + coordinate.getLatitude() + ", " + coordinate.getLongitude());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, TimeUnit.SECONDS.toMillis(45));
    }

    private void setPrayerView(String key) {
        if (!TextUtils.isEmpty(key)) {
            PrayTime prayTime = PrayTime.valueOf(key);

            switch (prayTime) {
                case FAJR:
                    textAlarmName.setText(getString(R.string.azan1));
                    athanIconView.setImageResource(R.drawable.fajr);
                    break;

                case DHUHR:
                    textAlarmName.setText(getString(R.string.azan2));
                    athanIconView.setImageResource(R.drawable.dhuhr);
                    break;

                case ASR:
                    textAlarmName.setText(getString(R.string.azan3));
                    athanIconView.setImageResource(R.drawable.asr);
                    break;

                case MAGHRIB:
                    textAlarmName.setText(getString(R.string.azan4));
                    athanIconView.setImageResource(R.drawable.maghrib);
                    break;

                case ISHA:
                    textAlarmName.setText(getString(R.string.azan5));
                    athanIconView.setImageResource(R.drawable.isha);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        sendBroadcastStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendBroadcastStop();
    }

    @Override
    public void onBackPressed() {
        sendBroadcastStop();
    }

    private void sendBroadcastStop() {
        Intent intent = new Intent(getBaseContext(), BroadcastReceivers.class);
        intent.setAction(Constants.ACTION_STOP_ALARM);
        AthanActivity.this.sendBroadcast(intent);
        finish();
    }

}
