package com.shaukan.gabriel.tinderclone.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shaukan.gabriel.tinderclone.Cards.Cards;
import com.shaukan.gabriel.tinderclone.R;

import org.w3c.dom.Text;

import java.util.List;

//Creating an arrayAdapter class
public class arrayAdapter extends ArrayAdapter<Cards> {

    Context context;

    //arrayAdapter constructor
    public arrayAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView age = (TextView) convertView.findViewById(R.id.age);

        name.setText(card_item.getName());
        age.setText(card_item.getAge());

        switch (card_item.getProfileImageUrl()) {
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;

    }
}
