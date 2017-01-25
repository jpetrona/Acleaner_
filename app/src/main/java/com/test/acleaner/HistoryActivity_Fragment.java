package com.test.acleaner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.ClipboardManager;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.the.bestna.cleaner.R;

import java.io.File;
import java.util.ArrayList;

import adapters.AdapterHistory;
import helpers.EntryItem;
import helpers.InfoCache;
import helpers.Item;
import helpers.SectionItem;
import interfaces.IAdShower;

@SuppressWarnings("deprecation")
public class HistoryActivity_Fragment extends Fragment implements OnItemClickListener {
    private ArrayList<Item> items = new ArrayList<Item>();
    private ListView listview = null;
    private Button limpiar = null;
    private AdapterHistory adapter;
    private AlertDialog.Builder builder;
    private AlertDialog.Builder builde;
    private Activity activity;
    public static final Uri BOOKMARKS_URI =
            Uri.parse("content://browser/bookmarks");

 //   private AdView mAdView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private View parentView;
    private Bundle saveInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_history, container, false);
        this.saveInstance = savedInstanceState;
        activity = getActivity();
        init();
        return parentView;
    }


    private void init() {

        limpiar = (Button) parentView.findViewById(R.id.limpiar);
        listview = (ListView) parentView.findViewById(R.id.listView_main);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
        Typeface typeface_medium = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Medium.ttf");
        limpiar.setTypeface(typeface_medium);

        listview.addHeaderView(new View(activity));
        listview.addFooterView(new View(activity));
        //barraTitulo();
        botonlimpiar();
        informacionParaListview();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                super.onAdFailedToLoad(errorCode);
//                mAdView.setVisibility(View.GONE);
//            }
//        });
//        mAdView.loadAd(new AdRequest.Builder().build());
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.history, menu);
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
//        //return true;
//    }
//
//    public void barraTitulo() {
//        ActionBar actionbar = activity.getSupportActionBar();
//        actionbar.setIcon(R.drawable.history_bar);
//        SpannableString s = new SpannableString("Clear history");
//        s.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        actionbar.setTitle(s);
//        actionbar.setDisplayUseLogoEnabled(true);
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        //actionbar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_passlock));
//    }

    public void informacionParaListview() {
        items.add(new SectionItem(activity.getResources().getString(R.string.Browser)));
//        items.add(new EntryItem("Browser", String.valueOf(numeroPaginasVisitadas()) + " Pag", R.drawable.web));

        items.add(new EntryItem(activity.getResources().getString(R.string.Browser), " " + activity.getResources().getString(R.string.Pag), R.drawable.web_icon));

        items.add(new SectionItem(activity.getResources().getString(R.string.Downloads)));
        items.add(new EntryItem(activity.getResources().getString(R.string.Downloads), espacioDescargas(), R.drawable.download_icon));
        items.add(new EntryItem(activity.getResources().getString(R.string.Records), espacioArchivos(), R.drawable.record_icon));

        items.add(new SectionItem(activity.getResources().getString(R.string.Applications)));
        items.add(new EntryItem(activity.getResources().getString(R.string.Clipboard), tamanoPortapapeles(), R.drawable.clipboar_icon));

        adapter = new AdapterHistory(activity, items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

//    public int numeroPaginasVisitadas() {
//
//        String[] proj = new String[]{Browser.BookmarkColumns.TITLE, chromeHistory()};
//        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
//
//
//        Cursor mCur = this.managedQuery(chromeHistory(), proj, sel, null, null);
//        activity.startManagingCursor(mCur);
//        mCur.moveToFirst();
//        int contador = 0;
//
//        if (mCur.moveToFirst() && mCur.getCount() > 0) {
//            while (mCur.isAfterLast() == false) {
//                contador += 1;
//                mCur.moveToNext();
//            }
//        }
//        return contador;
//    }

    public String chromeHistory() {
        String[] proj = new String[]{"title", "url"};
        Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
        String sel = "bookmark = 0";
        Cursor mCur = activity.getContentResolver().query(uriCustom, proj, sel, null, "date" + " ASC");
        String url = "";
        if (mCur.moveToFirst()) {
            do {
                url = mCur.getString(mCur.getColumnIndex("url"));
            } while (mCur.moveToNext());
        }
        mCur.close();
        return url;
    }

    public String espacioDescargas() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        long tamanoDescarga = 0;
        if (file != null) {
            if (file.listFiles() != null) {
                for (File f : file.listFiles()) {
                    if (!f.isDirectory()) {
                        tamanoDescarga += f.length();
                    }
                }
            }
        }
        return new InfoCache().calculateSize(tamanoDescarga);
    }

    public void DeleteEspacioDescargas(File file) {
        if (!file.isDirectory()) {
            file.delete();
        }
    }

    public String espacioArchivos() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        long tamanoDescarga = 0;
        if (file != null) {
            if (file.listFiles() != null) {
                for (File f : file.listFiles()) {
                    if (f.isDirectory()) {
                        for (File d : f.listFiles()) {
                            tamanoDescarga += d.length();
                        }
                    }
                }
            }
        }
        return new InfoCache().calculateSize(tamanoDescarga);
    }

    public void DeleteEspacioArchivos(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                DeleteEspacioArchivos(child);
            }
        }
        file.delete();
    }


    public String tamanoPortapapeles() {
        final ClipboardManager portapapeles = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
        if (portapapeles != null && portapapeles.hasText()) {
            return new InfoCache().calculateSize(portapapeles.getText().length());
        } else {
            return "0 Bytes";
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

        position = position - 1;
        EntryItem item = (EntryItem) items.get(position);
        CheckBox box = (CheckBox) view.findViewById(R.id.checkBox_ram);

        if (item.title.equals(activity.getResources().getString(R.string.Records))) {
            if (!item.isSelected()) {
                mensajeAlerta(item, box);
            } else {
                item.setSelected(false);
                box.setChecked(item.isSelected());
            }
        } else {
            if (!item.isSelected()) {
                item.setSelected(true);
                box.setChecked(item.isSelected());
            } else {
                item.setSelected(false);
                box.setChecked(item.isSelected());
            }
        }


    }

    @SuppressLint("NewApi")
    public void mensajeAlerta(final EntryItem item, final CheckBox box) {
        try {
            builder = new AlertDialog.Builder(activity, R.style.DialogCustomTheme);
        } catch (NoSuchMethodError e) {
            builder = new AlertDialog.Builder(activity);
        }

        builder.setTitle(activity.getResources().getString(R.string.doesFiles));
        builder.setIcon(R.drawable.alerta);
        builder.setMessage(activity.getResources().getString(R.string.musicfiles) + "" + "\n\n" + activity.getResources().getString(R.string.onceDelete) + "")

                .setPositiveButton(activity.getResources().getString(R.string.Include) + "", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        item.setSelected(true);
                        box.setChecked(item.isSelected());
                    }
                })

                .setNegativeButton(activity.getResources().getString(R.string.Exclude) + "", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        item.setSelected(false);
                        box.setChecked(item.isSelected());
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
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
                    Toast.makeText(activity, activity.getResources().getString(R.string.needToSelectOne) + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class LimpiarDatos extends AsyncTask<Void, Void, Boolean> {
        private Activity activity;
        private ArrayList<Item> items;
        ProgressDialog dialogo;
        boolean browserSeleccionado;
        boolean descargaSeleccionado;
        boolean archivosSeleccionado;
        boolean portapapelesSele;
        String espacioArchivos;
        String espacioDescarga;


        public LimpiarDatos(Activity activity, ArrayList<Item> items) {
            this.activity = activity;
            this.items = items;
            this.browserSeleccionado = false;
        }

        @Override
        protected void onPreExecute() {
            try {
                dialogo = new ProgressDialog(activity, R.style.DialogCustomTheme);
            } catch (NoSuchMethodError e) {
                dialogo = new ProgressDialog(activity);
            }

            dialogo.setMessage(activity.getResources().getString(R.string.cleaning));
            dialogo.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Looper.prepare();
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).isSection()) {
                    EntryItem ite = (EntryItem) items.get(i);
                    if (ite.isSelected()) {
                        if (ite.title.equals(activity.getResources().getString(R.string.Browser))) {
                            this.browserSeleccionado = true;
                            //   Browser.clearHistory(getContentResolver());
                            ite.setCantidad("0 " + activity.getResources().getString(R.string.Pag));
                            ite.setSelected(false);
                        } else if (ite.title.equals(activity.getResources().getString(R.string.Downloads))) {
                            descargaSeleccionado = true;
                            espacioDescarga = espacioDescargas();
                            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            for (File f : file.listFiles()) {
                                DeleteEspacioDescargas(f);
                            }
                            ite.setCantidad("0 Bytes");
                            ite.setSelected(false);
                        } else if (ite.title.equals(activity.getResources().getString(R.string.Records))) {
                            archivosSeleccionado = true;
                            espacioArchivos = espacioArchivos();
                            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            for (File f : file.listFiles()) {
                                DeleteEspacioArchivos(f);
                            }
                            ite.setCantidad("0 Bytes");
                            ite.setSelected(false);
                        } else if (ite.title.equals(activity.getResources().getString(R.string.Clipboard))) {
                            portapapelesSele = true;
                            final ClipboardManager portapapeles = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
                            portapapeles.setText("");
                            ite.setCantidad("0 Bytes");
                            ite.setSelected(false);
                        }
                    }
                }
            }
            return true;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialogo.dismiss();
            adapter.notifyDataSetChanged();

            try {
                builde = new AlertDialog.Builder(activity, R.style.DialogCustomTheme);
            } catch (NoSuchMethodError e) {
                builde = new AlertDialog.Builder(activity);
            }

            builde.setTitle(activity.getResources().getString(R.string.deviceCleaner) + "");
            builde.setIcon(R.drawable.dialog_clean_icon);

            String mensaje;
            String m1 = "", m2 = "", m3 = "", m4 = "";

            if (this.browserSeleccionado) {
                m1 = msjBrowser();
            }

            if (this.descargaSeleccionado) {
                m2 = msjDescarga(espacioDescarga);
            }

            if (this.archivosSeleccionado) {
                m3 = msjArchivos(espacioArchivos);
            }

            if (this.portapapelesSele) {
                m4 = msjPortapales();
            }

            mensaje = (m1 + m2 + m3 + m4);
            /*builde.setMessage(mensaje);
            builde
                    .setCancelable(false)
                    .setNeutralButton("Accept",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }
                    );
            AlertDialog alertDialog = builde.create();
            alertDialog.show();*/

            Manager.Dialog.showDialogMsg(activity, activity.getResources().getString(R.string.deviceCleaner), mensaje);
            ((IAdShower)HistoryActivity_Fragment.this.getActivity()).ShowAd();
        }
    }

    public String msjBrowser() {
        return activity.getResources().getString(R.string.histroyBrowse) + "" + "\n";
    }

    public String msjDescarga(String espacio) {
        return activity.getResources().getString(R.string.Hewiped) + " " + espacio + " " + activity.getResources().getString(R.string.downHistory) + "\n";
    }

    public String msjArchivos(String espacio) {
        return activity.getResources().getString(R.string.Hewiped) + " " + espacio + " " + activity.getResources().getString(R.string.mediafile) + "\n";
    }

    public String msjPortapales() {
        return activity.getResources().getString(R.string.wipeClip) + "";
    }

    public void onDestroy() {
        super.onDestroy();

    }

}
