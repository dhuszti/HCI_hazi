package com.example.daniel.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by daniel on 2015.10.27..
 */
public class NotificationReceiverActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // IDE KÉNE A NOTIFICATION GOMBOK LEKEZELÉSE

        Toast.makeText(getApplicationContext(),
                "Do Something NOW",
                Toast.LENGTH_LONG).show();
    }

}
