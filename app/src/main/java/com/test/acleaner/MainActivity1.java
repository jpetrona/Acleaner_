package com.test.acleaner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appnext.ads.interstitial.Interstitial;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.the.bestna.cleaner.R;

import java.io.File;

import Utilities.IDs;
import ads.AdmobInterstitial;
import ads.RateMyApp;
import interfaces.IAdShower;

/**
 * Created by mac on 4/1/16.
 */
public class MainActivity1 extends Activity implements
        AdapterView.OnItemClickListener, IAdShower {

    private SlidingMenu slidingMenu;
    private CacheActivity_Fragment cacheActivity_fragment;
    private CallsMsg_Fragment callsMsg_fragment;
    private AppActivity_Fragment appActivity_fragment;
    private RamActivity_Fragment ramActivity_fragment;
    private BateryActivity_Fragment bateryActivity_fragment;
    private HistoryActivity_Fragment historyActivity_fragment;
    private CPUTemperature_Fragment cpuTemperature_fragment;
    private android.app.FragmentTransaction fragmentTransaction;

    private LinearLayout lyCache, lyCall, lyAppManager, lyHistory, lyRam, lyBattery, lyCpuTemper;
    private TextView tvStorage_size;
    private TextView titleTxt, ignorTxt;

    private int selectedMenu = 0;
//    private AdView mAdView;
    Interstitial interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.interstitial =  new Interstitial(this, IDs.Appnext_PlacementID);
        this.interstitial.loadAd();
        setContentView(R.layout.activity_main1);

        titleTxt = (TextView) findViewById(R.id.titleTxt);
        ignorTxt = (TextView) findViewById(R.id.ignorTxt);

        ImageView ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideMenuToggle();
            }
        });
        setMenu();
        titleTxt.setText(getResources().getString(R.string.ram_title));
        showRamFragment();
    try{

      this.sendBroadcast(new Intent(this.getPackageName()+".Start_Services"));

      new RateMyApp(this).app_launched();

        }catch (Exception e){e.printStackTrace();}

        SetVisibleFragment(getIntent());
