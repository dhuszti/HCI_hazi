package com.example.daniel.myapplication;

/**
 * Created by daniel on 2015.10.13..
 */
public interface ListChangedListener {
    void onListItemRemoved(int position);
    void onPutOtherList(int position);
    void onRenameListItem(int position, String updated);
}
