package com.example.scholzpetr.easyseniorapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ScholzPetr on 25.1.2015.
 */
public class CellPhone extends ActionBarActivity {
    private TextView lastCall1;
    private TextView lastCall2;
    private TextView lastCall3;
    private TextView lastCall4;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone);
        findWidgets();

        // add PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

//        getCallDetails();

        lastCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:"+ lastCall1.getText().toString();
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                    startActivity(dialIntent);
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(),"Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        lastCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:"+ lastCall2.getText().toString();
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                    startActivity(dialIntent);
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(),"Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        lastCall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:"+ lastCall3.getText().toString();
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                    startActivity(dialIntent);
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(),"Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        lastCall4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:"+ lastCall4.getText().toString();
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                    startActivity(dialIntent);
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(),"Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

//    private void getCallDetails() {
//        int iterator = 0;
//        StringBuffer sb = new StringBuffer();
//        Uri contacts = CallLog.Calls.CONTENT_URI;
//        Cursor managedCursor = getContentResolver().query(contacts, null, null, null,null);
//        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
//        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
//        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
//        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
//        if(managedCursor.getCount() == 0) {
//            managedCursor.moveToLast();
//            do {
//                if (iterator < 4) {
//                    String callNumber = managedCursor.getString(number);
//                    String callType = managedCursor.getString(type);
//                    String callDate = managedCursor.getString(date);
//                    String callDuration = managedCursor.getString(duration);
//                    String dir = null;
//                    int dircode = Integer.parseInt(callType);
//                    switch (dircode) {
//                        case CallLog.Calls.OUTGOING_TYPE:
//                            dir = "OUTGOING";
//                            if (iterator == 0) lastCall1.setBackgroundColor(Color.GREEN);
//                            if (iterator == 1) lastCall2.setBackgroundColor(Color.GREEN);
//                            if (iterator == 2) lastCall3.setBackgroundColor(Color.GREEN);
//                            if (iterator == 3) lastCall4.setBackgroundColor(Color.GREEN);
//                            break;
//                        case CallLog.Calls.INCOMING_TYPE:
//                            dir = "INCOMING";
//                            if (iterator == 0) lastCall1.setBackgroundColor(Color.BLUE);
//                            if (iterator == 1) lastCall2.setBackgroundColor(Color.BLUE);
//                            if (iterator == 2) lastCall3.setBackgroundColor(Color.BLUE);
//                            if (iterator == 3) lastCall4.setBackgroundColor(Color.BLUE);
//                            break;
//                        case CallLog.Calls.MISSED_TYPE:
//                            dir = "MISSED";
//                            if (iterator == 0) lastCall1.setBackgroundColor(Color.RED);
//                            if (iterator == 1) lastCall2.setBackgroundColor(Color.RED);
//                            if (iterator == 2) lastCall3.setBackgroundColor(Color.RED);
//                            if (iterator == 3) lastCall4.setBackgroundColor(Color.RED);
//                            break;
//                    }
//                    if (iterator == 0) {
//                        lastCall1.setText(callNumber);
//                    }
//                    if (iterator == 1) {
//                        lastCall2.setText(callNumber);
//                    }
//                    if (iterator == 2) {
//                        lastCall3.setText(callNumber);
//                    }
//                    if (iterator == 3) {
//                        lastCall4.setText(callNumber);
//                    }
//                }
//                iterator++;
//            }
//            while (managedCursor.moveToPrevious());
//
//            managedCursor.close();
//            System.out.println(sb);
//        }
//    }

    public void clickOnButtonChooseContact(View view){
        Intent intent = new Intent(this, CellPhoneChooseContact.class);
        startActivity(intent);
    }
    public void clickOnButtonDialNumber(View view){
        Intent intent = new Intent(this, CellPhoneDialNumber.class);
        startActivity(intent);
    }
    public void clickOnButtonFavorite(View view){
        Intent intent = new Intent(this, CellPhoneFavorite.class);
        startActivity(intent);
    }
    public void clickOnButtonAddContact(View view){
        Intent intent = new Intent(this, CellPhoneAddContact.class);
        startActivity(intent);
    }
    private void findWidgets(){
        lastCall1 = (TextView) findViewById(R.id.textViewLastCall1);
        lastCall2 = (TextView) findViewById(R.id.textViewLastCall2);
        lastCall3 = (TextView) findViewById(R.id.textViewLastCall3);
        lastCall4 = (TextView) findViewById(R.id.textViewLastCall4);
    }
    private class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // phone ringing...
                    Toast.makeText(CellPhone.this, incomingNumber + " calls you",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Toast.makeText(CellPhone.this, "on call...",
                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call

                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall == true) {
                        Toast.makeText(CellPhone.this, "restart app after call",
                                Toast.LENGTH_LONG).show();

                        // restart our application
                        Intent restart = getBaseContext().getPackageManager().
                                getLaunchIntentForPackage(getBaseContext().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);

                        onCall = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

