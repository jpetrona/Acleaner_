package com.test.acleaner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.the.bestna.cleaner.R;

import java.util.ArrayList;

import adapters.AdapterCallsMsg;
import helpers.EntryItem;
import helpers.InfoLlamadas;
import helpers.InfoMensajes;
import helpers.Item;
import helpers.SectionItem;
import interfaces.IAdShower;


public class CallsMsg_Fragment extends Fragment implements OnItemClickListener {
    private InfoLlamadas llamadas;
    private InfoMensajes mensajes;
    private ArrayList<Item> items = new ArrayList<Item>();
    private ListView listview = null;
    private Button limpiar = null;
    private Activity activity;

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
//
//        });
//
//        mAdView.loadAd(new AdRequest.Builder().build());
    }

    private AdapterCallsMsg adapter;
    private AlertDialog.Builder builder;


    private View parentView;
    private Bundle saveInstance;
   // private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_call_msg, container, false);
        this.saveInstance = savedInstanceState;
        activity = getActivity();
        init();
        return parentView;
    }

    private void init() {

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
//
//        });
//
//        mAdView.loadAd(new AdRequest.Builder().build());

        llamadas = new InfoLlamadas(activity);
        mensajes = new InfoMensajes(activity);
        limpiar = (Button) parentView.findViewById(R.id.limpiar);
        listview = (ListView) parentView.findViewById(R.id.listView_main);
        listview.addHeaderView(new View(activity));
        listview.addFooterView(new View(activity));

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
        limpiar.setTypeface(typeface);
        //barraTitulo();
        informacionParaListview();
        botonlimpiar();




    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.llamadas_mensajes, menu);
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
//    }

