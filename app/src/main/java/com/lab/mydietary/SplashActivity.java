package com.lab.mydietary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    final static String SETTING_CODE = "settings";
    final static String HAVE_SPLASH = "splash";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences pref = getSharedPreferences(SETTING_CODE,MODE_PRIVATE);
        boolean skipSplash = pref.getBoolean(HAVE_SPLASH,false);
        ImageView iv = (ImageView) findViewById(R.id.image_heart);
        if(skipSplash)
        {
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
            finish();
        }
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.start();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
            finish();
        }, 10000);
    }
}
