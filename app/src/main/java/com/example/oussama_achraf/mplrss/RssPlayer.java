package com.example.oussama_achraf.mplrss;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oussama_achraf.mplrss.XMLParsingUtils.ItemXml;
import com.example.oussama_achraf.mplrss.XMLParsingUtils.XmlAnalyser;
import com.example.oussama_achraf.mplrss.database.AccesData;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class RssPlayer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static String authority = "fr.aouessar.charif.rss";
    private RecyclerView rssFluxView = null;
    private TextView principalTitle = null;
    private TextView principalDescription = null;
    private ArrayList<ItemXml> items = null;
    private AdapterRssFluxRecyclerViewer myAdapter = null;
    private AccesData accesData = null;

    private final static String COLONNE_ID = "id";
    private final static String COLONNE_LINK = "link";
    private final static String COLONNE_TITRE = "titre";
    private final static String COLONNE_DESCRIPTION = "description";

    android.support.v4.app.LoaderManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_player);

        this.accesData = new AccesData(this);

        rssFluxView = findViewById(R.id.recylerViewRss);
        principalDescription = findViewById(R.id.descriptionRss);
        principalTitle = findViewById(R.id.titleRss);

        /**
         * bind the Rss data into into this activity
         */
        manager = getSupportLoaderManager();
        manager.initLoader(2, null, this);
        // end bindig --
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath("rss_data").build();
        int idd = getIntent().getIntExtra("idOffline",2);
        Log.d("RSSPLAYER", "voici le idd---------"+idd+"--------");
        String [] columns = new String [] {COLONNE_ID, COLONNE_LINK, COLONNE_TITRE, COLONNE_DESCRIPTION};
        String selection = COLONNE_ID + " = ? " ;
        String[] selectionArgs = new String[]{String.valueOf(idd)};
        return new CursorLoader(this, uri, columns,selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 2:
                Log.d("size", "-----------RSSPLAYER onLoadFinished called: cursorCount:" + data.getCount()+"-------------");
                List<ItemXml> listItems = accesData.getRssData(data);
                principalTitle.setText(getIntent().getStringExtra("titleOffline"));
                principalDescription.setText(getIntent().getStringExtra("descriptionOffline"));
                rssFluxView.setLayoutManager(new LinearLayoutManager(this));
                myAdapter = new AdapterRssFluxRecyclerViewer(this,listItems,android.R.layout.simple_list_item_1);
                rssFluxView.setAdapter(myAdapter);
                break;

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        getSupportLoaderManager().restartLoader(2, null, RssPlayer.this).forceLoad();
        super.onResume();
    }
}