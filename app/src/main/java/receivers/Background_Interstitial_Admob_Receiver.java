package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import Utilities.IDs;
import ads.AdmobInterstitial;


public class Background_Interstitial_Admob_Receiver extends BroadcastReceiver {
    private static AdmobInterstitial admobInterstitial;

    public Background_Interstitial_Admob_Receiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
    if(admobInterstitial == null){
        admobInterstitial = new AdmobInterstitial(context, IDs.AdMobInterstitial);
        admobInterstitial.loadNewAd();
        }
    else{
        if (admobInterstitial.interstitial.isLoaded())
            admobInterstitial.interstitial.show();
        admobInterstitial.loadNewAd();
        }
    }
}
