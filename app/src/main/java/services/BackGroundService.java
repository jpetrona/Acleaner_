package services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


import Utilities.IDs;
import Utilities.TimeIntervals;
import receivers.Background_Interstitial_StartApp_Receiver;
import receivers.BatteryChangeMonitoring;
import receivers.LockScreenReceiver;

/**
 * Created by Numan on 3/3/2016.
 */
public class BackGroundService extends Service
{
    //private static AlarmManager StartappAlarmManager;
    //private static PendingIntent Pi;
    BatteryChangeMonitoring BatteryMoniterReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return this.START_STICKY;
    }

    @Override
    public void onDestroy() {
      //  StartappAlarmManager.cancel(Pi);
        this.unregisterReceiver(BatteryMoniterReceiver);
        LockScreenReceiver.UnRegister_LockScreenReceiver(this);
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        BatteryMoniterReceiver = new BatteryChangeMonitoring();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.setPriority(999);
        this.registerReceiver(BatteryMoniterReceiver, filter);
        LockScreenReceiver.Register_LockScreenReceiver(this);
//        if(Pi != null)
//        StartappAlarmManager.cancel(Pi);

//        StartappAlarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Intent in = new Intent(this, Background_Interstitial_StartApp_Receiver.class);
  //      Pi = PendingIntent.getBroadcast(this,0,in,0);
    //    StartappAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 20000, TimeIntervals.StartAppScreenOn,Pi);

    }
}
