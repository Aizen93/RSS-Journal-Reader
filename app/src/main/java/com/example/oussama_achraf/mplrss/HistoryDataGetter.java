package com.example.oussama_achraf.mplrss;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * right we put the links hardcoding
 * we'll fix that when the database is ready  --
 *
 */

public class HistoryDataGetter {

    private HistoryDataGetter(){}

    public static void fillTheHistoryLinks(Activity activity, ListView listView, final EditText editTextLink){

        ArrayList<String> links = new ArrayList<>();

        links.add("https://www.cnet.com/rss/news/");
        links.add("http://feed.cnet.com/feed/podcast/all/hd.xml/");
        links.add("https://www.cnet.com/rss/gaming/");


        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(activity,android.R.layout.simple_list_item_1,links);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTextLink.setText(adapter.getItem(position));
            }
        });
    }

    public static class AdapterRssFluxHistoryRecyclerViewer {
    }
}
