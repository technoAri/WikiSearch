package com.ws.andriod.wikisearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rapidd08 on 16-03-2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<DataModel> results;
    private Context context;

    public RecyclerAdapter(Context context, ArrayList<DataModel> items, RecyclerView recyclerView) {
        this.results = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.title.setText(results.get(i).getTitle());
        viewHolder.subTitle.setText((results.get(i).getSubTitle()));
        //viewHolder.img.setImageDrawable(LoadImageFromWebOperations(results.get(i).getImaageURL()));
        //viewHolder.img.setImageDrawable(LoadImageFromWebOperations(results.get(i).getImaageURL()));
        Picasso.with(context.getApplicationContext())
                .load(results.get(i).getImaageURL())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher)
                .resize(50, 50)
                .centerInside()
                .into(viewHolder.img);
    }

//    public static Drawable LoadImageFromWebOperations(String url) {
//        try {
//            InputStream is = (InputStream) new URL(url).getContent();
//            Drawable d = Drawable.createFromStream(is, "src name");
//            return d;
//        } catch (Exception e) {
//            return null;
//
//        }
//

    @Override
    public int getItemCount() {
        return results.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subTitle;
        private ImageView img;

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.tv_title);
            subTitle = (TextView) view.findViewById(R.id.tv_subTitle);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
