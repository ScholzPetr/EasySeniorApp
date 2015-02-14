package com.example.scholzpetr.easyseniorapp;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ScholzPetr on 11.2.2015.
 */
public class ToastShow {
    int lengthText;
    public void addToast(Context context, String text, int length) {
        if (length == 0) lengthText = Toast.LENGTH_SHORT;
        if (length == 1) lengthText = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context.getApplicationContext(), text, lengthText);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTextView = (TextView) toastLayout.getChildAt(0);
        toastTextView.setTextSize(30);
        toast.show();
    }
}
