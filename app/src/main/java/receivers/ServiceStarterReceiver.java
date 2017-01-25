package receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import Utilities.TimeIntervals;
import activities.AdLauncherActivity;
import services.BackGroundService;


/**
 * Created by Numan on 3/1/2016.
 */
public class ServiceStarterReceiver extends BroadcastReceiver
{
    private static AlarmManager AlarmManager;
    private static PendingIntent AdmobPi,CachePi,RamPi,CPUTemperPi;
    public static boolean Cpuopremized = false;

    private static void RegisterCPUAlarm(Context context){
        try{if(CPUTemperPi == null) {
            Cpuopremized = false;
            Intent cpuTemperIntent = new Intent(context, CPUTemperatureNotification.class);
            CPUTemperPi = PendingIntent.getBroadcast(context, 0,cpuTemperIntent, 0);
            AlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, TimeIntervals.CPUTemperatureNotification, CPUTemperPi);
        }
    }catch (Exception e){
    Log.d("Error",e.getMessage());}
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
                try{
                    Intent activityintent = new Intent(context,AdLauncherActivity.class);
                    activityintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activityintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityintent);
                    AlarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    //////
                    ///         AdMob Background Interstital
                    /////////////
//                    if(AdmobPi == null) {
//                        Intent i = new Intent(context, Background_Interstitial_Admob_Receiver.class);
//                        AdmobPi = PendingIntent.getBroadcast(context, 0, i, 0);
//                        AlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, TimeIntervals.AdMobInterstitial, AdmobPi);
//                    }
                    ////////////////
                    ////        End AdMob Background Interstial
                    //////////////////////////


                    //////
                    ///   Cache Notification
                    /////////////
                    if(CachePi == null) {
                        Intent cacheIntent = new Intent(context, CacheNotification.class);
                        CachePi = PendingIntent.getBroadcast(context, 0, cacheIntent, 0);
                        AlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1800000, TimeIntervals.CacheNotification, CachePi);
                    }
                    ////////////////
                    ////        End Cahce Notification
                    //////////////////////////

                    //////
                    ///         Ram Notification
                    /////////////
                    if(RamPi == null) {
                        Intent RamIntent = new Intent(context, RamNotification.class);
                        RamPi = PendingIntent.getBroadcast(context, 0,RamIntent, 0);
                        AlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3600000, TimeIntervals.RamNotification, RamPi);
                    }
                    ////////////////
                    ////        End Ram Notification
                    //////////////////////////

                    ////////////
                    ///         CPU Temperature Notification
                    /////////////
                    RegisterCPUAlarm(context);
                    ////////////////
                    ////        End CPU Temperature Notification
                    //////////////////////////
                    }
                catch (Exception e){
                    e.printStackTrace();
                }
            context.startService(new Intent(context, BackGroundService.class));
            //context.startService(new Intent(context, FacebookAdService.class));
    }
}
