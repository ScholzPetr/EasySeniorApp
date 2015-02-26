package com.example.scholzpetr.easyseniorapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ScholzPetr on 22.2.2015.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {
    private Contact contact;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.listview_contacts, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.textViewTvName);
        TextView tvPhone = (TextView) view.findViewById(R.id.textViewTvDetails);
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.imageViewIvPhoto);

        tvName.setText(contact.name);
        tvPhone.setText("");
        Uri imageContactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contact.id));
        if(hasContactPhoto(getContext(),contact.numbers.get(0).number)){
            InputStream photoStream = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(),imageContactUri);
            BufferedInputStream buf = new BufferedInputStream(photoStream);
            Bitmap myBitmap = BitmapFactory.decodeStream(buf);
            ivPhoto.setImageBitmap(myBitmap);
        }else {
            Drawable myDrawable = getContext().getResources().getDrawable(R.drawable.ic_contact_picture_holo_light);
            ivPhoto.setImageDrawable(myDrawable);
        }

        if (contact.numbers.size() > 0 && contact.numbers.get(0) != null) {
            tvPhone.setText(contact.numbers.get(0).number);
        }
        return view;
    }
    public static boolean hasContactPhoto(Context context, String number) {
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
}