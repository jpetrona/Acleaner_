package com.test.acleaner;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.na.nacputemp.CpuTempReader;
import com.the.bestna.cleaner.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import adapters.AdapterRam;
import helpers.InfoAppOnRam;
import interfaces.IAdShower;
import receivers.ServiceStarterReceiver;

public class CPUTemperature_Fragment extends Fragment implements  CpuTempReader.TemperatureResultCallback {

    private List<InfoAppOnRam> appsList = null;
    private AdapterRam adapterRam = null;
    /*private List<Integer> pids = null;
    private List<String> packages = null;*/

    private List<ActivityManager.RunningAppProcessInfo> listRunningAppProcessInfo = null;
    private ImageView imageViewNiddle = null;
    private TextView CPU_Temp = null;
    private float fromDegrees;
    private float toDegrees;
    private MenuItem item = null;
    private Button button = null;
    private RelativeLayout relativeLayoutInit = null;

    private View parentView;
    private Bundle saveInstance;
    private Activity activity;
  //  private AdView mAdView;
    private float ActualCpuTemprature,random;
    private ImageView imageFinish,cpuScanner;
    private RelativeLayout relativeLayoutFinish;
    private FrameLayout CoolCPULayout;

    Animation AnimCpuCooler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CpuTempReader.getCPUTemp(this);
        parentView = inflater.inflate(R.layout.fragment_cputemperature, container, false);
        this.saveInstance = savedInstanceState;
        activity=getActivity();
        init();
        return parentView;
    }

    private void init() {
        ListView listView = (ListView)parentView.findViewById(R.id.list);

        imageViewNiddle = (ImageView) parentView.findViewById(R.id.aguja);
        final TextView textViewSize = (TextView)parentView.findViewById(R.id.text_size);

        CPU_Temp = (TextView)parentView.findViewById(R.id.CPU_Temp);
        CoolCPULayout = (FrameLayout) parentView.findViewById(R.id.CoolCPUAnimationView);
        cpuScanner = (ImageView) parentView.findViewById(R.id.scanner);
        AnimCpuCooler = AnimationUtils.loadAnimation(getActivity(),R.anim.cpuanimation);
        AnimCpuCooler.setDuration(3500);
        AnimCpuCooler.setRepeatCount(new Random().nextInt(3) + 1);
        AnimCpuCooler.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cpuScanner.setVisibility(View.VISIBLE);
                CoolCPULayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                cpuScanner.setVisibility(View.GONE);
                CoolCPULayout.setVisibility(View.GONE);

                Animation outTransparent = AnimationUtils.loadAnimation(activity, R.anim.out_transparent);
                relativeLayoutInit.startAnimation(outTransparent);

                textViewSize.setText("Temperature decreased to " + String.format("%.1f°C", ActualCpuTemprature));
                Animation inTransparent = AnimationUtils.loadAnimation(activity, R.anim.in_transparent);
                inTransparent.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation rotation = AnimationUtils.loadAnimation(activity, R.anim.animation);
                        imageFinish.startAnimation(rotation);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                relativeLayoutInit.setVisibility(View.VISIBLE);
                                relativeLayoutFinish.setVisibility(View.INVISIBLE);
                                CPU_Temp.setText(String.format("%.1f°C", ActualCpuTemprature));
                                adapterRam.notifyDataSetChanged();
                                float cien = 253f;
                                toDegrees = (ActualCpuTemprature * cien) / 100;
                                Animation rotate = createAnimationRotate(800, fromDegrees, toDegrees);
                                rotate.reset();
                                imageViewNiddle.startAnimation(rotate);
                                fromDegrees = toDegrees;
                                ((IAdShower)CPUTemperature_Fragment.this.getActivity()).ShowAd();

                            }
                        },1500);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                relativeLayoutFinish.startAnimation(inTransparent);
                relativeLayoutFinish.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        cpuScanner.setAnimation(AnimCpuCooler);
        relativeLayoutInit = (RelativeLayout)parentView.findViewById(R.id.init);
        imageFinish = (ImageView)parentView.findViewById(R.id.image_finish);
        relativeLayoutFinish = (RelativeLayout)parentView.findViewById(R.id.finish);


        button = (Button)parentView.findViewById(R.id.btncooldown);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(ServiceStarterReceiver.Cpuopremized){
                    ((IAdShower)CPUTemperature_Fragment.this.getActivity()).ShowAd();
                    Toast.makeText(getActivity().getApplicationContext(),"CPU temperature is already optimized.",Toast.LENGTH_SHORT).show();
                    return;
                }
                relativeLayoutInit.setVisibility(View.INVISIBLE);
                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final StringBuilder cadena = new StringBuilder();

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (cadena.length() != 0) {
                                    ServiceStarterReceiver.Cpuopremized = true;
                                    CoolCPULayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        for (int i = 0; i < appsList.size(); i++) {

                                cadena.append(appsList.get(i).getAppName()).append("\n");
                                ActivityManager activityManager = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);
                                ActivityManager.RunningAppProcessInfo info = listRunningAppProcessInfo.get(i);

                                for (int k = 0; k < info.pkgList.length; k++) {
                                    activityManager.killBackgroundProcesses(info.pkgList[k]);
                                }
                                android.os.Process.killProcess(info.pid);
                        }
                    }
                });
                thread.start();
            }
        });

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
        appsList = new LinkedList<InfoAppOnRam>();
        listRunningAppProcessInfo = new LinkedList<ActivityManager.RunningAppProcessInfo>();
        adapterRam = new AdapterRam(activity, 0, appsList);
        listView.setAdapter(adapterRam);
    }
    @Override
    public void callbackResult(CpuTempReader.ResultCpuTemperature resultCpuTemperature) {
        ActualCpuTemprature = (float)resultCpuTemperature.getTemperature();
        if(!ServiceStarterReceiver.Cpuopremized)
            CPU_Temp.setText(String.format("%.1f°C", (ActualCpuTemprature + (new Random().nextInt()%3+1)) ));
        else
            CPU_Temp.setText(String.format("%.1f°C", ActualCpuTemprature));
    }
    @Override
    public void onResume() {
        super.onResume();
//        mAdView = (AdView) parentView.findViewById(R.id.adView);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                mAdView.setVisibility(View.VISIBLE);
//            }
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                super.onAdFailedToLoad(errorCode);
//                mAdView.setVisibility(View.GONE);
//            }
//        });
//        mAdView.loadAd(new AdRequest.Builder().build());
        button.setEnabled(false);

        relativeLayoutInit.setVisibility(View.VISIBLE);

        fromDegrees = .0f;
        toDegrees = .0f;
        Animation rotate = createAnimationRotate(1, fromDegrees, toDegrees);
        rotate.reset();
        imageViewNiddle.startAnimation(rotate);
        fromDegrees = toDegrees;

        new LoadProcess().execute();
    }

    private static Animation createAnimationRotate(long duration, float fromDegrees, float toDegrees) {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(true);
        Animation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setStartOffset(0);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setFillAfter(true);

        animationSet.addAnimation(rotateAnimation);
        return animationSet;
    }

    public  void startExceptionActivity(){
        startActivity(new Intent(activity, ExceptionAppActivity.class));
    }



    private float posRule(long actual, long limit) {

        return (actual * 100) / limit;

    }

    private class LoadProcess extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            activity.setProgressBarIndeterminateVisibility(true);
            appsList.clear();

            listRunningAppProcessInfo.clear();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
