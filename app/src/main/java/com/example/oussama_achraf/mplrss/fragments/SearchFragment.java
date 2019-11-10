package com.example.oussama_achraf.mplrss.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import com.example.oussama_achraf.mplrss.AdapterRssFluxRecyclerViewer;
import com.example.oussama_achraf.mplrss.R;
import com.example.oussama_achraf.mplrss.XMLParsingUtils.ItemXml;
import com.example.oussama_achraf.mplrss.database.AccesData;

import java.util.List;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static String authority = "fr.aouessar.charif.rss";
    private EditText search;
    private TextView results;
    private ImageView apply;
    private RecyclerView recyclerView;
    private AdapterRssFluxRecyclerViewer myAdapter = null;
    private AccesData accesData;

    //final Context ct = getActivity().getBaseContext();
    android.support.v4.app.LoaderManager manager;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        manager = getLoaderManager();
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View inf = inflater.inflate(R.layout.fragment_search, container, false);

        this.accesData = new AccesData(getActivity());

        this.search = inf.findViewById(R.id.editText);
        this.results = inf.findViewById(R.id.results);
        this.apply = inf.findViewById(R.id.apply);
        this.recyclerView = inf.findViewById(R.id.recyclerView);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        this.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Show", "Apply clicked");
                getLoaderManager().restartLoader(1, null, SearchFragment.this).forceLoad();
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
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content").authority(authority).appendPath("rss_data").build();
        return new CursorLoader(getActivity(), uri, null,null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        Log.d("Show", "onLoadFinished finished: "+data.getCount());
        List<ItemXml> listItems = accesData.RechercheMot(search.getText().toString(), data);
        if(listItems.isEmpty()) results.setText("No results found...");
        else {
            results.setText("Results... " + listItems.size() + " items found !");
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            myAdapter = new AdapterRssFluxRecyclerViewer(getActivity(), listItems, android.R.layout.simple_list_item_1);
            recyclerView.setAdapter(myAdapter);
            search.setText("");
            // just to not start keyboard each time at focus
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }
}
