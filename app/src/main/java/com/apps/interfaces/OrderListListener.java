package com.apps.interfaces;

import com.apps.items.ItemMenuCat;
import com.apps.items.ItemOrderList;

import java.util.ArrayList;

public interface OrderListListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemOrderList> arrayListOrderList);
}
