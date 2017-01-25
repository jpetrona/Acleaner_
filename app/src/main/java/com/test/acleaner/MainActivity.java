package com.test.acleaner;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.the.bestna.cleaner.R;

import java.io.File;

import Utilities.IDs;
import adapters.AdapterGridview;
import ads.AdmobInterstitial;
import ads.RateMyApp;
import helpers.TypefaceSpan;
import interfaces.IAdShower;

public class MainActivity extends ActionBarActivity implements
        OnItemClickListener, IAdShower {

    private GridView gridview;
    private TextView size;

  //  private AdView mAdView;
//    private AdmobInterstitial interstitial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = (GridView) findViewById(R.id.gridview);
        size = (TextView) findViewById(R.id.size);
        gridview.setOnItemClickListener(this);
        gridview.setAdapter(new AdapterGridview(this));
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        size.setTypeface(typeface);
        barraTitulo();
        size();

//        mAdView = (AdView) findViewById(R.id.adView);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                super.onAdFailedToLoad(errorCode);
//                mAdView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                mAdView.setVisibility(View.GONE);
//            }
//        });
//
//        mAdView.loadAd(new AdRequest.Builder().build());
        // Create the interstitial.
//        this.interstitial = new AdmobInterstitial(this, IDs.AdMobInterstitial);
//        this.interstitial.loadNewAd();
        
        new RateMyApp(this).app_launched();
    }


    public void barraTitulo() {
        ActionBar actionbar = this.getSupportActionBar();
        actionbar.setIcon(R.mipmap.ic_launcher);
        SpannableString s = new SpannableString("ACleaner");
        s.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionbar.setTitle(s);

        actionbar.setDisplayUseLogoEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(false);
    }

    public void size() {
        File path = Environment.getDataDirectory();
        espacio(path.getPath());
    }

    public void espacio(String path) {
        StatFs stat = new StatFs(path);
        @SuppressWarnings("deprecation")
        long blockSize = stat.getBlockSize();
        @SuppressWarnings("deprecation")
        long availableBlocks = stat.getAvailableBlocks();
        @SuppressWarnings("deprecation")
        long countBlocks = stat.getBlockCount();
        String fileSize = Formatter.formatFileSize(this, availableBlocks
                * blockSize);
        String maxSize = Formatter
                .formatFileSize(this, countBlocks * blockSize);
        size.setText("  " + fileSize + " / " + maxSize);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, CacheActivity.class));
               break;
            case 1:
                startActivity(new Intent(this, CallsMsgActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, AppActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, RamActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, BateryActivity.class));
                break;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void ShowAd() {
        //this.interstitial.showLoaded(true);
    }
}
