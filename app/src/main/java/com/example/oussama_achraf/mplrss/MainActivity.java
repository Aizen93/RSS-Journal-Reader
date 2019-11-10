package com.example.oussama_achraf.mplrss;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.oussama_achraf.mplrss.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                manager.beginTransaction();
        manager.beginTransaction()
                .add(R.id.container123, new HomeFragment())
                .commit();

    }
    /*
    public void goOnline(View view){
        startActivity(new Intent(this,RssFile.class));
    }
    public void goAbout(View view) {
        startActivity(new Intent(this,AboutActivity.class));
    }
*/
}
