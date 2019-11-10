package com.example.oussama_achraf.mplrss.database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    private BaseRss base_rss;

    private static final String LOG = "MyContentProvider";

    private static String authority = "fr.aouessar.charif.rss";
    private static final int CODE_RSS_DATA = 1;
    private static final int CODE_RSS_LINK = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(authority, "rss_data", CODE_RSS_DATA);
        matcher.addURI(authority, "rss_link", CODE_RSS_LINK);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = base_rss.getWritableDatabase();
        int code = matcher.match(uri);
        int count;
        Log.d(LOG, "delete uri=" + uri.toString());
        switch (code) {
            case CODE_RSS_DATA:
                count = db.delete("rss_data",selection, selectionArgs);
                Log.d("RSS DATA DELETE", "-delete Succesfull !");
                break;
            case CODE_RSS_LINK:
                count = db.delete("rss_link",selection, selectionArgs);
                Log.d("RSS LINK DELETE", "-delete Succesfull !");
                break;
            default:
                throw new UnsupportedOperationException("DELETE, not yet implemented code(404)");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        int code = matcher.match(uri);
        switch (code){
            case CODE_RSS_DATA:
                return "vnd.android.cursor.dir/fr.aouessar.charif.rss_data";
            case CODE_RSS_LINK:
                return "vnd.android.cursor.item/fr.aouessar.charif.rss_link";
            default:
                throw new IllegalArgumentException("not yet implemented code(402): " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = base_rss.getWritableDatabase();
        int code = matcher.match(uri);
        long id;
        String path;
        switch (code) {
            case CODE_RSS_DATA:
                id = db.insert("rss_data", null, values);
                path = "rss_data";
                Log.d("RSS DATA INSERT", "-insert Succesfull !");
                break;
            case CODE_RSS_LINK:
                id = db.insert("rss_link", null, values);
                path = "rss_link";
                Log.d("RSS LINK INSERT", "-insert Succesfull !");
                break;
            default:
                throw new UnsupportedOperationException("INSERT, not yet implemented code(400)");
        }
        Uri.Builder builder = (new Uri.Builder()).authority(authority).appendPath(path);
        return ContentUris.appendId(builder, id).build();
    }

    @Override
    public boolean onCreate() {
        base_rss = BaseRss.getInstance(this.getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = base_rss.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor;
        switch (code) {
            case CODE_RSS_DATA:
                cursor = db.query("rss_data", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_RSS_LINK:
                cursor = db.query("rss_link", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                Log.d("Uri provider =", uri.toString());
                throw new UnsupportedOperationException("QUERY, not yet implemented code(401)");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = base_rss.getReadableDatabase();
        int code = matcher.match(uri);
        int i;
        switch (code) {
            case CODE_RSS_DATA:
                i = db.update("rss_data", values, selection, selectionArgs);
                break;
            case CODE_RSS_LINK:
                i = db.update("rss_link", values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(" UPDATE, not yet implemented code(403)" + uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return i;
    }
}