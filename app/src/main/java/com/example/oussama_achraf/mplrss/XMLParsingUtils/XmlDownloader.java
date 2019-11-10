package com.example.oussama_achraf.mplrss.XMLParsingUtils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oussama_achraf.mplrss.database.AccesData;
import com.example.oussama_achraf.mplrss.fragments.RssFileFragment;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * this pretty class just for check and download stuf relieted to our xml file
 * it uses static methods so by gentle with her
 */

public class XmlDownloader {

    private XmlDownloader(){ /* private constructor just to prevent instanciat it outside the class */ }

    public static void checkInternet(Activity activity){
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            Toast toast = Toast.makeText(activity,"you'are connected to internet",Toast.LENGTH_LONG);
            toast.show();
        }else{
            Toast toast = Toast.makeText(activity,"you'are not connected to internet verify plz !",Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public static void checkDiskPermission(Activity activity)
    {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "No Permissions" , Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        else
        {
            Toast.makeText(activity, "Has Permissions" , Toast.LENGTH_LONG).show();
        }
    }

    public static void download(final Activity activity, String link, final ProgressBar progressBar, final TextView textViewLoading
            , final Button cancelButton, final RssFileFragment rssFile){


        final DownloadManager dm = (DownloadManager)activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(link);
        DownloadManager.Request request = new DownloadManager.Request(uri);


        request.setTitle("downloading file : "+uri.getLastPathSegment());
        request.setDescription("Downloading ....");
        request.setVisibleInDownloadsUi(true);


        request.setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS,
                uri.getLastPathSegment());

         final long  reference = dm.enqueue(request);
         Log.d("download","download path :"+reference+" name : "+uri.getLastPathSegment());








        final IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        final String link1 = link;

        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override

            public void onReceive(Context context, Intent intent) {
                long ref = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (reference == ref){


                    //activity.unregisterReceiver(this);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(reference);
                    final Cursor cursor = dm.query(query);


                    if (cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                        if(path==null){
                            return;
                        }
                        int fileNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                        String savedFilePath = cursor.getString(fileNameIndex);
                        Log.d("download","tha path is "+path);
                        Toast.makeText(context,"path is :"+path,Toast.LENGTH_LONG);
                        Log.d("download","download the file : "
                                +savedFilePath+" is finished .");


                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int sizeTotal = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);


                        AccesData accesData = new AccesData(activity);

                        ArrayList<ItemXml> items = new ArrayList<>();

                        Document document = XmlAnalyser.parseXml(savedFilePath);

                        items = XmlAnalyser.getItemsArray(document);

                        String titlePrincipalRss = XmlAnalyser.getPrincipalTitle(document);
                        String descriptionPrincipalRss = XmlAnalyser.getPrincipalDescription(document);

                        Date currentTime = Calendar.getInstance().getTime();
                        String dateNow = currentTime.toString();
                        int id_1 = accesData.insertRssLink(link1, titlePrincipalRss, descriptionPrincipalRss,dateNow);


                        if(id_1 != -1){
                            for(int i=0; i<items.size() ; i++){
                                accesData.insertRssData(items.get(i).getLink(),
                                        items.get(i).getTitle(),
                                        items.get(i).getDescription());
                            }
                            Toast toast = Toast.makeText(activity,"data is saved",Toast.LENGTH_LONG);
                            toast.show();
                        }else{
                            Toast toast = Toast.makeText(activity,"data is not saved it already exist  ",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        /**
                         * here we have to tell the loader to restart --
                         * and delete the file from the device
                         */
                        rssFile.getLoaderManager().restartLoader(0, null,
                                rssFile).forceLoad();

                        cancelButton.setVisibility(View.GONE);
                        textViewLoading.setText("download successful");

                        File mainPath = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS);


                        String[] tabNames = savedFilePath.split("/");
                        String nameFileDownload = tabNames[tabNames.length - 1];

                        File file = new File(String.valueOf(mainPath),nameFileDownload);
                        file.delete();


                        //boolean b = file.delete();



                    }
                }
            }
        };
        activity.registerReceiver(receiver,filter);
        showLoading(reference,dm,progressBar,textViewLoading,cancelButton,activity,receiver);
        final boolean[] ok = {true};




        }
        public static void showLoading(final long ref, final DownloadManager dm, final ProgressBar progressbar
        , final TextView textViewLoading, final Button cancelButton, final Activity activity, final BroadcastReceiver br){

            // to show loading
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {


                    final boolean[] downloading = {true};

                    while(downloading[0]) {

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //dm.remove(reference);
                                downloading[0] = false;
                                textViewLoading.setText("downloading stoped by you lol!");
                                Log.d("download","stop download by user");
                                activity.unregisterReceiver(br);
                                progressbar.setProgress(0);
                                cancelButton.setVisibility(View.GONE);


                                //dm.remove(reference);


                            }
                        });
                        if(!downloading[0]) break;
                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(ref);

                        Cursor cursor = dm.query(q);
                        cursor.moveToFirst();
                        int bytes_downloaded = cursor.getInt(cursor
                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading[0] = false;
                        }

                       // Log.d("download ", "loading " +
                                //bytes_downloaded +"of total of "+bytes_total);
                        //Log.d("download","pourcentage is :"+dl_progress);
                        progressbar.setProgress(dl_progress);
                        String loading = "poucentage is "+dl_progress+"% ..";
                        textViewLoading.setText(loading);
                        if(!downloading[0]) textViewLoading.setText("stop download by user");
                        cursor.close();
                    }

                }
            });
            t.start();
        }

}
