package com.example.user.simpleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/6/13.
 */
public class DrinkAdapter extends BaseAdapter{
    LayoutInflater inflater;//modify
    List<Drink> drinks;

    public DrinkAdapter(Context context, ArrayList<Drink> drinks){
        this.inflater = LayoutInflater.from(context);
        this.drinks = drinks;
    }
    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        return drinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.listview_drink_item, null);
            holder = new Holder();
            holder.drinkName = (TextView)convertView.findViewById(R.id.noteTextView);
            holder.mPriceTextView = (TextView)convertView.findViewById(R.id.mPriceTextView);
            holder.lPriceTextView = (TextView)convertView.findViewById(R.id.lPriceTextView);
            holder.drinkImageView = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }
        Drink drink = drinks.get(position);

        //holder.drinkNameTextView.setText(drink.name);
        //holder.drinkName.setText(drink.name); // 6/23
        holder.drinkName.setText(drink.getName());
        //holder.mPriceTextView.setText(String.valueOf(drink.mPrice));
        holder.mPriceTextView.setText(String.valueOf(drink.getmPrice()));
        //holder.lPriceTextView.setText(String.valueOf(drink.lPrice));
        holder.lPriceTextView.setText(String.valueOf(drink.getlPrice()));
        holder.drinkImageView.setImageResource(drink.imageId);

        Picasso.with(inflater.getContext()).load(drink.getImage().getUrl()).into(holder.drinkImageView); // 6/23

        return convertView;
    }
    class Holder{
        TextView drinkName;
        TextView mPriceTextView;
        TextView lPriceTextView;
        ImageView drinkImageView;
    }
}
