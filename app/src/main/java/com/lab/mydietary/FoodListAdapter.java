package com.lab.mydietary;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder> implements OnMapReadyCallback  {
    private String[] food_groups;
    private int[] food_images;
    private List<Food> list;
    private Context context;
    private FragmentManager fm;
    private boolean hasImage;
    private double lat;
    private double lang;
    private String address;
    private String[] meals;
    private boolean onDelete = false;
    private RecyclerView recycler;
    private FoodDao dao;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng eating_place = new LatLng(lat,lang);
        googleMap.addMarker(new MarkerOptions().position(eating_place)
                .title(address));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lang), 18.0f));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView text;
        public CardView card;
        public MyViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.food_list_image);
            text = view.findViewById(R.id.food_list_text);
            card = view.findViewById(R.id.card_view_list);
        }
    }

    public FoodListAdapter(String[] food_groups,int[] food_images, List<Food> list,Context context,FragmentManager fm,String[] meals,FoodDao dao,RecyclerView recycler)
    {
        this.recycler = recycler;
        this.dao = dao;
        this.meals = meals;
        this.food_groups = food_groups;
        this.fm = fm;
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
        Bitmap preview = !path.equals("NaN")? BitmapFactory.decodeFile(path) : BitmapFactory.decodeResource(context.getResources(),food_images[group]);
        preview = Bitmap.createScaledBitmap(preview,500,500,false);
        FragmentTransaction trans = fm.beginTransaction();
        lat = list.get(position).getLat();
        lang = list.get(position).getLang();
        address = list.get(position).getAddress();
        holder.image.setImageBitmap(preview);
        holder.text.setText(list.get(position).getName());
        holder.card.setOnClickListener(v->{
            final Dialog dialog = new Dialog(context);
            dialog.setTitle("Details");
            dialog.setContentView(R.layout.card_details);
            ImageButton left_btn = dialog.findViewById(R.id.left_btn);
            ImageButton right_btn = dialog.findViewById(R.id.right_btn);
            final SupportMapFragment mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.detail_map);
            mapFragment.getMapAsync(FoodListAdapter.this);
            left_btn.setOnClickListener(view -> {
                if(onDelete){
                    dao.delete(list.get(position));
                    File file = new File(list.get(position).getImage());
                    file.delete();
                    list.remove(position);
                    recycler.removeViewAt(position);
                    this.notifyItemRemoved(position);
                    this.notifyItemRangeChanged(position, list.size());
                    dialog.dismiss();
                }
                else
                {
                    onDelete = true;
                    left_btn.setImageResource(R.drawable.ic_confirm);
                    right_btn.setImageResource(R.drawable.ic_cancel);
                }
            });
            right_btn.setOnClickListener(view -> {
               if(onDelete){
                   onDelete = false;
                   left_btn.setImageResource(R.drawable.ic_delete);
                   right_btn.setImageResource(R.drawable.ic_edit);
               }
               else{
                   AppCompatActivity activity = (AppCompatActivity)context;
                   AddFragment myFragment = AddFragment.newInstance(list.get(position));
                   fm.beginTransaction().remove(mapFragment).commit();
                   dialog.dismiss();
                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

               }
            });

            ImageView top_image = dialog.findViewById(R.id.detail_image_top);
            ImageView group_image = dialog.findViewById(R.id.detail_food_groupImage);
            if(!path.equals("NaN")){
                top_image.setImageBitmap(BitmapFactory.decodeFile(path));
                group_image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),food_images[group]));
            }
            else{
                top_image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),food_images[group]));
                group_image.setImageResource(0);
            }

            ((TextView)dialog.findViewById(R.id.detail_food_name)).setText("NAME : "+list.get(position).getName());
            ((TextView)dialog.findViewById(R.id.detail_food_group)).setText("CATEGORY : "+food_groups[group]);
            ((TextView)dialog.findViewById(R.id.detail_date)).setText("DATE : "+list.get(position).getDate());
            ((TextView)dialog.findViewById(R.id.detail_time)).setText("TIME : "+list.get(position).getTime());
            ((TextView)dialog.findViewById(R.id.detail_location)).setText("LOCATION : "+list.get(position).getAddress());
            ((TextView)dialog.findViewById(R.id.detail_meal)).setText("MEAL : "+meals[list.get(position).getMeal()]);
            ((TextView)dialog.findViewById(R.id.detail_notes)).setText("NOTE : "+list.get(position).getNote());
            ((TextView)dialog.findViewById(R.id.detail_user)).setText("REPORTER : "+list.get(position).getUser());



            dialog.setOnCancelListener(dialog1 -> fm.beginTransaction().remove(mapFragment).commit());
            dialog.show();
        });
    }

    public void setFilter(List<Food> newList){
        list=new ArrayList<>();
        list.addAll(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return list.size();

    }
}
