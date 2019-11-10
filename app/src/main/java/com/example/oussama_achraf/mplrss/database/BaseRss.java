package com.example.oussama_achraf.mplrss.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseRss extends SQLiteOpenHelper {

    private static BaseRss ourInstance;

    public final static int VERSION = 9;
    public final static String DB_NAME = "base_rss";


    public final static String TABLE_RSS_LINK = "rss_link";
    public final static String COLONNE_ID = "id";
    public final static String COLONNE_HTTP = "lien_http";
    public final static String COLONNE_G_TITRE = "g_titre";
    public final static String COLONNE_G_DESCRIPTION = "g_description";
    public final static String COLONNE_DATE_MODIF = "date_modif";

    public final static String CREATE_RSS_LINK =  "create table " + TABLE_RSS_LINK + "(" +
            COLONNE_ID + " integer primary key autoincrement not null, " +
            COLONNE_HTTP + " string, " +
            COLONNE_G_TITRE + " string, " +
            COLONNE_G_DESCRIPTION + " text, " +
            COLONNE_DATE_MODIF + " text " +");";

    public final static String TABLE_RSS_DATA = "rss_data";
    public final static String COLONNE_LINK = "link";
    public final static String COLONNE_TITRE = "titre";
    public final static String COLONNE_DESCRIPTION = "description";


    public final static String CREATE_RSS_DATA = "create table " + TABLE_RSS_DATA + "(" +
            COLONNE_ID + " integer, " +
            COLONNE_LINK + " string, " +
            COLONNE_TITRE + " string, " +
            COLONNE_DESCRIPTION + " text " +");";


    public static BaseRss getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new BaseRss(context);
        return ourInstance;
    }

    private BaseRss(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RSS_DATA);
        db.execSQL(CREATE_RSS_LINK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + TABLE_RSS_DATA);
            db.execSQL("drop table if exists " + TABLE_RSS_LINK);
            onCreate(db);
        }
    }
}