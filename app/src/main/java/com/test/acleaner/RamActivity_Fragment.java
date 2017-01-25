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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.the.bestna.cleaner.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import adapters.AdapterRam;
import helpers.InfoAppOnRam;
import interfaces.IAdShower;

//import com.mastercleaner.helpers.SwipeDismissListViewTouchListener;

/**
 * Created by USER on 27/05/2014.
 */
public class RamActivity_Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<InfoAppOnRam> appsList = null;
    private AdapterRam adapterRam = null;
    /*private List<Integer> pids = null;
    private List<String> packages = null;*/

    private List<ActivityManager.RunningAppProcessInfo> listRunningAppProcessInfo = null;
    private ImageView imageViewNiddle = null;
    private TextView memoryConcurrent = null;
    private float fromDegrees;
    private float toDegrees;
    private long memoryOff = 0;
    private long memoryTotal = 0;
    private long memoryFree = 0;
    private long auxMemoryAcum = 0;
    private MenuItem item = null;
    private Button button = null;
    private RelativeLayout relativeLayoutInit = null;
    private RelativeLayout relativeLayoutFinish = null;
    private ImageView imageFinish;
    private TextView memoryFreeAndTotal = null;

    private View parentView;
    private Bundle saveInstance;
    private Activity activity;
  //  private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_ram, container, false);
        this.saveInstance = savedInstanceState;
        activity=getActivity();
        init();
        return parentView;
    }


    private void init() {
try{
        ListView listView = (ListView)parentView.findViewById(R.id.list);
        imageViewNiddle = (ImageView) parentView.findViewById(R.id.aguja);
        imageFinish = (ImageView)parentView.findViewById(R.id.image_finish);
        memoryConcurrent = (TextView)parentView.findViewById(R.id.CPU_Temp);
        memoryFreeAndTotal = (TextView)parentView.findViewById(R.id.memory_on_off);
        TextView textAvalible = (TextView)parentView.findViewById(R.id.text_avalible);
        relativeLayoutInit = (RelativeLayout)parentView.findViewById(R.id.init);
        relativeLayoutFinish = (RelativeLayout)parentView.findViewById(R.id.finish);
        final TextView textViewFinish = (TextView)parentView.findViewById(R.id.text_finish);
        final TextView textViewSize = (TextView)parentView.findViewById(R.id.text_size);

        button = (Button)parentView.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewSize.setText(auxMemoryAcum / 1024L + " MB");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final StringBuilder cadena = new StringBuilder();

                        //final long memoryRelace = auxMemoryAcum;

                        Log.d("TAG", "@@@--> value: " + memoryOff);

                        for (int i = 0; i < appsList.size(); i++) {
                            if (((CheckBox) appsList.get(i).view.findViewById(R.id.checkBox_ram)).isChecked()) {

                                cadena.append(appsList.get(i).getAppName()).append("\n");

                                ActivityManager activityManager = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);

                                //activityManager.killBackgroundProcesses(packages.get(i));

                                ActivityManager.RunningAppProcessInfo info = listRunningAppProcessInfo.get(i);

                                for (int k = 0; k < info.pkgList.length; k++) {
                                    activityManager.killBackgroundProcesses(info.pkgList[k]);
                                }

                                android.os.Process.killProcess(info.pid);

                                auxMemoryAcum -= appsList.get(i).getSize();
                                memoryOff -= appsList.get(i).getSize() * 1024L;
                                //appsList.remove(i);

                                //listRunningAppProcessInfo.remove(i);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //adapterRam.notifyDataSetChanged();
                                        memoryConcurrent.setText(auxMemoryAcum / 1024L + " MB");
                                        ((IAdShower)RamActivity_Fragment.this.getActivity()).ShowAd();
                                    }
                                });
                                try {
                                    Thread.sleep(20);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(RamActivity.this, cadena.toString(), Toast.LENGTH_LONG).show();

                                if (cadena.length() != 0) {

                                    cadena.append("--> App released");
                                    adapterRam.notifyDataSetChanged();

                                    float cien = 253f;

                                    Log.i("TAG", "@@@--> app: " + cadena.toString());

                                    toDegrees = (posRule(auxMemoryAcum, memoryTotal) * cien) / 100;

                                    Animation rotate = createAnimationRotate(800, fromDegrees, toDegrees);
                                    rotate.reset();
                                    imageViewNiddle.startAnimation(rotate);
                                    fromDegrees = toDegrees;

                                }

                                Animation outTransparent = AnimationUtils.loadAnimation(activity, R.anim.out_transparent);
                                relativeLayoutInit.startAnimation(outTransparent);
                                relativeLayoutInit.setVisibility(View.GONE);
                                Animation inTransparent = AnimationUtils.loadAnimation(activity, R.anim.in_transparent);
                                inTransparent.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        Animation rotation = AnimationUtils.loadAnimation(activity, R.anim.animation);
                                        imageFinish.startAnimation(rotation);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                relativeLayoutFinish.startAnimation(inTransparent);
                                relativeLayoutFinish.setVisibility(View.VISIBLE);

                            }
                        });

                    }
                });

                thread.start();
            }
        });

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");

        textViewFinish.setTypeface(typeface);

        textViewSize.setTypeface(typeface);

        memoryConcurrent.setTypeface(typeface);

        memoryFreeAndTotal.setTypeface(typeface);

        textAvalible.setTypeface(typeface);

        appsList = new LinkedList<InfoAppOnRam>();

        listView.setOnItemClickListener(this);

        /*pids = new LinkedList<Integer>();

        packages = new LinkedList<String>();*/

        listRunningAppProcessInfo = new LinkedList<ActivityManager.RunningAppProcessInfo>();

        adapterRam = new AdapterRam(activity, 0, appsList);

        listView.setAdapter(adapterRam);

       // barraTitulo();
    }catch(Exception e){e.printStackTrace();}}
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
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                super.onAdFailedToLoad(errorCode);
//                mAdView.setVisibility(View.GONE);
//            }
//        });
//        mAdView.loadAd(new AdRequest.Builder().build());
        button.setEnabled(false);

        ActivityManager activityManager = (ActivityManager)activity.getSystemService(activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        relativeLayoutFinish.setVisibility(View.INVISIBLE);
        relativeLayoutInit.setVisibility(View.VISIBLE);
        auxMemoryAcum = 0;

        try {
            memoryTotal = Manager.AccessMemory.getMemoryTotal();
            memoryFree = Manager.AccessMemory.getMemoryFree();
            memoryOff = memoryTotal - memoryFree;

        } catch (IOException e) {
            e.printStackTrace();
        }
try{
        memoryOff = memoryTotal - memoryFree;

        memoryFreeAndTotal.setText(memoryFree / 1024L + " MB / " + memoryTotal / 1024L + " MB");

        fromDegrees = .0f;

        toDegrees = .0f;

        Animation rotate = createAnimationRotate(1, fromDegrees, toDegrees);

        rotate.reset();

        imageViewNiddle.startAnimation(rotate);

        fromDegrees = toDegrees;

        new LoadProcess().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        item = menu.findItem(R.id.ignore_list);
//        if (item != null) {
//            item.setVisible(false);
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

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

//    public void barraTitulo() {
//        android.support.v7.app.ActionBar actionbar = this.getSupportActionBar();
//        actionbar.setIcon(R.drawable.ram_bar);
//        SpannableString s = new SpannableString("Opimizer Ram");
//        s.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        actionbar.setTitle(s);
//        actionbar.setDisplayUseLogoEnabled(true);
//        actionbar.setDisplayHomeAsUpEnabled(true);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                startActivity(new Intent(this, MainActivity.class));
//                return true;
//            case R.id.ignore_list:
//                startActivity(new Intent(this, ExceptionAppActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
    public  void startExceptionActivity(){
        startActivity(new Intent(activity, ExceptionAppActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        String msg = activity.getResources().getString(R.string.freeRamMem)+" " + appsList.get(position).getAppName() + "?";

        final int p = position;

        View.OnClickListener acept = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityManager activityManager = (ActivityManager)activity.getSystemService(activity.ACTIVITY_SERVICE);

                ActivityManager.RunningAppProcessInfo info = listRunningAppProcessInfo.get(p);

                for (int k = 0; k < info.pkgList.length; k++) {
                    activityManager.killBackgroundProcesses(info.pkgList[k]);
                }

                android.os.Process.killProcess(info.pid);

                auxMemoryAcum -= appsList.get(p).getSize();
                memoryConcurrent.setText(auxMemoryAcum / 1024L + " MB");
                appsList.remove(p);
                listRunningAppProcessInfo.remove(p);
                adapterRam.notifyDataSetChanged();
                if (appsList.isEmpty()) {
                    relativeLayoutInit.setVisibility(View.GONE);
                    relativeLayoutFinish.setVisibility(View.VISIBLE);
                }
            }
        };

        View.OnClickListener add = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // esta llamada verifica si existe el shared, si no existe lo creacon la lista por defecto.
                        Manager.SPreferences.defaultSharedP(activity);

                        SharedPreferences sharedPreferences = activity.getSharedPreferences(Manager.SPreferences.SP_NAME_EXCEPTION, Context.MODE_PRIVATE);
                        String lista = sharedPreferences.getString(Manager.SPreferences.SP_LIST_EXCEPTION, null);

                        if(lista == null){
                            Manager.SPreferences.defaultSharedP(activity);
                            lista = sharedPreferences.getString(Manager.SPreferences.SP_LIST_EXCEPTION, null);
                        }

                        try {
                            JSONArray jsonArray = new JSONArray(lista);
                            jsonArray.put(appsList.get(p).getPackageName());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Manager.SPreferences.SP_LIST_EXCEPTION, jsonArray.toString());
                            editor.commit();


                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    appsList.remove(p);

                                    adapterRam.notifyDataSetChanged();

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }
        };

        View.OnClickListener cancel = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        Manager.Dialog.showDialogConfirmation(activity, activity.getResources().getString(R.string.confirm)+"", msg, acept, cancel, add);

    }

    private float posRule(long actual, long limit) {

        return (actual * 100) / limit;

    }

    private class LoadProcess extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            try{
            activity.setProgressBarIndeterminateVisibility(true);
            appsList.clear();
            /*pids.clear();
            packages.clear();*/
            listRunningAppProcessInfo.clear();
            super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                auxMemoryAcum += (long) memoryInfoArray[0].getTotalPss();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        final InfoAppOnRam infoAppOnRam = new InfoAppOnRam(packName, name, icon1, (long) memoryInfoArray[0].getTotalPss(), pides[0]);

                        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        infoAppOnRam.view = layoutInflater.inflate(R.layout.item_list_ram, null);

                        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");

                        Typeface typeface_medium = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Medium.ttf");

                        TextView appName = (TextView) (infoAppOnRam.view).findViewById(R.id.app_name);

                        TextView infoSize = (TextView) infoAppOnRam.view.findViewById(R.id.delete);//infoAppOnRam.view.findViewById(R.id.delete);

                        ImageView imageView = (ImageView) infoAppOnRam.view.findViewById(R.id.app_icon);

                        final CheckBox checkBox = (CheckBox) infoAppOnRam.view.findViewById(R.id.checkBox_ram);

                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (checkBox.isChecked()) {
                                    auxMemoryAcum += infoAppOnRam.getSize();
                                } else {
                                    auxMemoryAcum -= infoAppOnRam.getSize();
                                }
                                memoryConcurrent.setText(auxMemoryAcum / 1024L + " MB");
                            }
                        });

                        appName.setTypeface(typeface_medium);

                        infoSize.setTypeface(typeface);

                        appName.setText(infoAppOnRam.getAppName());

                        infoSize.setText(infoAppOnRam.getSize() / 1024L + " MB");

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