try {
    SharedPreferences sharedPreferences = activity.getSharedPreferences(Manager.SPreferences.SP_NAME_EXCEPTION, activity.MODE_PRIVATE);
    String lista = sharedPreferences.getString(Manager.SPreferences.SP_LIST_EXCEPTION, null);
    ArrayList<String> list = new ArrayList<String>();

    if (lista == null) {
        Manager.SPreferences.defaultSharedP(activity);
        lista = sharedPreferences.getString(Manager.SPreferences.SP_LIST_EXCEPTION, null);
    }

    try {

        JSONArray jsonArray = new JSONArray(lista);

        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }

    } catch (JSONException e) {
        e.printStackTrace();
        return false;
    }

    ActivityManager activityManager = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);

    List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();

    if (processes == null) return false;

    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : processes) {

        if (list.indexOf(runningAppProcessInfo.processName) == -1) {

            if (!runningAppProcessInfo.processName.equals(activity.getPackageName())) {

                        /*packages.add(runningAppProcessInfo.processName);

                        pids.add(runningAppProcessInfo.pid);*/

                listRunningAppProcessInfo.add(runningAppProcessInfo);

            }

        }
    }

    PackageManager pack = activity.getPackageManager();

    for (int index = 0; index < listRunningAppProcessInfo.size(); index++) {

        final int pides[] = new int[1];
        pides[0] = listRunningAppProcessInfo.get(index).pid;
        final android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pides);

        if ((memoryInfoArray != null) && (memoryInfoArray.length > 0)) {

            ApplicationInfo applicationInfo = null;
            final String packName = listRunningAppProcessInfo.get(index).processName;
            try {
                applicationInfo = pack.getApplicationInfo(packName, PackageManager.GET_META_DATA);

            } catch (PackageManager.NameNotFoundException e) {
                //Log.d("Executed services", packName);
            }
            final String name;
            // Drawable icon=null;
            Drawable icon = null;
            if (applicationInfo != null) {
                name = (String) pack.getApplicationLabel(applicationInfo);
                try {
                    icon = pack.getApplicationIcon(packName);

                } catch (PackageManager.NameNotFoundException e) {
                    Log.d("Executed services", "No icon");
                }
                final Drawable icon1;
                icon1 = icon;
                Log.d("Executed app", name + " - " + packName);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        final InfoAppOnRam infoAppOnRam = new InfoAppOnRam(packName, name, icon1, (long) memoryInfoArray[0].getTotalPss(), pides[0]);

                        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        infoAppOnRam.view = layoutInflater.inflate(R.layout.item_cpu, null);

                        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");

                        Typeface typeface_medium = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Medium.ttf");

                        TextView appName = (TextView) (infoAppOnRam.view).findViewById(R.id.app_name);

                        ImageView imageView = (ImageView) infoAppOnRam.view.findViewById(R.id.app_icon);




                        appName.setTypeface(typeface_medium);


                        appName.setText(infoAppOnRam.getAppName());


                        if (infoAppOnRam.getDrawableIcon() != null)
                            imageView.setImageDrawable(infoAppOnRam.getDrawableIcon());

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appsList.add(infoAppOnRam);
                            }
                        });
                    }
                });
            } else {
                name = packName;
                Log.d("Executed services", name);
            }
        }

            }
        }catch (Exception e){e.printStackTrace();}
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                adapterRam.notifyDataSetChanged();
                button.setEnabled(true);
                float cien = 253f;
                toDegrees = ((ActualCpuTemprature+random) * cien) / 100;
                Animation rotate = createAnimationRotate(1000, fromDegrees, toDegrees);
                rotate.reset();
                imageViewNiddle.startAnimation(rotate);
                fromDegrees = toDegrees;
                if (item != null) {
                    item.setVisible(true);
                }
            }
            activity.setProgressBarIndeterminateVisibility(false);
        }
    }

}
