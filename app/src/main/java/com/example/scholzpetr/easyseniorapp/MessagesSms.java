package com.example.scholzpetr.easyseniorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by ScholzPetr on 25.1.2015.
 */
public class MessagesSms extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_sms);


    }
    public void clickOnButtonNewMessage(View view){
        Intent intent = new Intent(this, MessagesSmsNew.class);
        startActivity(intent);
    }
}
