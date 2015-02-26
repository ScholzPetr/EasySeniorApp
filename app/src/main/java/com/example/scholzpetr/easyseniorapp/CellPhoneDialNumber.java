package com.example.scholzpetr.easyseniorapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ScholzPetr on 25.1.2015.
 */
public class CellPhoneDialNumber extends ActionBarActivity {

    private Button buttonNumber0;
    private Button buttonNumber1;
    private Button buttonNumber2;
    private Button buttonNumber3;
    private Button buttonNumber4;
    private Button buttonNumber5;
    private Button buttonNumber6;
    private Button buttonNumber7;
    private Button buttonNumber8;
    private Button buttonNumber9;
    private Button buttonNumberSymbol;
    private Button buttonDial;
    final Context context = this;
    private EditText editTextDialNumber;
    private int length;
    Editable text;
    private String helpDialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone_dial_number);


        findView();
        editTextDialNumber.setInputType(0);  //no keyboard
        editTextDialNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTextDialNumber.setText("");
                return false;
            }
        });
        editTextDialNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                length = editTextDialNumber.getText().length();
                if (length > 0) {
                    editTextDialNumber.setText(editTextDialNumber.getText().delete(length - 1, length).toString());
                }

            }
        });
        buttonNumber0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber0.getText().toString());
            }
        });
        buttonNumber1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber1.getText().toString());
            }
        });
        buttonNumber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber2.getText().toString());
            }
        });
        buttonNumber3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber3.getText().toString());
            }
        });
        buttonNumber4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber4.getText().toString());
            }
        });
        buttonNumber5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber5.getText().toString());
            }
        });
        buttonNumber6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber6.getText().toString());
            }
        });
        buttonNumber7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber7.getText().toString());
            }
        });
        buttonNumber8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber8.getText().toString());
            }
        });
        buttonNumber9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                editTextDialNumber.setText(helpDialNumber + buttonNumber9.getText().toString());
            }
        });
        buttonNumberSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOutText();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_choose_number_symbol);
                Button buttonNumberSymbolPlus = (Button) dialog.findViewById(R.id.buttonNumberPlus);
                buttonNumberSymbolPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextDialNumber.setText(helpDialNumber + "+");
                        dialog.cancel();
                    }
                });
                Button buttonNumberSymbolAsterisk = (Button) dialog.findViewById(R.id.buttonNumberAsterisk);
                buttonNumberSymbolAsterisk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextDialNumber.setText(helpDialNumber + "*");
                        dialog.cancel();
                    }
                });
                Button buttonNumberSymbolDagger = (Button) dialog.findViewById(R.id.buttonNumberDagger);
                buttonNumberSymbolDagger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextDialNumber.setText(helpDialNumber + "#");
                        dialog.cancel();
                    }
                });
                Button buttonNumberSymbolCancel = (Button) dialog.findViewById(R.id.buttonNumberCancel);
                buttonNumberSymbolCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        // add PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);



        buttonDial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:"+ editTextDialNumber.getText().toString();
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
    private void findView(){
        editTextDialNumber = (EditText) findViewById(R.id.editTextDialNumber);

        buttonNumber0 = (Button) findViewById(R.id.buttonNumber0);
        buttonNumber1 = (Button) findViewById(R.id.buttonNumber1);
        buttonNumber2 = (Button) findViewById(R.id.buttonNumber2);
        buttonNumber3 = (Button) findViewById(R.id.buttonNumber3);
        buttonNumber4 = (Button) findViewById(R.id.buttonNumber4);
        buttonNumber5 = (Button) findViewById(R.id.buttonNumber5);
        buttonNumber6 = (Button) findViewById(R.id.buttonNumber6);
        buttonNumber7 = (Button) findViewById(R.id.buttonNumber7);
        buttonNumber8 = (Button) findViewById(R.id.buttonNumber8);
        buttonNumber9 = (Button) findViewById(R.id.buttonNumber9);
        buttonNumberSymbol = (Button) findViewById(R.id.buttonNumberSymbol);
        buttonDial = (Button) findViewById(R.id.buttonDial);
    }
    private void findOutText(){
        helpDialNumber = editTextDialNumber.getText().toString();
    }


    private class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // phone ringing...
                    Toast.makeText(CellPhoneDialNumber.this, incomingNumber + " calls you",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Toast.makeText(CellPhoneDialNumber.this, "on call...",
                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call

                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall == true) {
                        Toast.makeText(CellPhoneDialNumber.this, "restart app after call",
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

