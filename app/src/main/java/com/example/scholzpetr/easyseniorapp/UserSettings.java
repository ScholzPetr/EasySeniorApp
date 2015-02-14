package com.example.scholzpetr.easyseniorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by ScholzPetr on 24.1.2015.
 */
public class UserSettings extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);


    }
    public void clickUserSettingsSos(View view){
        Intent intent = new Intent(this, UserSettingsSos.class);
        startActivity(intent);
        //startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
