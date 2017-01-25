package receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.test.acleaner.MainActivity1;
import com.the.bestna.cleaner.R;

public class RamNotification extends BroadcastReceiver {
    public RamNotification() {
    }
    protected NotificationCompat.Builder notification;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try {
            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Intent i = new Intent(context, MainActivity1.class);

            PendingIntent pi = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_ONE_SHOT);
            notification = new NotificationCompat.Builder(context);
            notification.setAutoCancel(true);
            notification.setDefaults(Notification.DEFAULT_ALL);
            notification.setContentTitle(context.getResources().getString(R.string.Ram_title));
            notification.setContentText(context.getResources().getString(R.string.Rame_text));
            notification.setTicker(context.getResources().getString(R.string.Ticker_text));
            notification.setSmallIcon(R.mipmap.ic_launcher);
            notification.setContentIntent(pi);
            notification.setPriority(Notification.PRIORITY_MAX);

            nm.notify(2, notification.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
