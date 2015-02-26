package com.example.scholzpetr.easyseniorapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class CellPhoneChooseContact extends ActionBarActivity {
    private ArrayList<Contact> listContacts;
    private ListView lvContacts;
    private EditText editTextFindContacts;
    private String helpContacts;
    private String contactID;
    private String contactName;
    private String contactNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone_choose_contact);
        findViewText();
        lvContacts = (ListView) findViewById(R.id.listViewContacts);
        listContacts = new ContactFetcher(this).fetchAll();
        final ContactsAdapter adapterContacts = new ContactsAdapter(this, listContacts);
        lvContacts.setAdapter(adapterContacts);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    Intent data = new Intent();
                    data.putExtra("data",adapterContacts.getItem(position).numbers.get(0).number);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Intent intent = new Intent(getBaseContext(), CellPhoneViewContact.class);
                    contactID = adapterContacts.getItem(position).id;
                    contactName = adapterContacts.getItem(position).name;
                    contactNumber = adapterContacts.getItem(position).numbers.get(0).number;
                    intent.putExtra("contactID", contactID);
                    intent.putExtra("contactName", contactName);
                    intent.putExtra("contactNumber", contactNumber);
                    setResult(RESULT_OK, intent);

                    startActivityForResult(intent, 1);
                }
            }
        });

        editTextFindContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterContacts.getFilter().filter(s.toString());
                lvContacts.setAdapter(adapterContacts);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextFindContacts.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTextFindContacts.setText("");
                return false;
            }
        });
        editTextFindContacts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String clearText = "";
                findOutText();
                for (int i = 0; i < helpContacts.length() - 1; i++) {
                    clearText = clearText + helpContacts.charAt(i);
                }
                editTextFindContacts.setText(clearText);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
                recreate();
    }
    private void findViewText(){
        editTextFindContacts = (EditText) findViewById(R.id.editTextFindContacts);
        lvContacts = (ListView) findViewById(R.id.listViewContacts);
    }
    private void findOutText() {
        helpContacts = editTextFindContacts.getText().toString();
    }
}