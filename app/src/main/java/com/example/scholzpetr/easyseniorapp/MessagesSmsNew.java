package com.example.scholzpetr.easyseniorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ScholzPetr on 25.1.2015.
 */
public class MessagesSmsNew extends ActionBarActivity {

    EditText textTelephoneNumber;
    EditText textTextMessage;
    Button buttonSendMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_sms_new);

        textTelephoneNumber = (EditText) findViewById(R.id.textTelephoneNumber);
        textTextMessage = (EditText) findViewById(R.id.textTextMessage);
        buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsByManager();
            }
        });


    }
    public void sendSmsByManager(){
        ToastShow toastShow = new ToastShow();
        String text;
        Context myContext = getApplicationContext();
        if(textTextMessage.getText().toString() != "" && textTelephoneNumber.getText().toString() != "") {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(textTelephoneNumber.getText().toString(), null, textTextMessage.getText().toString(), null, null);
                text = "Sms uspesne odeslána!";
                toastShow.addToast(myContext, text, 1);
                this.finish();
            } catch (Exception e) {
                text = "Sms nebyla odeslána!";
                toastShow.addToast(myContext, text, 1);
                e.printStackTrace();
            }
        } else {
            text = "Pole nejsou vyplňěná!";
            toastShow.addToast(myContext,text,1);
        }
    }
    public void clickChooseContact(View view){
        Intent intent = new Intent(this, CellPhoneChooseContact.class);
        startActivity(intent);
    }
}
