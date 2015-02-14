package com.example.scholzpetr.easyseniorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ScholzPetr on 25.1.2015.
 */
public class Gallery extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


    }
    public void clickOnButtonPhoto(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void clickOnButtonVideo(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.videos");
        startActivity(intent);
    }
    public void clickOnButtonMusic(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.music");
        startActivity(intent);
    }
    public void clickOnButtonDocuments(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.docs.editors.docs");
        startActivity(intent);
    }
    public void clickOnButtonYouTube(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
        startActivity(intent);
    }
    public void clickOnButtonStreamCz(View view) {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.stream.cz.app");
        startActivity(intent);
    }
    public void clickOnButtonAddItem(View view){
        Context context = getApplicationContext();
        CharSequence text = "Pouze v premiové applikaci";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
