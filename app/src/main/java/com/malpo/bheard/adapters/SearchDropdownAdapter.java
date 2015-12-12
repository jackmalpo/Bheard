package com.malpo.bheard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malpo.bheard.R;
import com.malpo.bheard.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jack on 12/12/15.
 */
public class SearchDropdownAdapter extends ArrayAdapter<Artist> {

    private Context context;
    private int resource;
    private List<Artist> items;

    public SearchDropdownAdapter(Context context, int resource, List<Artist> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    public void updateData(List<Artist> artists){
        items = artists;
        notifyDataSetChanged();
    }

    @Override
    public Artist getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Artist thisArtist = items.get(position);
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.textView.setText(thisArtist.getName());
            Picasso.with(context).load(thisArtist.getImage().get(3).getText()).into(holder.imageView);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        return convertView;
    }


    static class ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(View view){
            textView = (TextView) view.findViewById(R.id.dropdown_text);
            imageView = (ImageView) view.findViewById(R.id.dropdown_image);
        }

    }
}
