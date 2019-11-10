package com.example.oussama_achraf.mplrss.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oussama_achraf.mplrss.AdapterRssFluxRecyclerHistoryViewer;
import com.example.oussama_achraf.mplrss.R;
import com.example.oussama_achraf.mplrss.XMLParsingUtils.RssElement;
import com.example.oussama_achraf.mplrss.XMLParsingUtils.XmlDownloader;
import com.example.oussama_achraf.mplrss.database.AccesData;

import java.util.ArrayList;
import java.util.List;

public class RssFileFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String authority = "fr.aouessar.charif.rss";
    private Button cancelButton;
    private Button showHideButton;
    private TextView textViewLoading;
    private EditText editTextLink;
    private ImageView buttonDownload;
    private FloatingActionButton buttonSearch;
    private ProgressBar progressBar;
    public RecyclerView recyclerView;

    private AdapterRssFluxRecyclerHistoryViewer myAdapter;
    private AccesData accesData;
    final Activity ac = getActivity();
    private int showHistory = 1;
    LoaderManager manager;
    List<RssElement> rssitems;




    public RssFileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        manager = getLoaderManager();
        manager.initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public static RssFileFragment newInstance(String param1, String param2) {
        RssFileFragment fragment = new RssFileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inf = inflater.inflate(R.layout.fragment_rss_file, container, false);

        this.cancelButton = inf.findViewById(R.id.cancelButton);
        this.cancelButton.setVisibility(View.GONE);
        this.showHideButton = inf.findViewById(R.id.showHideButton);
        this.textViewLoading = inf.findViewById(R.id.textViewLoading);
        this.editTextLink = inf.findViewById(R.id.editTextLink);
        this.buttonDownload = inf.findViewById(R.id.buttonDownload);
        this.buttonSearch = inf.findViewById(R.id.searchIcon);
        this.progressBar = inf.findViewById(R.id.progressBar);
        this.recyclerView = inf.findViewById(R.id.recyclerView);

        this.accesData = new AccesData(getActivity());
        this.rssitems = new ArrayList<>();
        XmlDownloader.checkInternet(getActivity());
        XmlDownloader.checkDiskPermission(getActivity());



        this.buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = editTextLink.getText().toString();
                Log.d("fr",link);

                if(link.isEmpty()){
                    Toast t = Toast.makeText(getActivity(),"please type a valid link",Toast.LENGTH_LONG);
                    t.show();
                    return;
                }

                if(URLUtil.isHttpsUrl(link) || URLUtil.isHttpUrl(link) ) {
                    cancelButton.setVisibility(View.VISIBLE);
                    XmlDownloader.download(getActivity(),link,progressBar,textViewLoading,cancelButton,RssFileFragment.this);

                }else{
                    Toast t = Toast.makeText(getActivity(),"please type a valid http/https url",Toast.LENGTH_LONG);
                    t.show();
                }

            }
        });
        this.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SearchFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container123, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        this.showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showHistory == 1 ){
                    recyclerView.setVisibility(View.GONE);
                    showHistory = 0;
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    showHistory = 1;
                }
            }
        });

        return inf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath("rss_link").build();
        return new CursorLoader(getActivity(), uri, null,null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cur) {
        switch (loader.getId()) {
            case 0:
                Log.d("size", "-----------onLoadFinished called: cursorCount:" + cur.getCount()+"-------------");
                this.rssitems.clear();
                this.rssitems = accesData.getAllRssLink(cur);
                StaggeredGridLayoutManager staggeredGridVertical=new
                        StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridVertical);
                //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
                myAdapter = new AdapterRssFluxRecyclerHistoryViewer(getActivity(),rssitems,android.R.layout.simple_list_item_1, null);
                recyclerView.setAdapter(myAdapter);
                break;

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d("size","+++++++++++++++++++++++++++++++entrez dans le reset");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("resume","hello again");
        getLoaderManager().restartLoader(0, null,
                RssFileFragment.this).forceLoad();

    }
}
