package com.example.scholzpetr.easyseniorapp;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ScholzPetr on 9.2.2015.
 */
public class CellPhoneAddContact extends ActionBarActivity{
    private EditText displayName;
    private EditText mobileNumber;
    private EditText homeNumber;
    private EditText email;
    private File f;
    private File mediaFile;
    private ImageButton imageButtonSelect;
    private Button buttonAddContact;
    private Bitmap selectedImage;
    private InputStream imageStream;
    private final int SELECT_PHOTO = 1;
    private final int SELECT_PHOTO_CAMERA = 100;
    private final String title = "Přidat fotku!";
    private final String takePhoto = "Vyfotit se";
    private final String chooseFromGallery = "Vybrat z galerie";
    private final String clearImageButton = "Vymazat foto";
    private final String back = "Zpět";
    private final CharSequence[] options = {takePhoto, chooseFromGallery,clearImageButton, back};
    private final CharSequence[] optionsWithPhoto = {takePhoto, chooseFromGallery,clearImageButton, back};
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone_add_contact);

        findView();
        imageButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(optionsWithPhoto);
            }
        });
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    addContact();
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode){
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try{
                        System.out.println("jsem v chatch a try bloku tak co je");
                        Uri imageUri = imageReturnedIntent.getData();
                        imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageButtonSelect.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            case SELECT_PHOTO_CAMERA:
                if (resultCode == RESULT_OK) {
                    try {
                        imageStream = getContentResolver().openInputStream(Uri.fromFile(f));
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        while (selectedImage.getWidth() > 800) {
                            int width = selectedImage.getWidth();
                            int height = selectedImage.getHeight();
                            Bitmap bm = Bitmap.createScaledBitmap(selectedImage,(int) (width/2),(int) (height/2),true);
                            selectedImage = bm;
                        }
                        imageButtonSelect.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }


        }
    }
    private void findView(){
        displayName = (EditText) findViewById(R.id.editTextDisplayName);
        mobileNumber = (EditText) findViewById(R.id.editTextMobileNumber);
        homeNumber = (EditText) findViewById(R.id.editTextHomeNumber);
        email = (EditText) findViewById(R.id.editTextEmail);
        buttonAddContact = (Button) findViewById(R.id.buttonAddContact);
        imageButtonSelect = (ImageButton) findViewById(R.id.imageButtonSelect);
    }
    private void selectImage(final CharSequence[] options) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CellPhoneAddContact.this);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //Kamera fotka nacteni
                if (options[item].equals(takePhoto)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //ziskani aktualniho casu kvuli nazvu souborů
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    //definovani názvu
                    mediaFile = new File(File.separator + "IMG_" + timeStamp + ".jpg");
                    //vyber ulozeni fotky
                    f = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "Camera" + mediaFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, SELECT_PHOTO_CAMERA);

                //Nacteni fotky z galerie
                } else if (options[item].equals(chooseFromGallery)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    System.out.println("tady to musi jit vzdyt to nacte az pak to spadne");
                //vymazani image bitmap
                } else if (options[item].equals(clearImageButton)) {
                    Bitmap b = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                    imageButtonSelect.setImageBitmap(b);
                    dialog.dismiss();
                //tlacitko zpet
                } else if (options[item].equals(back)) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void addContact() {
        ArrayList< ContentProviderOperation > ops = new ArrayList<>();
        int rawContactID = ops.size();
        // adding insert operation to operations list
        // to insert a new raw contact in the table
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        //------------------------------------------------------ Image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(selectedImage != null){
            selectedImage.compress(Bitmap.CompressFormat.PNG,75,stream);
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.IS_SUPER_PRIMARY,1)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,stream.toByteArray()).build());
            try {
                stream.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        //------------------------------------------------------ Names
        if (displayName.getText() != null) {
            ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                displayName.getText().toString()).build());
        }
        //------------------------------------------------------ Mobile Number
        if (mobileNumber.getText() != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber.getText().toString())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        //------------------------------------------------------ Home Numbers
        if (homeNumber.getText() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber.getText().toString())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }
        //------------------------------------------------------ Email
        if (email.getText() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getText().toString())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }
        // Asking the Contact provider to create a new contact
        Context myContext = getApplicationContext();
        ToastShow toastShow = new ToastShow();
        String toastText;
        if(displayName.getText().toString().isEmpty() == false && mobileNumber.getText().toString().isEmpty() == false) {
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                toastText = "Kontakt byl přidán!";
                toastShow.addToast(myContext, toastText, 1);
                this.onRestart();
                this.finish();

            } catch (NullPointerException ev){
                toastText = "Velká fotka";
                toastShow.addToast(myContext, toastText, 1);
            } catch (Exception e) {
                e.printStackTrace();
                toastText = "Kontakt nelze přidat!!!";
                toastShow.addToast(myContext, toastText + e.getMessage(), 1);
            }
        } else {
            toastText = "Nevyplňené udaje";
            toastShow.addToast(myContext, toastText, 1);
        }
    }
}
