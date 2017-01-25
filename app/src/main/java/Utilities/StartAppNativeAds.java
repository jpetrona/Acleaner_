package Utilities;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

import interfaces.IAdClickListener;

/**
 * Created by Usman Arif on 9/20/2016.
 */
public class StartAppNativeAds implements View.OnClickListener, AdEventListener {
    private Context context;
    private int titleViewID,descriptionViewID,imageViewID,iconViewID;

    private View AdView;
    private TextView AdTitle,AdDescription;
    private ImageView AdImage,AdIcon;
    private StartAppNativeAd startAppNativeAd;
    private NativeAdDetails nativeAdDetail;
    private IAdClickListener iAdClickListener;

    public StartAppNativeAds(Context context,IAdClickListener iAdClickListener,View AdView, int titleViewID, int descriptionViewID,int iconViewID, int imageViewID) {
        this.context = context;
        this.titleViewID = titleViewID;
        this.descriptionViewID = descriptionViewID;
        this.imageViewID = imageViewID;
        this.AdView = AdView;
        this.iAdClickListener = iAdClickListener;
        this.iconViewID = iconViewID;
        initView();
    }
    private void initView(){
        AdTitle = (TextView) AdView.findViewById(titleViewID);
        AdDescription = (TextView) AdView.findViewById(descriptionViewID);
        AdTitle = (TextView) AdView.findViewById(titleViewID);
        AdImage = (ImageView) AdView.findViewById(imageViewID);
        AdIcon = (ImageView) AdView.findViewById(iconViewID);

        AdTitle.setOnClickListener(this);
        AdDescription.setOnClickListener(this);
        AdImage.setOnClickListener(this);
        AdIcon.setOnClickListener(this);

        startAppNativeAd = new StartAppNativeAd(context);
        startAppNativeAd.loadAd(new NativeAdPreferences()
                                    .setAdsNumber(1)
                                    .setAutoBitmapDownload(true)
                                    .setPrimaryImageSize(4)
                                    .setSecondaryImageSize(0),this);
    }
    @Override
    public void onClick(View v) {
        if(nativeAdDetail != null) {
            nativeAdDetail.sendClick(context);
            iAdClickListener.StartAppNative_AdClicked();
        }
        nativeAdDetail = null;
    }
    @Override
    public void onReceiveAd(Ad ad) {
        final ArrayList<NativeAdDetails> ads = startAppNativeAd.getNativeAds();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AdTitle.setText(ads.get(0).getTitle());
                AdDescription.setText(ads.get(0).getDescription());
                AdImage.setImageBitmap(ads.get(0).getImageBitmap());
                AdIcon.setImageBitmap(ads.get(0).getSecondaryImageBitmap());
                ads.get(0).sendImpression(context);
                nativeAdDetail = ads.get(0);
            }
        },0);
        AdView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onFailedToReceiveAd(Ad ad) {
        AdView.setVisibility(View.GONE);
        nativeAdDetail = null;
    }

}
