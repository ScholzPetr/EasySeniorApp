package com.example.scholzpetr.easyseniorapp;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ScholzPetr on 25.1.2015.
 */
public class MessagesSmsNew extends ActionBarActivity{

    EditText textTelephoneNumber;
    EditText textTextMessage;
    Button buttonSendMessage;
    TextView textViewCountChar;
    private final int SMS_LENGTH = 160;
    int i = 0;
    int lengthText = 1;
    private final int REQUEST1 = 1;
    int lengthTextBefore = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_messages_sms_new);
        textTelephoneNumber = (EditText) findViewById(R.id.textTelephoneNumber);
        textTextMessage = (EditText) findViewById(R.id.textTextMessage);
        buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
        textViewCountChar = (TextView) findViewById(R.id.textViewCountChar);
        textTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lengthTextBefore = s.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lengthText = s.length();

                if(lengthText >= SMS_LENGTH) {
                    if(lengthText == lengthTextBefore+1){
                        if(lengthText==(SMS_LENGTH+1) || lengthText == ((SMS_LENGTH*2)+1)) {
                            i++;
                        }
                    }
                    if(lengthText == lengthTextBefore-1){
                        if(lengthText==(SMS_LENGTH+1) || lengthText == ((SMS_LENGTH*2)+1)) {
                            i--;

                        }
                        if(lengthText < SMS_LENGTH-20){
                            textViewCountChar.setVisibility(View.GONE);
                        }
                    }

                    textViewCountChar.setVisibility(View.VISIBLE);
                    textViewCountChar.setText((String.valueOf((lengthText-(SMS_LENGTH*(i+1)))*(-1)))+"/"+(i+1));
                }
                if(lengthText > (SMS_LENGTH-20) && lengthText < SMS_LENGTH){
                    textViewCountChar.setVisibility(View.VISIBLE);
                    textViewCountChar.setText(String.valueOf((s.length()-SMS_LENGTH)*(-1)));
                }


            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            textTelephoneNumber.setText(bundle.getString("editTextSmsNumberKey"));
        }
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
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        Context myContext = getApplicationContext();
        if(textTextMessage.getText().toString() != "" && textTelephoneNumber.getText().toString() != "") {
            try {
                // ---when the SMS has been sent---
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "No service",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getBaseContext(), "Null PDU",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getBaseContext(), "Radio off",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SENT));
                // ---when the SMS has been delivered---
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {

                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getBaseContext(), "SMS not delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> msgArray = smsManager.divideMessage(textTextMessage.getText().toString());
                smsManager.sendTextMessage(textTelephoneNumber.getText().toString(), null, textTextMessage.getText().toString(), null, null);
                smsManager.sendMultipartTextMessage(textTelephoneNumber.getText().toString(), null,msgArray, null, null);
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
        Intent intent = new Intent(getApplicationContext(),CellPhoneChooseContact.class);
        intent.putExtra("yes","yes");
        startActivityForResult(intent,REQUEST1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST1 && resultCode==RESULT_OK){
            textTelephoneNumber.setText(data.getStringExtra("data"));
        }
    }
}
