package com.example.dataentryformipnx;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class List_Adapter extends SimpleAdapter {



    public List_Adapter(Activity context, List<? extends Map <String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View v = super.getView(position, convertView, parent);

        ImageView img = (ImageView) v.getTag();

        if (img == null){
            img = (ImageView) v.findViewById(R.id.imageView);
            v.setTag(img);
        }

        String url = ((Map)getItem(position)).get("sUserImage").toString();


        if(url.isEmpty()){
            Picasso.with(v.getContext()).load(R.drawable.ipnx).into(img);
            return v;
        }

        else {
            Picasso.with(v.getContext()).load(url).into(img);
            return v;

        }


    }
}
