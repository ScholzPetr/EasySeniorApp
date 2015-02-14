package com.example.scholzpetr.easyseniorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by ScholzPetr on 25.1.2015.
 */
public class Messages extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

    }
    public void clickOnButtonSms(View view){
        Intent intent = new Intent(this, MessagesSms.class);
        startActivity(intent);
    }
    public void clickOnButtonEmail(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        startActivity(intent);
    }
    public void clickOnButtonFacebook(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.facebook.orca");
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
