package com.lab.mydietary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {
    private EditText food_group_edit,date_edit,time_edit,meal_edit;
    private RecyclerView food_group_list;
    private FoodGroupAdapter adapter;
    private CardView card_view_main;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        food_group_edit = findViewById(R.id.food_group_edit);
        date_edit = findViewById(R.id.date_edit);
        time_edit = findViewById(R.id.time_edit);
        meal_edit = findViewById(R.id.meal_edit);
        card_view_main = findViewById(R.id.card_view_main);
        food_group_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_group_dialog();
            }
        });
        card_view_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_group_dialog();
            }
        });
        date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_date_dialog();
            }
        });
        time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_time_dialog();
            }
        });
        meal_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_meal_dialog();
            }
        });

    }

    private void show_group_dialog()
    {
        food_group_edit = findViewById(R.id.food_group_edit);
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Food Group");
        dialog.setContentView(R.layout.dialog_food_group);
        food_group_list =(RecyclerView)dialog.findViewById(R.id.food_group_list);
        layoutManager = new LinearLayoutManager(this);
        food_group_list.setLayoutManager(layoutManager);
        food_group_list.setHasFixedSize(true);
        String[] food_groups = {"Wholemeal and Grains","Nature's Vegetables","Muscle Building Proteins","Colorful Fruits","Delicious Fats"};
        int[] images={R.drawable.grainsgroup,R.drawable.vegegroup,R.drawable.meatgroup,R.drawable.fruitgroup,R.drawable.fatsgroup};
        adapter = new FoodGroupAdapter(food_groups,images,food_group_edit,dialog,card_view_main);
        food_group_list.setAdapter(adapter);
        dialog.show();
    }

    private void show_date_dialog()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Date Picker");
        dialog.setContentView(R.layout.dialog_date);

        DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                date_edit.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void show_time_dialog()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Time Picker");
        dialog.setContentView(R.layout.dialog_time);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker);
        Button set = dialog.findViewById((R.id.set_button));
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourFull = timePicker.getHour();
                int minute = timePicker.getMinute();
                String am_pm = hourFull > 12 ? "PM" : "AM";
                time_edit.setText((hourFull > 12 ? hourFull-12 : hourFull)+":"+String.format("%02d",minute)+" "+ am_pm);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void show_meal_dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
        final ArrayAdapter<String> mealAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView item = (TextView) view.findViewById(android.R.id.text1);
                item.setTextColor(Color.WHITE);
                return view;
            }
        };
        mealAdapter.add("Snack");
        mealAdapter.add("Breakfast");
        mealAdapter.add("Lunch");
        mealAdapter.add("Dinner");
        mealAdapter.add("Supper");
        mealAdapter.add("Tea Time");
        mealAdapter.add("Brunch");

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(mealAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String meal = mealAdapter.getItem(which);
                meal_edit.setText(meal);
                
            }
        });
        builder.show();
    }

}
