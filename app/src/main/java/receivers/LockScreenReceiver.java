package receivers;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.the.bestna.cleaner.R;

import java.util.Calendar;

import Utilities.MyPhoneStateListner;
import Utilities.StartAppNativeAds;
import interfaces.IAdClickListener;
import interfaces.ICallStateListner;
import views.WaveHelper;
import views.WaveView;

/**
 * Created by Numan on 5/13/2016.
 */
@SuppressWarnings("ALL")
public class LockScreenReceiver extends BroadcastReceiver  implements View.OnClickListener, ICallStateListner, IAdClickListener {
    private LayoutInflater mInflater;
    private View LockScreenView;
    private at.markushi.ui.CircleButton btn;
    private WindowManager.LayoutParams params;
    private WindowManager mWindowManager;
    private static Boolean PowerConnected = false;
    private static LockScreenReceiver LockReceiver;
    private TextView hours,min,ampm,date,battery,fastCharging;
    private ImageView adImageView;
    private TelephonyManager telephonyManager;
    private ImageView batteryImgView;
    private int batteryPct;
    private static Context _context;
    private WaveView mWaveView;
    private WaveHelper mWaveHelper;
    private FrameLayout ParentFrameLayout;
    private StartAppNativeAds startAppNativeAds;

    private static KeyguardManager km;
    private static KeyguardManager.KeyguardLock kl;


    public static void Register_LockScreenReceiver(Context context){
        LockReceiver = new LockScreenReceiver();

        IntentFilter filter = new IntentFilter();
        filter.setPriority(999);

        _context = context;
        km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        kl =  km .newKeyguardLock("MyKeyguardLock");

        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        context.registerReceiver(LockReceiver,filter);
    }
    public static void UnRegister_LockScreenReceiver(Context context){
        if(LockReceiver != null)
        context.unregisterReceiver(LockReceiver);
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        if (intent.getAction() == Intent.ACTION_POWER_CONNECTED) {
            if(mWindowManager != null)
                UpdateBatteryImage(R.drawable.battery_charging);
            InitScreen(context);
            PowerConnected = true;

            kl.disableKeyguard();
        } else if (intent.getAction() == Intent.ACTION_POWER_DISCONNECTED) {
            kl.reenableKeyguard();
            PowerConnected = false;
            if(mWindowManager != null)
                UpdateBatteryImage(R.drawable.ic_battery_full);
        }
        if (PowerConnected)
        {
            if (intent.getAction() == Intent.ACTION_SCREEN_OFF || intent.getAction() == Intent.ACTION_POWER_CONNECTED) {
                InitScreen(context);
                if(mWaveHelper != null && !mWaveView.getShowWave())
                    mWaveHelper.start();
            }
            if (mWindowManager != null && intent.getAction() == Intent.ACTION_TIME_TICK) {
                UpdateTime(context);
            }
            if (mWindowManager != null && (intent.getAction() == Intent.ACTION_BATTERY_CHANGED) )
            {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                batteryPct = Math.round(100 * (level / (float) scale));
                if ((level != -1 && scale != -1))
                {
                    UpdateBattery(batteryPct);
                    if(battery.getText().toString().startsWith("100"))
                        UpdateBatteryImage(R.drawable.ic_battery_full);
                }
                if(mWaveHelper != null && !mWaveView.getShowWave())
                    mWaveHelper.start();
            }
        }
    }

