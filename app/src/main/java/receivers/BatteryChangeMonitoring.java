package receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.support.v7.app.NotificationCompat;

import com.test.acleaner.MainActivity1;
import com.the.bestna.cleaner.R;

/**
 * Created by Numan on 4/21/2016.
 */
public class BatteryChangeMonitoring extends BroadcastReceiver
{
    private NotificationCompat.Builder n;
    private static NotificationManager nm;
    private Intent i;
    private int prevPercent = 0;
    @Override
    public void onReceive(Context context, Intent intent)
    {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryPct = Math.round(100 * (level / (float) scale));


            if ((level != -1 && scale != -1) && (batteryPct < 20))
            {
                Intent i = new Intent(context, MainActivity1.class);
                i.putExtra("battery",true);
                PendingIntent pi = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

                n = new NotificationCompat.Builder(context);
                n.setAutoCancel(true);
                n.setDefaults(Notification.DEFAULT_ALL);
                n.setContentTitle(context.getString(R.string.Low_Battery_Title));
                n.setContentText(context.getString(R.string.Low_Battery_Content_Text));
                n.setTicker(context.getText(R.string.Low_Battery_Ticker_Text));
                n.setOnlyAlertOnce(true);
                n.setOngoing(true);
                n.setPriority(Notification.PRIORITY_MAX);
                n.setSmallIcon(R.mipmap.ic_launcher);
                n.setContentIntent(pi);

                nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                nm.notify(123, n.build());
            }
            else if(nm != null)
                    nm.cancel(123);
    }
}
