package com.apps.interfaces;

import com.apps.items.ItemMenu;
import com.apps.items.ItemMenuCat;

import java.util.ArrayList;

public interface MenuListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemMenu> arrayList_menu);
}
