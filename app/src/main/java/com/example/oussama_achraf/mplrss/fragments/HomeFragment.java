package com.example.oussama_achraf.mplrss.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oussama_achraf.mplrss.R;

public class HomeFragment extends Fragment {

    private Button aboutButton = null;
    private Button rssFileButton = null;


    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        aboutButton = (Button) rootView.findViewById(R.id.about);
        rssFileButton = (Button) rootView.findViewById(R.id.rssFile);

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAbout();
            }
        });
        rssFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRssFile();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void goToAbout() {

        Fragment fragment = new AboutFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container123, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void goToRssFile() {
        Fragment fragment = new RssFileFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container123, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
