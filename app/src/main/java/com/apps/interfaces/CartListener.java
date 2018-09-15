package com.apps.interfaces;

import com.apps.items.ItemCart;
import com.apps.items.ItemMenu;

import java.util.ArrayList;

public interface CartListener {
    void onStart();
    void onEnd(String success, String message, ArrayList<ItemCart> arrayList_menu);
}
