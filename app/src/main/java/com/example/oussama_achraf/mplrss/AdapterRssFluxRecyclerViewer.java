package com.example.oussama_achraf.mplrss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oussama_achraf.mplrss.XMLParsingUtils.ItemXml;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdapterRssFluxRecyclerViewer extends RecyclerView.Adapter<AdapterRssFluxRecyclerViewer.ViewHolder>{

    private List<ItemXml> rssItems = null;
    private ArrayList<String> rssItemsVisibleOrNot = null;
    private Activity activity = null;

    private int itemLayout;

    public AdapterRssFluxRecyclerViewer(Activity activity,List<ItemXml> items, int itemLayout){
        this.rssItems = items;
        this.itemLayout = itemLayout;
        this.rssItemsVisibleOrNot = new ArrayList<>();
        for(int i=0; i<rssItems.size() ;rssItemsVisibleOrNot.add("VISIBLE"),i++);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_view_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.title_1.setText(rssItems.get(i).getTitle());
        viewHolder.description_1.setText(rssItems.get(i).getDescription());

       // viewHolder.imageView_1.setImageURI(Uri.parse(rssItems.get(i).getImage()));

        /**
         * init
         */
    if(rssItemsVisibleOrNot.get(i).equals("VISIBLE")){

        Log.d("design","visi "+i);
        viewHolder.description_1.setVisibility(View.VISIBLE);
        viewHolder.link_1.setVisibility(View.VISIBLE);

        viewHolder.de1.setVisibility(View.VISIBLE);
        //viewHolder.title_1.setBackgroundColor(Color.rgb(0x13,0x54,0x7a));
        //viewHolder.title_1.setTextColor(Color.WHITE);
        viewHolder.descriptionCard.animate().rotationX(360.0f);

    }else{

        viewHolder.description_1.setVisibility(View.GONE);
        viewHolder.link_1.setVisibility(View.GONE);

        viewHolder.de1.setVisibility(View.GONE);

       // viewHolder.title_1.setTextColor(Color.rgb(0x13,0x54,0x7a));
        //viewHolder.title_1.setBackgroundColor(Color.WHITE);
        viewHolder.descriptionCard.animate().rotationX(0.0f);

    }

        viewHolder.title_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rssItemsVisibleOrNot.get(i).equals("INVISIBLE")){
                    rssItemsVisibleOrNot.set(i,"VISIBLE");
                    Log.d("design","visi "+i);
                    notifyDataSetChanged();
                }
                else{
                    rssItemsVisibleOrNot.set(i,"INVISIBLE");
                    notifyDataSetChanged();
                }
            }
        });


        viewHolder.link_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity,WebVuePlayer.class);
                in.putExtra("link",rssItems.get(i).getLink());
                activity.startActivity(in);
            }
        });







    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title_1;
        private TextView description_1;
        private Button link_1;

        private TextView de1;
        private LinearLayout descriptionCard;



        public ViewHolder(View itemView) {
            super(itemView);
            title_1 = (TextView) itemView.findViewById(R.id.itemTitle);
            description_1 = (TextView) itemView.findViewById(R.id.itemDescription);
            link_1 = (Button) itemView.findViewById(R.id.itemLink);

            de1 = (TextView) itemView.findViewById(R.id.de1);
            descriptionCard = (LinearLayout) itemView.findViewById(R.id.descriptionCard);


        }


    }

}
