package services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.the.bestna.cleaner.R;

import java.util.Timer;
import java.util.TimerTask;

import activities.Facebook_NativeAd_Activity;

/**
 * Created by Numan on 4/12/2016.
 */
public class FacebookAdService extends Service //implements AdListener,Response.Listener<Bitmap>,Response.ErrorListener
{

    //    public static NativeAd nativeAd;
    private Timer t;
    private TimerTask tt;
    private Handler handler;
    private NotificationCompat.Builder n;
    private RequestQueue reqQueue;
    private BroadcastReceiver FbAdReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return this.START_STICKY;
    }

    @Override
    public void onCreate() {
        try {
            IntentFilter FbAdFilter = new IntentFilter();
            FbAdFilter.addAction("ShowFBAd");
            FbAdReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("ShowFBAd")) {
                        Intent i = new Intent(context, Facebook_NativeAd_Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            };
            this.registerReceiver(FbAdReceiver, FbAdFilter);

            reqQueue = Volley.newRequestQueue(getApplicationContext());

            //AdSettings.addTestDevice("");
            handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    //              nativeAd = new NativeAd(FacebookAdService.this,"480911548772837_480912935439365");
                    //            nativeAd.setAdListener(FacebookAdService.this);
                    //          nativeAd.loadAd();
                }
            };
            tt = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
            t = new Timer();
            t.scheduleAtFixedRate(tt, 150000, 10800000);
        } catch (Exception e) {
        }
        super.onCreate();
    }
/*
    @Override
    public void onDestroy() {
        super.onDestroy();
    //if(nativeAd != null)
      //  nativeAd.destroy();
        this.unregisterReceiver(FbAdReceiver);
    }
    @Override
    public void onError(){//Ad ad, AdError adError) {
        //if (ad != nativeAd)
            return;
    }
    @Override
    public void onAdLoaded(){//final Ad ad) {
        //if(nativeAd != null){
          //  if (ad != nativeAd)
            return;
        //NativeAd.Image adIcon = nativeAd.getAdIcon();
        if(adIcon != null){
  //          ImageRequest imgReq = new ImageRequest(adIcon.getUrl(),this,adIcon.getWidth(),adIcon.getHeight(),null,null,this);

            n = new NotificationCompat.Builder(getApplicationContext());
            n.setAutoCancel(true);
            n.setDefaults(Notification.DEFAULT_ALL);
    //        n.setContentTitle(nativeAd.getAdTitle());
      //      n.setContentText(nativeAd.getAdBody());
        //    n.setTicker(nativeAd.getAdBody());
            n.setSmallIcon(R.drawable.bg);

            //reqQueue.add(imgReq);
        }
        }
    /*
    @Override
    public void onAdClicked(Ad ad) {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }
    @Override
    public void onResponse(Bitmap response)
    {
        if(response != null)
        {
            Intent pintent = new Intent("ShowFBAd");
            PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0, pintent, 0);

            n.setLargeIcon(response);
            n.setContentIntent(pi);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, n.build());
        }
    }*/
}