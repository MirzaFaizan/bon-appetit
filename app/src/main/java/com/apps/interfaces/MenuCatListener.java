package com.apps.interfaces;

import com.apps.items.ItemMenuCat;
import com.apps.items.ItemRestaurant;

import java.util.ArrayList;

public interface MenuCatListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenuCat> arrayList_menucat);
}
