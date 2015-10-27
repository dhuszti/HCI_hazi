package com.example.daniel.myapplication;

import java.util.ArrayList;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends NotificationReceiverActivity implements
        ListChangedListener{

    private ListView lv;

    private EditText etNewText;

    private ArrayList<String> shoppingList;
    private CustomAdapter adapter;
    private ArrayList<String> fridgeList;

    // Search EditText
    private EditText basketSearch;

    // késleltetéshez szükséges
    private Handler handlerUI = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNewText = (EditText) findViewById(R.id.etNewItem);
        fridgeList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listview);
        displayList();

        onSearchList();

        randomNotification();

    }

    public void createNotification(String message, String product) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle(message)
                .setContentText(product)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(
                        android.R.drawable.ic_input_add,
                        "Yes",
                        PendingIntent.getActivity(getApplicationContext(), 0,getIntent(), 0, null))

                .addAction(
                        android.R.drawable.ic_delete,"No",
                        PendingIntent.getActivity(getApplicationContext(), 0,getIntent(), 0, null)).build();

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, noti);

    }

    public void onSearchList() {
        // Keresés funkció
        basketSearch = (EditText) findViewById(R.id.basketSearch);

        // search function
        basketSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                MainActivity.this.adapter.getFilter().filter(cs.toString());
                //Toast.makeText(MainActivity.this, "msg", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                startActivity(new Intent(this, FridgeActivity.class).putStringArrayListExtra("fridge_list", fridgeList));
                return true;
        }
    }

    private void displayList() {

        final ListView listview = (ListView) findViewById(R.id.listview);
        String[] values = new String[] { "Kenyér", "Sör", "Sajt"};

        shoppingList = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            shoppingList.add(values[i]);
        }
        adapter = new CustomAdapter(this, shoppingList, this);
        listview.setAdapter(adapter);

    }

    public void onAddItem(View view) {
        String itemText = etNewText.getText().toString();
        shoppingList.add(0, itemText);
        adapter.notifyDataSetChanged();
        etNewText.setText("");
    }

    @Override
    public void onListItemRemoved(int position) {
        shoppingList.remove(position);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onPutOtherList(final int position) {

        // 3 másodperc után adódik csak a hűtőhöz a termék (kicsit bugos lehet)
        handlerUI.postDelayed(new Runnable() {
            @Override
            public void run() {
                fridgeList.add(shoppingList.get(position).toString());
                shoppingList.remove(position);
                adapter.notifyDataSetChanged();
            }
        }, 3000);


    }


    @Override
    public void onRenameListItem(final int position, String updated) {
        shoppingList.remove(position);
        shoppingList.add(position, updated);
        adapter.notifyDataSetChanged();
    }


    // Lejárt a termék, ekkor kitörlöm és küldök egy értesítést
    public void FridgeItemExpired () {
        Random rand = new Random();
        int sListsize = fridgeList.size();
        int position = rand.nextInt((sListsize - 0) + 1);

        if (sListsize > 0) {
            createNotification("Lejárt: " + fridgeList.get(position), "Szeretné hozzáadni a bevásárlólistához");
            fridgeList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    // A termék elfogyott a hűtőből
    public void FridgeItemEaten () {
        Random rand = new Random();
        int sListsize = fridgeList.size();
        int position = rand.nextInt((sListsize - 0) + 1);

        if (sListsize > 0) {
            createNotification("Elfogyott:" + fridgeList.get(position), "Szeretné hozzáadni a bevásárlólistához?");
            fridgeList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    public void randomNotification() {
        Random rand =  new Random();
        handlerUI.postDelayed(new Runnable() {
            @Override
            public void run() {
                FridgeItemEaten();
            }
        }, 60000);
    }

}