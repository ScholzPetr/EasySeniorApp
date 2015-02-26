package com.example.scholzpetr.easyseniorapp;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.util.Predicate;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by ScholzPetr on 6.2.2015.
 */
public class Applications extends ListActivity {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private ApplicationAdapter listadapter = null;
    private String helpText;
    private EditText editTextFindApplications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        packageManager = getPackageManager();
        new LoadApplications().execute();
        editTextFindApplications = (EditText) findViewById(R.id.editTextFindApplications);

        editTextFindApplications.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new LoadApplications().execute();
                Applications.this.listadapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextFindApplications.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTextFindApplications.setText("");
                return false;
            }
        });
        editTextFindApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                String clearText = "";
                for (int i = 0;i < helpText.length()-1;i++){
                    clearText = clearText + helpText.charAt(i);
                }
                editTextFindApplications.setText(clearText);
            }
        });
    }
    //pomocná metoda, která slouží k načtení editTextu
    private void findOutText(){
        helpText = editTextFindApplications.getText().toString();
    }
    //metoda slouží k určení pozice applistu a prepnuti na intent package name
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = applist.get(position);
        try {
            Intent intent = packageManager
                    .getLaunchIntentForPackage(app.packageName);

            if (null != intent) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Applications.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(Applications.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    //metoda slouží k vymazání diakritiky kvuli třídění
    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{InCombiningDiacriticalMarks}+","");
        return str;
    }
    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> listApplications) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : listApplications) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    //vyhledavani podle znaků
                    if(info.loadLabel(packageManager).toString().toLowerCase().startsWith(editTextFindApplications.getText().toString().toLowerCase()) == true)     {
                        applist.add(info);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //sort application info
        Collections.sort(applist, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
                return stripDiacritics(lhs.loadLabel(packageManager).toString().toLowerCase()).compareTo(stripDiacritics(rhs.loadLabel(packageManager).toString().toLowerCase()));
            }
        });
        return applist;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;
        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadapter = new ApplicationAdapter(Applications.this,
                    R.layout.snippet_list_row, applist);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Applications.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
