package com.test.acleaner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.the.bestna.cleaner.R;

import java.util.ArrayList;

import adapters.AdapterApp;
import helpers.InfoAplicaciones;
import helpers.InfoApp;
import helpers.InfoCache;
import interfaces.IAdShower;

public class AppActivity_Fragment extends Fragment implements OnItemClickListener {
    static ArrayList<InfoApp> apps;
    private ListView lista;
    private AdapterApp adaptador;
    private Button desintalar;
    private AppDeleted detectarAppDelete;
    private IntentFilter filter;
    private IntentFilter intentFilter;
    private InfoAplicaciones app;
    private Handler handler;
    private InfoApp infoapp;
    private Activity activity;
    private TextView numapp;
    private TextView espacioOcupado;
    private int contador;

  //  private AdView mAdView; in on resume method

    private View parentView;
    private Bundle saveInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_app, container, false);
        // parentView = inflater.inflate(R.layout.activity_app, container, false);
        this.saveInstance = savedInstanceState;
        activity = getActivity();
        init();
        return parentView;
    }


    private void init() {

        lista = (ListView) parentView.findViewById(R.id.list);
        desintalar = (Button) parentView.findViewById(R.id.button1);
        numapp = (TextView) parentView.findViewById(R.id.numeroapp);
        espacioOcupado = (TextView) parentView.findViewById(R.id.cantidad);
        TextView texto = (TextView) parentView.findViewById(R.id.texto);
        TextView espacio = (TextView) parentView.findViewById(R.id.espacio);
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
        Typeface typeface_medium = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface typeface_regular = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
        desintalar.setTypeface(typeface_medium);
        texto.setTypeface(typeface_regular);
        espacio.setTypeface(typeface_regular);
        numapp.setTypeface(typeface_regular);
        espacioOcupado.setTypeface(typeface_regular);
        activity.setProgressBarIndeterminateVisibility(true);
        lista.setOnItemClickListener(this);
        lista.addHeaderView(new View(activity));
        lista.addFooterView(new View(activity));
        //  barraTitulo();
        cargarAplicaciones();
        iniciarHilo();
        desintalarApps();

        // Detecta si una aplicación ha sido desintalada.
        detectarAppDelete = new AppDeleted();
        filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_DELETE);
        filter.addDataScheme("package");
        activity.registerReceiver(detectarAppDelete, filter);

        // Permite remover de la lista la aplicación que ha sido desintalada.
        intentFilter = new IntentFilter();
        intentFilter.addAction("QUITAR");
        activity.registerReceiver(intentReceiver, intentFilter);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.list_app, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                startActivity(new Intent(this, MainActivity.class));
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

//    public void barraTitulo() {
//        ActionBar actionbar = this.getSupportActionBar();
//        actionbar.setIcon(R.drawable.app_bar);
//        SpannableString s = new SpannableString("Application Manager");
//        s.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        actionbar.setTitle(s);
//        actionbar.setDisplayUseLogoEnabled(true);
//        actionbar.setDisplayHomeAsUpEnabled(true);
//
//    }


    public void desintalarApps() {
        desintalar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for (int i = 0; i < apps.size(); i++) {
                    if (apps.get(i).isSelected()) {
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:" + apps.get(i).getPname()));
                        startActivity(intent);
                        ((IAdShower)AppActivity_Fragment.this.getActivity()).ShowAd();
                        flag = true;
                    }
                }

                if (!flag) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.selectApplication) + "", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try {
            builder = new AlertDialog.Builder(activity, R.style.DialogCustomTheme);
        } catch (NoSuchMethodError e) {
            builder = new AlertDialog.Builder(activity);
        }

        builder.setTitle(apps.get(position).getAppname());
        builder.setIcon(apps.get(position).getIcon());

        String msg =
                "Version:" + apps.get(position).getVersionName() + "\n" +
                        "Date:  " + apps.get(position).getFechaInstalled() + "\n" +
                        "Space:" + apps.get(position).getInstallSize();


        builder.setMessage(msg)
                .setPositiveButton("Uninstall", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:" + apps.get(position).getPname()));
                        startActivity(intent);
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();*/
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            actualizarLista();
        }
    };

    public void actualizarLista() {
        for (int i = 0; i < apps.size(); i++) {
            if (apps.get(i).isDelete()) {
                apps.remove(i);
                adaptador.notifyDataSetChanged();
                i--;
            }
        }
    }

    public void iniciarHilo() {
        apps = new ArrayList<InfoApp>();
        app = new InfoAplicaciones(activity, activity.getPackageManager().getInstalledPackages(0));
        app.setHandler(handler);
        app.start();
    }

    @SuppressLint("HandlerLeak")
    public void cargarAplicaciones() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                contador += 1;
                Bundle bundle;
                bundle = msg.getData();
                infoapp = new InfoApp();
                InfoApp i = bundle.getParcelable("info");

                infoapp.setIcon(i.getIcon());
                infoapp.setPname(i.getPname());
                infoapp.setAppname(i.getAppname());
                infoapp.setDataApp(i.getDataApp());
                infoapp.setCacheApp(i.getCacheApp());
                infoapp.setVersionName(i.getVersionName());
                infoapp.setInstallSize(i.getInstallSize());
                infoapp.setFechaInstalled(i.getFechaInstalled());
                infoapp.setEspacioOcupadoCacheCodigo(i.getEspacioOcupadoCacheCodigo());

                apps.add(infoapp);
                adaptador = new AdapterApp(activity, 0, apps);
                lista.setAdapter(adaptador);
                numapp.setText(String.valueOf(contador));
                espacioOcupado.setText(new InfoCache().calculateSize(InfoAplicaciones.totalespacioOcupado));
            }
        };
    }

    @Override
    public void onDestroy() {
        activity.unregisterReceiver(intentReceiver);
        activity.unregisterReceiver(detectarAppDelete);
        super.onDestroy();
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
    }
}
