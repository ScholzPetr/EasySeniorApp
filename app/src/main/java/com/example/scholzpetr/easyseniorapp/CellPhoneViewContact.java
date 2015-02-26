package com.example.scholzpetr.easyseniorapp;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ScholzPetr on 23.2.2015.
 */
public class CellPhoneViewContact extends ActionBarActivity {
    private TextView textViewDisplayName;
    private TextView textViewNumber;
    private EditText editTextSmsNumber;
    private ImageView imageViewContactPhoto;
    private String displayName = "";
    private String phoneNumber = "";
    private Button buttonCall;
    private Uri contactUri; //contacts unique URI
    private String contactID; //contacts unique ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone_view_contact);
        findViewText();
        // add PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            contactID = bundle.getString("contactID");
            displayName = bundle.getString("contactName");
            phoneNumber = bundle.getString("contactNumber");
            textViewDisplayName.setText(displayName);
            textViewNumber.setText(phoneNumber);
            Uri imageContactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
            contactUri = imageContactUri;
            if(hasContactPhoto(getApplicationContext(),phoneNumber)){
                InputStream photoStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),imageContactUri);
                BufferedInputStream buf = new BufferedInputStream(photoStream);
                Bitmap myBitmap = BitmapFactory.decodeStream(buf);
                imageViewContactPhoto.setImageBitmap(myBitmap);
            }else {
                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_contact_picture_holo_light);
                imageViewContactPhoto.setImageDrawable(myDrawable);
            }
        }
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:" + phoneNumber;
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                    startActivity(dialIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();
    }

    public void clickOnButtonDelete (View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Opravdu chcete tento kontakt smazat?")
                .setTitle(displayName + " - " + phoneNumber)
                .setCancelable(false)
                .setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CellPhoneViewContact.this.finish();
                        deleteContact(getContentResolver(), phoneNumber);
                        ToastShow toastShow = new ToastShow();
                        toastShow.addToast(getApplicationContext(),"Kontakt smazán!",1);
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void clickOnButtonEdit (View view){
        Intent editIntent = new Intent(Intent.ACTION_EDIT, Uri.parse(ContactsContract.Contacts.CONTENT_URI + "/" + contactID));
        editIntent.putExtra("finishActivityOnSaveCompleted", true);
        startActivityForResult(editIntent, 1);
    }
    public void clickOnButtonSms (View view){
        Intent smsIntent = new Intent(this, MessagesSmsNew.class);
        smsIntent.putExtra("editTextSmsNumberKey", phoneNumber);
        startActivity(smsIntent);
    }

    private static long getContactID (ContentResolver contactHelper,String number){
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] projection = {ContactsContract.PhoneLookup._ID};
        Cursor cursor = null;

        try {
            cursor = contactHelper.query(contactUri, projection, null, null,
                    null);

            if (cursor.moveToFirst()) {
                int personID = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                return cursor.getLong(personID);
            }
            return -1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        return -1;
    }
    private static void deleteContact (ContentResolver contactHelper, String number){
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        String[] args = new String[]{String.valueOf(getContactID(contactHelper, number))};

        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
            try {
                contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
    }
    private static boolean hasContactPhoto(Context context, String number) {
        String thumbUri = "";
        String photoId = "";
        String id = "";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        Cursor cursor = context.getContentResolver().query(uri,
                new String[] {ContactsContract.PhoneLookup._ID,ContactsContract.PhoneLookup.PHOTO_ID}, null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            photoId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_ID));
        }
        cursor.close();
        if(!id.equals("") && photoId != null && !photoId.equals(""))
            return true;
        else
            return false;
    }
    private void findViewText () {
        textViewDisplayName = (TextView) findViewById(R.id.textViewDisplayName);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        editTextSmsNumber = (EditText) findViewById(R.id.textTelephoneNumber);
        imageViewContactPhoto = (ImageView) findViewById(R.id.imageViewContactPhoto);
        buttonCall = (Button) findViewById(R.id.buttonCall);
    }

    private void retrieveContactPhoto(){
        Bitmap photo = null;
        try{
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
            if(inputStream != null){
                photo = BitmapFactory.decodeStream(inputStream);
                imageViewContactPhoto.setImageBitmap(photo);
                inputStream.close();
            }
            assert  inputStream != null;
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    private void retrieveContactNumber(){
        String contactNumber = null;
        Cursor cursorID = getContentResolver().query(contactUri, new String[]{ContactsContract.Contacts._ID},
                null,null,null);
        if(cursorID.moveToFirst()){
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        //Usins contactID and now get phone number
        Cursor cursorPhone = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID}, null);
        if(cursorPhone.moveToFirst()){
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        textViewNumber.setText(contactNumber);
        phoneNumber = textViewNumber.getText().toString();
        cursorPhone.close();
    }
    private void retrieveContactName(){
        String contactName = null;
        Cursor cursor = getContentResolver().query(contactUri,null,null,null,null);
        if(cursor.moveToFirst()){
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        textViewDisplayName.setText(contactName);
        displayName = textViewDisplayName.getText().toString();
    }

    private class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // phone ringing...
                    Toast.makeText(CellPhoneViewContact.this, incomingNumber + " calls you",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Toast.makeText(CellPhoneViewContact.this, "on call...",
                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call

                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall == true) {
                        Toast.makeText(CellPhoneViewContact.this, "restart app after call",
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