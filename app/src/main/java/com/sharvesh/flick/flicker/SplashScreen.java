package com.sharvesh.flick.flicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharvesh.flick.flicker.activity.MainActivity;

public class SplashScreen extends AppCompatActivity {

    static int SPLASH_TIME_OUT = 5000;
    Typeface tf, tf1;
    TextView tv, tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);

        tf = Typeface.createFromAsset(getAssets(), "FTY STRATEGYCIDE NCV.ttf");
        tf1 = Typeface.createFromAsset(getAssets(), "DroidSerif-Bold.ttf");
        tv.setTypeface(tf);
        tv1.setTypeface(tf1);

           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   Intent i = new Intent(SplashScreen.this, MainActivity.class);
                   startActivity(i);
                   finish();
               }
           }, SPLASH_TIME_OUT);
    }
}
