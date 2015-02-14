package com.example.scholzpetr.easyseniorapp;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.File;
import java.io.FileOutputStream;

public class CellPhoneChooseContact extends ActionBarActivity {
    private EditText editTextFindContacts;
    private String helpContacts;
    private ListView listViewContacts;
    private SimpleCursorAdapter mAdapter;
    private MatrixCursor mMatrixCursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone_choose_contact);
        mMatrixCursor = new MatrixCursor(new String[] {"_id","name","photo","details"});
        mAdapter = new SimpleCursorAdapter(getBaseContext(),
                R.layout.listview_contacts,null,new String[]{"name","photo","details"},new int[]{
                R.id.textViewTvName,R.id.textViewTvPhoto,R.id.textViewTvDetails},0);
        findViewText();
        listViewContacts.setAdapter(mAdapter);
        ListViewContactsLoader listViewContactsLoader = new ListViewContactsLoader();
        listViewContactsLoader.execute();


//        editTextFindContacts.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                editTextFindContacts.setText("");
//                return false;
//            }
//        });
//        editTextFindContacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                findOutText();
//                String clearText = "";
//                for (int i = 0;i < helpContacts.length()-1;i++){
//                    clearText = clearText + helpContacts.charAt(i);
//                }
//                editTextFindContacts.setText(clearText);
//            }
//        });
    }
    private class ListViewContactsLoader extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;

            // Querying the table ContactsContract.Contacts to retrieve all the contacts
            Cursor contactsCursor = getContentResolver().query(contactsUri, null, null, null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

            if(contactsCursor.moveToFirst()){
                do{
                    long contactId = contactsCursor.getLong(contactsCursor.getColumnIndex("_ID"));

                    Uri dataUri = ContactsContract.Data.CONTENT_URI;

                    // Querying the table ContactsContract.Data to retrieve individual items like
                    // home phone, mobile phone, work email etc corresponding to each contact
                    Cursor dataCursor = getContentResolver().query(dataUri, null,
                            ContactsContract.Data.CONTACT_ID + "=" + contactId,
                            null, null);

                    String displayName="";
                    String nickName="";
                    String homePhone="";
                    String mobilePhone="";
                    String workPhone="";
                    String photoPath="";
                    byte[] photoByte=null;
                    String homeEmail="";
                    String workEmail="";
                    String companyName="";
                    String title="";

                    if(dataCursor.moveToFirst()){
                        // Getting Display Name
                        displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ));
                        do{

                            // Getting NickName
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
                                nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));

                            // Getting Phone numbers
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)){
                                switch(dataCursor.getInt(dataCursor.getColumnIndex("data2"))){
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
                                        homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
                                        mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
                                        workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                }
                            }

                            // Getting EMails
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ) ) {
                                switch(dataCursor.getInt(dataCursor.getColumnIndex("data2"))){
                                    case ContactsContract.CommonDataKinds.Email.TYPE_HOME :
                                        homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Email.TYPE_WORK :
                                        workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                }
                            }

                            // Getting Organization details
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)){
                                companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                title = dataCursor.getString(dataCursor.getColumnIndex("data4"));
                            }

                            // Getting Photo
                            if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)){
                                photoByte = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));

                                if(photoByte != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);

                                    // Getting Caching directory
                                    File cacheDirectory = getBaseContext().getCacheDir();

                                    // Temporary file to store the contact image
                                    File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"+contactId+".png");

                                    // The FileOutputStream to the temporary file
                                    try {
                                        FileOutputStream fOutStream = new FileOutputStream(tmpFile);

                                        // Writing the bitmap to the temporary file as png file
                                        bitmap.compress(Bitmap.CompressFormat.PNG,100, fOutStream);

                                        // Flush the FileOutputStream
                                        fOutStream.flush();

                                        //Close the FileOutputStream
                                        fOutStream.close();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    photoPath = tmpFile.getPath();
                                }
                            }
                        }while(dataCursor.moveToNext());
                        String details = "";

                        // Concatenating various information to single string
                        if(homePhone != null && !homePhone.equals("") )
                            details = "HomePhone : " + homePhone + "\n";
                        if(mobilePhone != null && !mobilePhone.equals("") )
                            details += "MobilePhone : " + mobilePhone + "\n";
                        if(workPhone != null && !workPhone.equals("") )
                            details += "WorkPhone : " + workPhone + "\n";
                        if(nickName != null && !nickName.equals("") )
                            details += "NickName : " + nickName + "\n";
                        if(homeEmail != null && !homeEmail.equals("") )
                            details += "HomeEmail : " + homeEmail + "\n";
                        if(workEmail != null && !workEmail.equals("") )
                            details += "WorkEmail : " + workEmail + "\n";
                        if(companyName != null && !companyName.equals("") )
                            details += "CompanyName : " + companyName + "\n";
                        if(title != null && !title.equals("") )
                            details += "Title : " + title + "\n";

                        // Adding id, display name, path to photo and other details to cursor
                        mMatrixCursor.addRow(new Object[]{ Long.toString(contactId),displayName,photoPath,details});
                    }
                }while(contactsCursor.moveToNext());
            }
            return mMatrixCursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            // Setting the cursor containing contacts to listview
            mAdapter.swapCursor(result);
        }
    }

    private void findViewText(){
        //editTextFindContacts = (EditText) findViewById(R.id.editTextFindContacts);
        listViewContacts = (ListView) findViewById(R.id.listViewContacts);
    }
    private void findOutText() {
        helpContacts = editTextFindContacts.getText().toString();
    }

}