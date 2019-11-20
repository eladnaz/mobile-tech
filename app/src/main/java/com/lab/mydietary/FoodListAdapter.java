package com.lab.mydietary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder> {
    private String[] food_groups;
    private int[] food_images;
    private List<Food> list;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView text;
        public MyViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.food_list_image);
            text = view.findViewById(R.id.food_list_text);
        }
    }

    public FoodListAdapter(String[] food_groups,int[] food_images, List<Food> list,Context context)
    {
        this.context = context;
        this.food_images = food_images;
        this.food_groups = food_groups;
        this.list = list;
    }

    public FoodListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_food_added,parent,false);
        FoodListAdapter.MyViewHolder holder = new FoodListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FoodListAdapter.MyViewHolder holder, int position)
    {
        String path = list.get(position).getImage();
        int group = list.get(position).getGroup();
        Bitmap imageFromFile = path.equals("NaN") ? BitmapFactory.decodeResource(context.getResources(),food_images[group]) : BitmapFactory.decodeFile(path);
        holder.image.setImageBitmap(imageFromFile);
        holder.text.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount()
    {
        return food_groups.length;

    }
}
