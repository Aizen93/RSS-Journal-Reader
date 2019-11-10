package com.example.oussama_achraf.mplrss.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.oussama_achraf.mplrss.XMLParsingUtils.ItemXml;
import com.example.oussama_achraf.mplrss.XMLParsingUtils.RssElement;

import static android.content.ContentValues.TAG;


import java.util.ArrayList;
import java.util.List;

public class AccesData {

    private final static String TABLE_RSS_DATA = "rss_data";
    private final static String COLONNE_LINK = "link";
    private final static String COLONNE_TITRE = "titre";
    private final static String COLONNE_DESCRIPTION = "description";



    private final static String TABLE_RSS_LINK = "rss_link";
    private final static String COLONNE_ID = "id";
    private final static String COLONNE_HTTP = "lien_http";
    private final static String COLONNE_G_TITRE = "g_titre";
    private final static String COLONNE_G_DESCRIPTION = "g_description";
    private final static String COLONNE_DATE_MODIF = "date_modif";

    private static String authority = "fr.aouessar.charif.rss";

    private ContentResolver contentResolver;

    private List<ItemXml> list_rssdata = new ArrayList<>();
    private List<RssElement> list_rsslink = new ArrayList<>();
    private List<ItemXml> list_recherche = new ArrayList<>();

    public AccesData(Context context){
        contentResolver = context.getContentResolver();
    }

