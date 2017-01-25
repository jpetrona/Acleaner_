package com.test.acleaner;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.the.bestna.cleaner.R;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends Activity implements Runnable {
    private Animation fadeinAnimation;
    private ImageView splashLogo;
    private TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        splashLogo = (ImageView) findViewById(R.id.splashScreenLogo);
        appName = (TextView) findViewById(R.id.appName);

        fadeinAnimation = AnimationUtils.loadAnimation(this,R.anim.fadein);
        appName.setAnimation(fadeinAnimation);


    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        appName.setVisibility(View.VISIBLE);
        fadeinAnimation.startNow();

         new Handler().postDelayed(this,3000);
    }
    @Override
    public void run() {
        startActivity(new Intent(this,MainActivity1.class));
        this.finish();
    }
}