    private void UpdateBattery(final float level){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    battery.setText(String.valueOf(level).substring(0,String.valueOf(level).length()-2)+"%");
                float waterLevel = level/100f;
                if(level >= 90 && level <= 100){
                    ParentFrameLayout.setBackgroundResource(R.color.ninety_hundred_color_background);
                    mWaveView.setWaveColor(Color.parseColor(_context.getResources().getString(R.color.ninety_hundred_color_behind)),Color.parseColor(_context.getResources().getString(R.color.ninety_hundred_color_front)));
                    mWaveView.setWaterLevelRatio(waterLevel);
                }
                else if(level >= 70 && level < 90){
                    ParentFrameLayout.setBackgroundResource(R.color.seventy_ninety_color_background);
                    mWaveView.setWaveColor(Color.parseColor(_context.getResources().getString(R.color.seventy_ninety_color_behind)),Color.parseColor(_context.getResources().getString(R.color.seventy_ninety_color_front)));
                    mWaveView.setWaterLevelRatio(waterLevel);
                }
                else if(level >= 50 && level < 70){
                    ParentFrameLayout.setBackgroundResource(R.color.fifty_seventy_color_background);
                    mWaveView.setWaveColor(Color.parseColor(_context.getResources().getString(R.color.fifty_seventy_color_behind)),Color.parseColor(_context.getResources().getString(R.color.fifty_seventy_color_front)));
                    mWaveView.setWaterLevelRatio(waterLevel);
                }
                else if(level >= 30 && level < 50){
                    ParentFrameLayout.setBackgroundResource(R.color.thirty_fifty_color_background);
                    mWaveView.setWaveColor(Color.parseColor(_context.getResources().getString(R.color.thirty_fifty_color_behind)),Color.parseColor(_context.getResources().getString(R.color.thirty_fifty_color_front)));
                    mWaveView.setWaterLevelRatio(waterLevel);
                }
                else if(level >= 15 && level < 30){
                    ParentFrameLayout.setBackgroundResource(R.color.fifteen_thirty_color_background);
                    mWaveView.setWaveColor(Color.parseColor(_context.getResources().getString(R.color.fifteen_thirty_color_behind)),Color.parseColor(_context.getResources().getString(R.color.fifteen_thirty_color_front)));
                    mWaveView.setWaterLevelRatio(waterLevel);
                }
                else if(level > 0 && level < 15){
                    ParentFrameLayout.setBackgroundResource(R.color.zero_fifteen_color_background);
                   mWaveView.setWaveColor(Color.parseColor(_context.getResources().getString(R.color.zero_fifteen_color_behind)),Color.parseColor(_context.getResources().getString(R.color.zero_fifteen_color_front)));
                    mWaveView.setWaterLevelRatio(waterLevel);
                }
             }catch (Exception ex){
                    Log.d("Lock Screen Error",ex.getMessage());
                }
            }
        },0);
    }

    private void InitScreen(final Context context) {
        if(mWindowManager != null)
           return;
            mWindowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
            mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            LockScreenView = mInflater.inflate(R.layout.activity_lock_screen, null);

            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                    PixelFormat.TRANSLUCENT);
            params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

            ParentFrameLayout = (FrameLayout) LockScreenView.findViewById(R.id.parenLayout);

            date  = (TextView) LockScreenView.findViewById(R.id.textViewDate);
            hours = (TextView) LockScreenView.findViewById(R.id.hours);
            min   = (TextView) LockScreenView.findViewById(R.id.min);
            ampm  = (TextView) LockScreenView.findViewById(R.id.ampm);
            btn   = (at.markushi.ui.CircleButton) LockScreenView.findViewById(R.id.unlock);
            battery = (TextView) LockScreenView.findViewById(R.id.textViewBattery);
            fastCharging = (TextView) LockScreenView.findViewById(R.id.textViewSuperCharging);

            mWaveView = (WaveView) LockScreenView.findViewById(R.id.wave);
            mWaveView.setBorder(0,Color.BLUE);
            mWaveView.setShapeType(WaveView.ShapeType.SQUARE);

            mWaveHelper = new WaveHelper(mWaveView);
            mWaveView.setWaterLevelRatio(0.0f);


            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            Typeface typeface_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            Typeface typeface_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

            date.setTypeface(typeface);
            hours.setTypeface(typeface_regular);
            min.setTypeface(typeface_regular);
            ampm.setTypeface(typeface_regular);
            fastCharging.setTypeface(typeface_medium);

            btn.setColor(Color.TRANSPARENT);

            btn.setOnClickListener(this);

            battery.setText(String.valueOf(batteryPct)+"%");
            batteryImgView = (ImageView) LockScreenView.findViewById(R.id.imageViewBattery);

            telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(new MyPhoneStateListner(this), PhoneStateListener.LISTEN_CALL_STATE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWindowManager.addView(LockScreenView, params);
                    UpdateTime(context);
                }
            }, 0);
        startAppNativeAds = new StartAppNativeAds(_context, this,(ScrollView)LockScreenView.findViewById(R.id.AdScrollView),R.id.AdTitle,R.id.AdDescription,R.id.AdIcon,R.id.AdimageView);
    }

    @Override
    public void onClick(final View v)
    {
        if(mWindowManager != null)
            new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch(v.getId()) {
                                default:
                                    mWindowManager.removeView(LockScreenView);
                                    mWindowManager = null;
                                    break;
                            }
                        }
                    },0);
    }

    private void UpdateTime(final Context context){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                if (!DateFormat.is24HourFormat(context))
                {
                    int minutes = Calendar.getInstance().get(Calendar.MINUTE);
                    int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    if(minutes < 10)
                        min.setText("0"+minutes);
                    else
                        min.setText(""+minutes);

                    if(hourOfDay>=12)
                    {
                        hours.setText(""+hourOfDay%12);
                        ampm.setText(" PM");
                    } else {
                        hours.setText(""+(hourOfDay==0?"12":hourOfDay));
                        ampm.setText(" AM");
                    }
                }
                else {
                    hours.setText(""+Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    min.setText(""+Calendar.getInstance().get(Calendar.MINUTE));
                    ampm.setText("");
                    }
                java.text.DateFormat df = DateFormat.getDateFormat(context);
                //noinspection deprecation
                date.setText(df.format(calendar.getTime()));
            }
        }, 0);
    }
    private void UpdateBatteryImage(final int resource){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                batteryImgView.setImageResource(resource);
                if(resource == R.drawable.battery_charging)
                    fastCharging.setVisibility(View.VISIBLE);
                else
                    fastCharging.setVisibility(View.GONE);
            }
        }, 0);
    }
    private boolean ringing = false;
    @Override
    public void CallStateRinging() {
        onClick(btn);
        ringing = true;
    }

    @Override
    public void CallStateOffHook() {
    }

    @Override
    public void CallStateIdle() {
    }

    @Override
    public void StartAppNative_AdClicked() {
        mWindowManager.removeView(LockScreenView);
        mWindowManager = null;
    }
}
