package com.lab.mydietary;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FoodGroupAdapter extends RecyclerView.Adapter<FoodGroupAdapter.MyViewHolder> {
    private String[] food_groups;
    private int[] images;
    private EditText food_group_edit;
    private Dialog dialog;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView text;
        public CardView card;
        public MyViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.food_group_image);
            text = view.findViewById(R.id.food_group_text);
            card = view.findViewById((R.id.card_view));
        }
    }

    public FoodGroupAdapter(String[] food_groups, int[] images, EditText text,Dialog dialog)
    {
        this.dialog = dialog;
        this.food_groups = food_groups;
        this.images = images;
        food_group_edit = text;
    }

    public FoodGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position)
    {
        final int index = holder.getAdapterPosition();
        holder.image.setImageResource(images[position]);
        holder.text.setText(food_groups[position]);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_group_edit.setText(food_groups[index]);
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return food_groups.length;

    }
}
