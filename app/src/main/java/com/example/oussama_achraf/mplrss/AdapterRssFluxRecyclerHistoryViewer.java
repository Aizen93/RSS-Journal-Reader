package com.example.oussama_achraf.mplrss;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

import com.example.oussama_achraf.mplrss.XMLParsingUtils.RssElement;
import com.example.oussama_achraf.mplrss.database.AccesData;

import java.util.List;

public class AdapterRssFluxRecyclerHistoryViewer extends RecyclerView.Adapter<AdapterRssFluxRecyclerHistoryViewer.ViewHolder> {

    private Activity activity = null;
    private List<RssElement> rssList = null;
    private AccesData accesData = null;
    private android.app.Fragment fragment = null;

    Cursor dataCursor;
    Context context;

    private int itemLayout;

    public AdapterRssFluxRecyclerHistoryViewer(
            Activity activity, List<RssElement> rssItems, int itemLayout, Cursor cursor){
        this.rssList = rssItems;
        this.itemLayout = itemLayout;
        this.activity = activity;
        this.fragment = activity.getFragmentManager().findFragmentById(R.id.container123);
        accesData = new AccesData(activity);
        dataCursor = cursor;
    }

    @NonNull
    @Override
    public AdapterRssFluxRecyclerHistoryViewer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_view_rss_list, parent, false);
        return new ViewHolder(v);
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRssFluxRecyclerHistoryViewer.ViewHolder viewHolder, int i) {
        //viewHolder.item.setText(rssList.get(i));
        viewHolder.title_1.setText(rssList.get(i).getTitre());
        viewHolder.description_1.setText(rssList.get(i).getDescription());
        viewHolder.link_1.setText(rssList.get(i).getLink());
        viewHolder.date_1.setText(rssList.get(i).getDate());



        final int index_to_delete = rssList.get(i).getId();
        final int index = i;
        viewHolder.delete_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("delete","index of item :"+index_to_delete);
                // delete the rss element from the database;
                deleteAreYouSure(index_to_delete,index);
            }
        });


        viewHolder.show_rss_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("show","the id is "+ rssList.get(index).getId());
                // get the rss title and description
                String title_2 = rssList.get(index).getTitre();
                String description_2 = rssList.get(index).getDescription();

                Log.d("show","title is "+title_2);
                Log.d("show","description_2 is "+description_2);




                Intent intentRssPlayer = new Intent(activity,RssPlayer.class);

                intentRssPlayer.putExtra("mode","offline");
                intentRssPlayer.putExtra("titleOffline",title_2);
                intentRssPlayer.putExtra("descriptionOffline",description_2);
                intentRssPlayer.putExtra("idOffline",rssList.get(index).getId());

                activity.startActivity(intentRssPlayer);

            }
        });
    }

    @Override
    public int getItemCount() {
        return rssList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView title_1 = null;
        private TextView description_1 = null;
        private TextView httpAddres_1 = null;
        private TextView date_1 = null;

        private LinearLayout linearlayoutCard = null;
        private TextView link_1 = null;

        private ImageView delete_1 = null;
        private Button show_rss_1 = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            title_1 = (TextView) itemView.findViewById(R.id.itemTitle);
            description_1 = (TextView) itemView.findViewById(R.id.itemDescription);
            linearlayoutCard = (LinearLayout) itemView.findViewById(R.id.itemsView);
            link_1 = (TextView) itemView.findViewById(R.id.itemLinkHttp);
            delete_1 = (ImageView) itemView.findViewById(R.id.imageDelete);
            show_rss_1 = (Button) itemView.findViewById(R.id.itemLink);
            date_1 = (TextView) itemView.findViewById(R.id.itemDate);

        }
    }

    public void deleteAreYouSure(final int index_to_delete, final int index){
        final boolean[] ok = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Do you really want to delete this Rss ?");
        builder.setMessage("Are you sure ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                ok[0] = true;
                dialog.dismiss();
                Log.d("sure",":"+ok[0]);
                    accesData.deleteRssData(index_to_delete);
                    accesData.deleteRssLink(index_to_delete);
                    rssList.remove(index);
                    notifyItemRemoved(index);
                    notifyDataSetChanged();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ok[0] = false;
                // Do nothing
                dialog.dismiss();
                Log.d("sure",":"+ok[0]);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