    /**
     * ajoute des données a la table rss_data du Content provider
     */
    public void insertRssData(String link, String titre, String description){
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath(TABLE_RSS_LINK).build();

        final Cursor cur = contentResolver.query(uri, null, null, null,null);
        List<RssElement> list = getAllRssLink(cur);
        int id = list.get(list.size()-1).getId();
        Log.d("show","the is oussama 2  : "+id);


        ContentValues values = new ContentValues();
        values.put(COLONNE_ID, id);
        values.put(COLONNE_LINK, link);
        values.put(COLONNE_TITRE, titre);
        values.put(COLONNE_DESCRIPTION, description);


        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_RSS_DATA);
        uri = builder.build();
        try {
            uri = contentResolver.insert(uri, values);
        } catch (android.database.SQLException e){
            e.printStackTrace();
            Log.i(TAG, "PROBLEME D'INSERTION code(405): "+uri);
        }
        Log.i(TAG, "INSERTION RSS_DATA SUCCESFULL !");
    }

    /**
     * ajoute des données a la table rss_link du Content provider
     * @return -1 si le RSS exist deja ou y a eu une erreur et id du dernier insert sinon
     */
    public int insertRssLink(String http, String gtitre, String gdescription, String date_modif){
        int id = -1;
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath(TABLE_RSS_LINK).build();

        final Cursor cur = contentResolver.query(uri, null, null, null,null);
        List<RssElement> list = getAllRssLink(cur);
        for(int i= 0; i < list.size(); i++){
            if(list.get(i).getLink().equals(http) || list.get(i).getTitre().equals(gtitre)){
                return id;
            }
        }

        ContentValues values = new ContentValues();
        values.put(COLONNE_HTTP, http);
        values.put(COLONNE_G_TITRE, gtitre);
        values.put(COLONNE_G_DESCRIPTION, gdescription);
        values.put(COLONNE_DATE_MODIF, date_modif);

        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_RSS_LINK);
        uri = builder.build();
        try {
            uri = contentResolver.insert(uri, values);
            id = 0;
        } catch (android.database.SQLException e){
            e.printStackTrace();
            Log.i(TAG, "PROBLEME D'INSERTION code(406): "+uri);
        }
        Log.i(TAG, "INSERTION RSS_LINK SUCCESFULL !");

        Log.d("show","the is oussama 1  : "+id);
        return id;
    }

    /**
     * supprime tous les items qui ont pour ID @param id
     * @param id ID reference
     */
    public void deleteRssData(int id){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_RSS_DATA);
        Uri uri = builder.build();

        String selection = COLONNE_ID + " = ? " ;
        String[] selectionArgs = new String[]{String.valueOf(id)};

        try {
            contentResolver.delete(uri,selection,selectionArgs);
        } catch (android.database.SQLException e){
            e.printStackTrace();
            Log.i(TAG, "PROBLEME DE SUPPRESSION code(405): ");
        }
        Log.i(TAG, "DELETE ALL RAW RSS_DATA WHERE ID = "+ id +" SUCCESFULL !");
    }

    /**
     * supprime le fil RSS qui a pour ID @param id
     * @param id ID reference
     */
    public void deleteRssLink(int id){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_RSS_LINK);
        Uri uri = builder.build();

        String selection = COLONNE_ID + " = ? " ;
        String[] selectionArgs = new String[]{id+""};

        try {
            contentResolver.delete(uri,selection,selectionArgs);
        } catch (android.database.SQLException e){
            e.printStackTrace();
            Log.i(TAG, "PROBLEME DE SUPPRESSION code(406): "+uri);
        }
        Log.i(TAG, "DELETE ONE RAW RSS_DATA WHERE ID="+ id +" SUCCESFULL !");
    }

    /**
     * affiche le contenu de la table rss_data via le Content Provider
     * dont l'id est le @param id
     */
    public List<ItemXml> getRssData(Cursor cur) {
        Log.d("GET RSS DATA COUNT",""+cur.getCount());

        list_rssdata.clear();
        if(cur != null && cur.getCount() != 0) {
            String contenu;
            Log.i(TAG, "RSS DATA: VOICI CE QUE J AI TROUVE");
            int cc = 0;
            //Log.d("show","c = "+cc);
            while (cur.moveToNext()) {
                cc++;
                Log.d("show","c = "+cc);
                int idd = cur.getInt(cur.getColumnIndex(COLONNE_ID));
                String link = cur.getString(cur.getColumnIndex(COLONNE_LINK));
                String titre = cur.getString(cur.getColumnIndex(COLONNE_TITRE));
                String description = cur.getString(cur.getColumnIndex(COLONNE_DESCRIPTION));

                ItemXml item = new ItemXml(titre, description, link);
                contenu = idd + "+++"+ link + "+++" + titre + "+++" + description;

                list_rssdata.add(item);
                Log.i(TAG, contenu);
            }
            cur.close();
        }else{
            Log.i(TAG, "IL NYA RIEN A AFFICHER DEPUIS LE RSS_DATA");
        }
        return list_rssdata;
    }

    /**
     * renvoie le contenu d'une ligne de la table rss_link via le Content Provider
     * sous forme lien+++titre+++description+++date
     */
    public RssElement getUnRssLink(int id) {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath(TABLE_RSS_LINK).build();

        String [] columns = new String [] {COLONNE_HTTP, COLONNE_G_TITRE, COLONNE_G_DESCRIPTION};
        String selection = COLONNE_ID + " = ? " ;
        String[] selectionArgs = new String[]{String.valueOf(id)};
        final Cursor cur = contentResolver.query(uri, columns, selection, selectionArgs,null);

        RssElement rss_item = null;
        if(cur != null && cur.getCount() != 0) {
            cur.moveToFirst();
            String lien = cur.getString(cur.getColumnIndex(COLONNE_HTTP));
            String gtitre = cur.getString(cur.getColumnIndex(COLONNE_G_TITRE));
            String gdescription = cur.getString(cur.getColumnIndex(COLONNE_G_DESCRIPTION));
            String date = cur.getString(cur.getColumnIndex(COLONNE_DATE_MODIF));

            rss_item = new RssElement(id, lien, gdescription, date, gtitre);
            String contenu = lien + "+++" + gtitre + "+++" + gdescription + "+++" + date;

            Log.i(TAG, "RSS LINK: VOICI CE QUE J AI TROUVE");
            Log.i(TAG, contenu);
            cur.close();
        }else{
            Log.i(TAG, "IL NYA RIEN A AFFICHER DEPUIS LE RSS_LINK");
        }
        return rss_item;
    }

    /**
     * renvoie le contenu de toute la table rss_link via le Content Provider
     * sous forme d'une ArrayList composé de RssElement sous forme id+++lien+++titre+++description+++date
     */
    public List<RssElement> getAllRssLink(Cursor cur){
        /*Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath(TABLE_RSS_LINK).build();

        final Cursor cur = contentResolver.query(uri, null, null, null,null);*/
        list_rsslink.clear();
        if(cur != null && cur.getCount() != 0) {
            String contenu;
            Log.i(TAG, "RSS LINK: VOICI CE QUE J AI TROUVE");
            while (cur.moveToNext()) {
                int id = cur.getInt(cur.getColumnIndex(COLONNE_ID));
                String lien = cur.getString(cur.getColumnIndex(COLONNE_HTTP));
                String gtitre = cur.getString(cur.getColumnIndex(COLONNE_G_TITRE));
                String gdescription = cur.getString(cur.getColumnIndex(COLONNE_G_DESCRIPTION));
                String date = cur.getString(cur.getColumnIndex(COLONNE_DATE_MODIF));

                RssElement rss_item = new RssElement(id, lien, gdescription, date, gtitre);
                contenu = id + "+++" + lien + "+++" + gtitre + "+++" + gdescription + "+++" + date;

                list_rsslink.add(rss_item);
                Log.i(TAG, contenu);
            }
            cur.close();
        }else{
            Log.i(TAG, "IL NYA RIEN A AFFICHER DEPUIS LE RSS_LINK");
        }
        return list_rsslink;
    }

    /**
     * Recherche un mot dans toute la table rss_data et renvoie une arrayList de tous les items
     * qui contiennent le mot
     * @param mot a rechercher
     * @return ArrayList<ItemXml>
     */
    public List<ItemXml> RechercheMot(String mot, Cursor cur) {
        list_recherche.clear();
        if(cur != null && cur.getCount() != 0) {
            String contenu;
            Log.i(TAG, "VOICI CE QUE J AI TROUVE");
            while (cur.moveToNext()) {
                String lien = cur.getString(cur.getColumnIndex(COLONNE_LINK));
                String titre = cur.getString(cur.getColumnIndex(COLONNE_TITRE));
                String description = cur.getString(cur.getColumnIndex(COLONNE_DESCRIPTION));

                ItemXml item = new ItemXml(titre, description, lien);
                contenu = titre + "+++" + description;
                if(item.getTitle().contains(mot) || item.getDescription().contains(mot) || item.getLink().contains(mot)) {
                    list_recherche.add(item);
                    Log.i(TAG, contenu);
                }
            }
            cur.close();
        }else{
            Log.i(TAG, "IL NYA RIEN A AFFICHER DEPUIS LE RSS_DATA");
        }
        return list_recherche;
    }
}