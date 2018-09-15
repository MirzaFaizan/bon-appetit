package com.apps.interfaces;

import com.apps.items.ItemCat;

import java.util.ArrayList;

public interface CategoryListener {
    void onStart();
    void onEnd(String success, String message, ArrayList<ItemCat> arrayListCat);
}
