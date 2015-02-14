package com.example.scholzpetr.easyseniorapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by ScholzPetr on 9.2.2015.
 */
public class CellPhoneFavorite extends ActionBarActivity {
    private TextView callFavorite1;
    private TextView callFavorite2;
    private TextView callFavorite3;
    private TextView callFavorite4;
    private TextView callFavorite5;
    private TextView callFavorite6;
    private TextView callFavorite7;
    private TextView callFavorite8;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone_favorite);
        findWidgets();
    }
    private void findWidgets(){
        callFavorite1 = (TextView) findViewById(R.id.textViewFavorite1);
        callFavorite2 = (TextView) findViewById(R.id.textViewFavorite2);
        callFavorite3 = (TextView) findViewById(R.id.textViewFavorite3);
        callFavorite4 = (TextView) findViewById(R.id.textViewFavorite4);
        callFavorite5 = (TextView) findViewById(R.id.textViewFavorite5);
        callFavorite6 = (TextView) findViewById(R.id.textViewFavorite6);
        callFavorite7 = (TextView) findViewById(R.id.textViewFavorite7);
        callFavorite8 = (TextView) findViewById(R.id.textViewFavorite8);
    }
}
