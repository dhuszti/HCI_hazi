package com.example.daniel.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by daniel on 2015.10.13..
 */
public class CustomAdapter extends ArrayAdapter<String> {


    private final Context context;
    private final ArrayList<String> values;
    private ListChangedListener listChangedListener;

    public CustomAdapter(Context context, ArrayList<String> values, ListChangedListener listChangedListener) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
        this.listChangedListener = listChangedListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        //TextView textView = (TextView) rowView.findViewById(R.id.name);
        EditText editText = (EditText) rowView.findViewById(R.id.name);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.chk_box);
        ImageView deleteButton = (ImageView) rowView.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listChangedListener.onListItemRemoved(position);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listChangedListener.onPutOtherList(position);
                }
            }
        });

        // szöveg kijelzése
        editText.setText(values.get(position));

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // szöveg módosítása
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                prefs.edit().putString("autoSave", s.toString()).commit();

                // Execute some code after 2 seconds have passed
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        listChangedListener.onRenameListItem(position, s.toString());
                    }
                }, 3000);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        return rowView;
    }
}