//        this.interstitial = new AdmobInterstitial(this, IDs.AdMobInterstitial);
//        this.interstitial.loadNewAd();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SetVisibleFragment(intent);
    }

    private void SetVisibleFragment(Intent intent){
        if(intent.getBooleanExtra("cache",false)){
            ignorTxt.setVisibility(View.GONE);
            titleTxt.setText(getResources().getString(R.string.cache_title));
            showCacheFragment();
            selectedMenu = 0;
        }
        else if(intent.getBooleanExtra("cpu",false)){
            ignorTxt.setVisibility(View.GONE);
            titleTxt.setText(getResources().getString(R.string.cputemp));
            showCpuTemperatureFragment();
            selectedMenu = 0;
        }
        else if(intent.getBooleanExtra("battery",false)){
            ignorTxt.setVisibility(View.GONE);
            titleTxt.setText(getResources().getString(R.string.battery_title));
            showBatteryFragment();
            selectedMenu = 2;
        }
        else{
            ignorTxt.setVisibility(View.GONE);
            titleTxt.setText(getResources().getString(R.string.ram_title));
            showRamFragment();
            selectedMenu = 2;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.owner_home_side_menu);

        initMenuView();
        setListenersForMenu();
    }

    protected void slideMenuToggle() {
        slidingMenu.toggle();
    }

    private void initMenuView() {
        lyCache = (LinearLayout) slidingMenu.findViewById(R.id.lyCache);
        lyCall = (LinearLayout) slidingMenu.findViewById(R.id.lyCall);
        lyAppManager = (LinearLayout) slidingMenu.findViewById(R.id.lyAppManager);
        lyHistory = (LinearLayout) slidingMenu.findViewById(R.id.lyHistory);
        lyRam = (LinearLayout) slidingMenu.findViewById(R.id.lyRam);
        lyBattery = (LinearLayout) slidingMenu.findViewById(R.id.lyBattery);
        lyCpuTemper = (LinearLayout) slidingMenu.findViewById(R.id.lyCpuTemper);
        tvStorage_size = (TextView) slidingMenu.findViewById(R.id.tvStorage_size);

        getStoragesize();
    }

    private void setListenersForMenu() {
        lyCache.setOnClickListener(menuItemClicked);
        lyCall.setOnClickListener(menuItemClicked);
        lyAppManager.setOnClickListener(menuItemClicked);
        lyHistory.setOnClickListener(menuItemClicked);
        lyCpuTemper.setOnClickListener(menuItemClicked);
        lyRam.setOnClickListener(menuItemClicked);
        lyBattery.setOnClickListener(menuItemClicked);
        ignorTxt.setOnClickListener(menuItemClicked);
    }

    public void getStoragesize() {
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
        tvStorage_size.setText("  " + fileSize + " / " + maxSize);
    }

    private View.OnClickListener menuItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.lyCache:
                    ignorTxt.setVisibility(View.GONE);
                    // if (selectedMenu != 0) {
                    titleTxt.setText(getResources().getString(R.string.cache_title));
                    showCacheFragment();
                    selectedMenu = 0;
                    // }
                    slideMenuToggle();
                    break;
                case R.id.lyCall:
                    ignorTxt.setVisibility(View.GONE);
                    titleTxt.setText(getResources().getString(R.string.callmessge_title));
                    showCallsMessageFragment();
                    selectedMenu = 1;
                    // }
                    slideMenuToggle();
                    break;
                case R.id.lyAppManager:
                    ignorTxt.setVisibility(View.GONE);
                    titleTxt.setText(getResources().getString(R.string.appmanager_title));
                    showAppManagerFragment();
                    selectedMenu = 2;
                    // }
                    slideMenuToggle();
                    break;
                case R.id.lyHistory:
                    ignorTxt.setVisibility(View.GONE);
                    titleTxt.setText(getResources().getString(R.string.history_title));
                    showHistoryFragment();
                    selectedMenu = 2;
                    // }
                    slideMenuToggle();
                    break;
                case R.id.lyCpuTemper:
                    ignorTxt.setVisibility(View.GONE);
                    titleTxt.setText(getResources().getString(R.string.cputemp));
                    showCpuTemperatureFragment();
                    selectedMenu = 2;
                    // }
                    slideMenuToggle();
                    break;
                case R.id.lyRam:
                    ignorTxt.setVisibility(View.VISIBLE);
                    titleTxt.setText(getResources().getString(R.string.ram_title));
                    showRamFragment();
                    selectedMenu = 2;
                    // }
                    slideMenuToggle();
                    break;
                case R.id.lyBattery:
                    ignorTxt.setVisibility(View.GONE);
                    titleTxt.setText(getResources().getString(R.string.battery_title));
                    showBatteryFragment();
                    selectedMenu = 2;
                    // }
                    slideMenuToggle();
                    break;
                case R.id.ignorTxt:
                    ramActivity_fragment.startExceptionActivity();
                    break;
                default:
                    break;
            }
        }
    };


    private void showCacheFragment() {
        cacheActivity_fragment = new CacheActivity_Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, cacheActivity_fragment);
        fragmentTransaction.commit();
    }

    private void showCallsMessageFragment() {
        callsMsg_fragment = new CallsMsg_Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, callsMsg_fragment);
        fragmentTransaction.commit();
    }

    private void showAppManagerFragment() {
        appActivity_fragment = new AppActivity_Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, appActivity_fragment);
        fragmentTransaction.commit();
    }

    private void showRamFragment() {
        ramActivity_fragment = new RamActivity_Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, ramActivity_fragment);
        fragmentTransaction.commit();
    }

    private void showBatteryFragment() {
        bateryActivity_fragment = new BateryActivity_Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, bateryActivity_fragment);
        fragmentTransaction.commit();
    }

    private void showHistoryFragment() {
        historyActivity_fragment = new HistoryActivity_Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, historyActivity_fragment);
        fragmentTransaction.commit();
    }

    private void showCpuTemperatureFragment() {
        cpuTemperature_fragment = new CPUTemperature_Fragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, cpuTemperature_fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void ShowAd() {
        if(this.interstitial.isAdLoaded())
            this.interstitial.showAd();
        this.interstitial.loadAd();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
