package com.example.scholzpetr.easyseniorapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraDevice;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    final Context context = this;
    private ImageButton buttonOptions;
    private TextView textViewOptions;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;

    private TextView textViewGetBattery;
    private TextView textViewDatum;

    int millisecond;
    int second;
    int minute;
    //12 h format
    int hour;
    //24 h format
    int hourofday;
    int dayofyear;
    int year;
    int dayofweek;
    int dayofmonth;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewGetBattery = (TextView) findViewById(R.id.textViewGetBattery);
        textViewDatum = (TextView) findViewById(R.id.textViewGetDatum);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        textViewDatum.setText(currentDateTimeString);
        this.registerReceiver(this.mBatInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, UserSettings.class);
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
        if (id == R.id.action_endapp) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);
            textViewGetBattery.setText(String.valueOf(level)+"%");
        }
    };
    public void clickCellPhone(View view){
        Intent intent = new Intent(this, CellPhone.class);
        startActivity(intent);
    }
    public void getTimeDatum(){
        Calendar cal = Calendar.getInstance();
        millisecond = cal.get(Calendar.MILLISECOND);
        second = cal.get(Calendar.SECOND);
        minute = cal.get(Calendar.MINUTE);
        //12 h format
        hour = cal.get(Calendar.HOUR);
        //24 h format
        hourofday = cal.get(Calendar.HOUR_OF_DAY);
        dayofyear = cal.get(Calendar.DAY_OF_YEAR);
        year = cal.get(Calendar.YEAR);
        dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
    }
    public void clickMessages(View view){
        Intent intent = new Intent(this, Messages.class);
        startActivity(intent);
    }
    public void clickInternet(View view){
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("com.android.chrome");
        startActivity(intent);

    }
    public void clickGallery(View view){
        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);
    }
    public void clickCamera(View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }
    /** copy from android developer */
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Camera", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    //Zkontrolovani zdali zařízení má kameru
//    public boolean checkCameraHW(Context context){
//        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//            //má kameru
//            return true;
//        }else{
//            //nemá kameru
//            Toast toast = new Toast(context);
//            toast.setText("Bohužel Vaše zařízení nemá kameru!!!");
//            toast.show();
//            return false;
//
//        }
//    }
    public void clickApplications(View view){
        Intent intent = new Intent(this, Applications.class);
        startActivity(intent);
    }
    public void clickSos(View view){
        Intent intent = new Intent(this, Sos.class);
        startActivity(intent);
    }
    public void clickOnButtonSettingsApplication(View view){
        Intent intent = new Intent(this, UserSettings.class);
        startActivity(intent);
    }
    public void clickSettings(View view){

        buttonOptions = (ImageButton) findViewById(R.id.imageButtonSettings);
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_choose_settings);

                TextView textViewDialogSettings = (TextView) dialog.findViewById(R.id.textViewDialogSettings);

                Button buttonSettingsApplication = (Button) dialog.findViewById(R.id.buttonSettingsApplication);
                buttonSettingsApplication.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickOnButtonSettingsApplication(v);
                    }
                });
                Button buttonSettingsSystem = (Button) dialog.findViewById(R.id.buttonSettingsSystem);
                buttonSettingsSystem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                });
                Button buttonSettingsCancel = (Button) dialog.findViewById(R.id.buttonSettingsCancel);
                buttonSettingsCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

}
