package com.watchmecoding.eazynote.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Drawer {
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemPosition(position);
        }
    }

    private void selectItemPosition(int position) {

    }
}
