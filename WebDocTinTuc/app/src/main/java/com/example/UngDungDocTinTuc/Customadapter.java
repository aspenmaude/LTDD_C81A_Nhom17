package com.example.UngDungDocTinTuc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Customadapter extends ArrayAdapter<Read> {

    public Customadapter(Context context, int resource, List<Read> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.line_layout_listview, null);
        }
        Read p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txttitle = (TextView) view.findViewById(R.id.textviewtitle);
            txttitle.setText(p.title);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Picasso.with(getContext()).load(p.images).into(imageView);


        }
        return view;
    }

}