//    public void barraTitulo() {
//        ActionBar actionbar = this.getSupportActionBar();
//        actionbar.setIcon(R.drawable.call_msg_bar);
//        SpannableString s = new SpannableString("Calls and messages");
//        s.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        actionbar.setTitle(s);
//        actionbar.setDisplayUseLogoEnabled(true);
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        //actionbar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_passlock));
//    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
        position = position - 1;
        EntryItem item = (EntryItem) items.get(position);
        CheckBox box = (CheckBox) view.findViewById(R.id.checkBox_ram);

        if (!item.isSelected()) {
            item.setSelected(true);
            box.setChecked(item.isSelected());
        } else {
            item.setSelected(false);
            box.setChecked(item.isSelected());
        }

    }


    public void informacionParaListview() {
        llamadas.obtenerDetallesLlamadas();
        mensajes.obtenerMensajesRecibidos();
        mensajes.obtenerMensajesEnviados();

        items.add(new SectionItem(getResources().getString(R.string.calls)));
        items.add(new EntryItem(getResources().getString(R.string.incomecall), String.valueOf(llamadas.getLlamadasRecibidas().size()), R.drawable.call_recive));
        items.add(new EntryItem(getResources().getString(R.string.outgoing), String.valueOf(llamadas.getLlamadasSalientes().size()), R.drawable.call_send));
        items.add(new EntryItem(getResources().getString(R.string.misscall), String.valueOf(llamadas.getLlamadasPerdidas().size()), R.drawable.call_cancel));

        items.add(new SectionItem(getResources().getString(R.string.messgetext)));
        items.add(new EntryItem(getResources().getString(R.string.recMessage), String.valueOf(mensajes.getMensajesRecibidos().size()), R.drawable.msg_recive));
        items.add(new EntryItem(getResources().getString(R.string.sendMessage), String.valueOf(mensajes.getMensajesEnviados().size()), R.drawable.msg_send));

        adapter = new AdapterCallsMsg(activity, items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    public void botonlimpiar() {
        limpiar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean haySeleccion = false;
                for (int i = 0; i < items.size(); i++) {
                    if (!items.get(i).isSection()) {
                        EntryItem ite = (EntryItem) items.get(i);
                        if (ite.isSelected()) {
                            haySeleccion = true;
                        }
                    }
                }

                if (haySeleccion) {
                    new LimpiarDatos(activity, items).execute();
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.needtoselect) + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class LimpiarDatos extends AsyncTask<Void, Void, Boolean> {
        private Activity activity;
        private ArrayList<Item> items;
        private int totalRegistros;
        private int registrosLlamadas;
        private int registrosMensajes;
        ProgressDialog dialogo;

        public LimpiarDatos(Activity activity, ArrayList<Item> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        protected void onPreExecute() {
            try {
                dialogo = new ProgressDialog(activity, R.style.DialogCustomTheme);
            } catch (NoSuchMethodError e) {
                dialogo = new ProgressDialog(activity);
            }

            dialogo.setMessage(getResources().getString(R.string.cleaning));
            dialogo.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int e = 0, s = 0, p = 0, r = 0, en = 0;
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).isSection()) {
                    EntryItem ite = (EntryItem) items.get(i);
                    if (ite.isSelected()) {
                        if (ite.title.equals(getResources().getString(R.string.incomecall))) {
                            e = llamadas.getLlamadasRecibidas().size();
                            llamadas.eliminarRegistrosLlamadas(llamadas.getLlamadasRecibidas());
                            ite.setCantidad("0");
                            ite.setSelected(false);
                        } else if (ite.title.equals(getResources().getString(R.string.outgoing))) {
                            s = llamadas.getLlamadasSalientes().size();
                            llamadas.eliminarRegistrosLlamadas(llamadas.getLlamadasSalientes());
                            ite.setCantidad("0");
                            ite.setSelected(false);
                        } else if (ite.title.equals(getResources().getString(R.string.misscall))) {
                            p = llamadas.getLlamadasPerdidas().size();
                            llamadas.eliminarRegistrosLlamadas(llamadas.getLlamadasPerdidas());
                            ite.setCantidad("0");
                            ite.setSelected(false);
                        } else if (ite.title.equals(getResources().getString(R.string.recMessage))) {
                            r = mensajes.getMensajesRecibidos().size();
                            mensajes.eliminarMensajes(mensajes.getMensajesRecibidos());
                            ite.setCantidad("0");
                            ite.setSelected(false);
                        } else if (ite.title.equals(getResources().getString(R.string.sendMessage))) {
                            en = mensajes.getMensajesEnviados().size();
                            mensajes.eliminarMensajes(mensajes.getMensajesEnviados());
                            ite.setCantidad("0");
                            ite.setSelected(false);
                        }
                    }
                }
            }
            this.registrosMensajes = (r + en);
            this.registrosLlamadas = (e + s + p);
            this.totalRegistros = (e + s + p + r + en);
            return true;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialogo.dismiss();
            adapter.notifyDataSetChanged();

            String msg = getResources().getString(R.string.nothingtoClean);

            /*try {
                builder = new AlertDialog.Builder(activity, R.style.DialogCustomTheme);
            } catch (NoSuchMethodError e) {
                builder = new AlertDialog.Builder(activity);
            }

            builder.setTitle("Your device looks cleaner!");
            builder.setIcon(R.drawable.dialog_clean_icon);*/

            if (this.registrosLlamadas != 0) {
                msg = msjRegistroLlamadas(registrosLlamadas) + getResources().getString(R.string.totalDelete) + " :" + String.valueOf(this.totalRegistros);
                //builder.setMessage(msjRegistroLlamadas(registrosLlamadas) + "Total Deleted Records :" + String.valueOf(this.totalRegistros));
            }

            if (this.registrosMensajes != 0) {
                msg = msjRegistroMensajes(registrosMensajes) + getResources().getString(R.string.totalDelete) + " :" + String.valueOf(this.totalRegistros);
                //builder.setMessage(msjRegistroMensajes(registrosMensajes) + "Total Deleted Records :" + String.valueOf(this.totalRegistros));
            }

            if ((this.registrosLlamadas != 0) && (this.registrosMensajes != 0)) {
                msg = msjRegistroLlamadas(registrosLlamadas) + msjRegistroMensajes(registrosMensajes) + getResources().getString(R.string.totalDelete) + " :" + String.valueOf(this.totalRegistros);
                //builder.setMessage(msjRegistroLlamadas(registrosLlamadas) + msjRegistroMensajes(registrosMensajes) + "Total Deleted Records :" + String.valueOf(this.totalRegistros));
            }

            /*builder
                    .setCancelable(false)
                    .setNeutralButton("Accept",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }
                    );
            AlertDialog alertDialog = builder.create();
            alertDialog.show();*/

            Manager.Dialog.showDialogMsg(activity, getResources().getString(R.string.lookCleaner) + "", msg);
            ((IAdShower)CallsMsg_Fragment.this.getActivity()).ShowAd();
        }


        public String msjRegistroLlamadas(int registros) {
            return activity.getResources().getString(R.string.areErased) + "" + String.valueOf(registros) + " " + activity.getResources().getString(R.string.inComOut) + "" + "\n\n";
        }

        public String msjRegistroMensajes(int registros) {
            return activity.getResources().getString(R.string.areErased) + "" + String.valueOf(registros) + " " + activity.getResources().getString(R.string.reSendMess) + " " + "\n\n";
        }
    }
}
