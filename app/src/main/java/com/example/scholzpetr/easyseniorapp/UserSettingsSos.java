package com.example.scholzpetr.easyseniorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ScholzPetr on 24.1.2015.
 */
public class UserSettingsSos extends ActionBarActivity {

    Button buttonSos;
    private int pom = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_sos);

        buttonSos = (Button) findViewById(R.id.buttonSos);
        String io = (String) buttonSos.getText();
        buttonSos.setText("AHoj");

    }
    public void clickButtonSos(View view){
        buttonSos.setText("hooj");

    }

}
