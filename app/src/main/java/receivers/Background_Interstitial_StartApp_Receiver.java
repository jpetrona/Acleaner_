package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;


public class Background_Interstitial_StartApp_Receiver extends BroadcastReceiver implements AdEventListener
{
    //private static StartAppAd startAppAd;
    public Background_Interstitial_StartApp_Receiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
//        if(startAppAd == null) {
//            startAppAd = new StartAppAd(context);
//        }
//        else{
//            startAppAd.loadAd(this);
//        }
    }
    @Override
    public void onReceiveAd(Ad ad) {
//        startAppAd.showAd();
//        startAppAd.onBackPressed();
    }
    @Override
    public void onFailedToReceiveAd(Ad ad) {

    }
}