//                        auxMemoryAcum += (long) memoryInfoArray[0].getTotalPss();
//
//                        final InfoAppOnRam infoAppOnRam = new InfoAppOnRam(packName, name, icon, (long) memoryInfoArray[0].getTotalPss(), pides[0]);
//
//                        LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                        infoAppOnRam.view = layoutInflater.inflate(R.layout.item_list_ram, null);
//
//                        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
//
//                        TextView appName = (TextView) (infoAppOnRam.view).findViewById(R.id.app_name);
//
//                        TextView infoSize = (TextView) infoAppOnRam.view.findViewById(R.id.delete);//infoAppOnRam.view.findViewById(R.id.delete);
//
//                        ImageView imageView = (ImageView) infoAppOnRam.view.findViewById(R.id.app_icon);
//
//                        final CheckBox checkBox = (CheckBox) infoAppOnRam.view.findViewById(R.id.checkBox_ram);
//
//                        checkBox.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (checkBox.isChecked()) {
//                                    auxMemoryAcum += infoAppOnRam.getSize();
//                                } else {
//                                    auxMemoryAcum -= infoAppOnRam.getSize();
//                                }
//                                memoryConcurrent.setText(auxMemoryAcum / 1024L + " MB");
//                            }
//                        });
//
//                        appName.setTypeface(typeface);
//
//                        infoSize.setTypeface(typeface);
//
//                        appName.setText(infoAppOnRam.getAppName());
//
//                        infoSize.setText(infoAppOnRam.getSize() / 1024L + " MB");
//
//                        if (infoAppOnRam.getDrawableIcon() != null)
//                            imageView.setImageDrawable(infoAppOnRam.getDrawableIcon());
//
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                appsList.add(infoAppOnRam);
//                            }
//                        });


            } else {
                name = packName;
                Log.d("Executed services", name);
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    memoryConcurrent.setText(auxMemoryAcum / 1024L + " MB");
                }
            });

        }

            }
        }catch (Exception e){e.printStackTrace();}
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if (aBoolean) {

                adapterRam.notifyDataSetChanged();

                Log.d("TAG", "@@@Espacio ocupado " + memoryOff);

                Log.d("TAG", "@@@Espacio ocupado Aux " + auxMemoryAcum);

                Log.d("TAG", "@@@Espacio maximo " + memoryTotal);

                button.setEnabled(true);

                float cien = 253f;

                toDegrees = (posRule(auxMemoryAcum, memoryTotal) * cien) / 100;

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
