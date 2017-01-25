package receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.na.nacputemp.CpuTempReader;
import com.test.acleaner.MainActivity1;
import com.the.bestna.cleaner.R;

public class  CPUTemperatureNotification extends BroadcastReceiver implements CpuTempReader.TemperatureResultCallback {

    public CPUTemperatureNotification() {
    }
    NotificationManager nm;
    protected NotificationCompat.Builder notification;
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        CpuTempReader.getCPUTemp(this);
        nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
    }
    @Override
    public void callbackResult(CpuTempReader.ResultCpuTemperature resultCpuTemperature) {
        if((float)resultCpuTemperature.getTemperature() >= 50f)
        {
            try {

                Intent i = new Intent(context, MainActivity1.class);
                i.putExtra("cpu",true);
                PendingIntent pi = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_ONE_SHOT);
                notification = new NotificationCompat.Builder(context);
                notification.setAutoCancel(true);
                notification.setDefaults(Notification.DEFAULT_ALL);
                notification.setContentTitle(context.getResources().getString(R.string.cpuTemper_title));
                notification.setContentText(context.getResources().getString(R.string.cpu_text));
                notification.setTicker(context.getResources().getString(R.string.cpuTicker_text));
                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setContentIntent(pi);
                notification.setOnlyAlertOnce(true);
                notification.setPriority(Notification.PRIORITY_MAX);
                ServiceStarterReceiver.Cpuopremized = false;
                nm.notify(3, notification.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(notification!=null && (float)resultCpuTemperature.getTemperature() < 50f){
            ServiceStarterReceiver.Cpuopremized = true;
            nm.cancel(3);
        }
    }
}
