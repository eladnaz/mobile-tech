package com.lab.mydietary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;




import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText food_group_edit,date_edit,time_edit,meal_edit,location_edit,food_name_edit,user_name_edit,note_edit;
    private FloatingActionButton add_btn;
    private RecyclerView food_group_list;
    private FoodGroupAdapter adapter;
    private CardView card_view_main;
    private RecyclerView.LayoutManager layoutManager;
    private double lat = 0;
    private double lang = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        food_group_edit = findViewById(R.id.food_group_edit);
        date_edit = findViewById(R.id.date_edit);
        time_edit = findViewById(R.id.time_edit);
        note_edit = findViewById(R.id.note_edit);
        meal_edit = findViewById(R.id.meal_edit);
        user_name_edit = findViewById(R.id.user_name_edit);
        card_view_main = findViewById(R.id.card_view_main);
        add_btn = findViewById(R.id.add_btn);
        food_name_edit = findViewById(R.id.food_name_edit);
        location_edit = findViewById(R.id.location_edit);
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
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
        location_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_map_dialog();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArray();
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

        adapter = new FoodGroupAdapter(Food.food_groups,Food.images_groups,food_group_edit,dialog,card_view_main);
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
                hourFull = hourFull == 0 ? 12 : hourFull;
                int minute = timePicker.getMinute();
                String am_pm = hourFull >= 12 ? "PM" : "AM";
                time_edit.setText(String.format("%02d",(hourFull > 12 ? hourFull-12 : hourFull))+":"+String.format("%02d",minute)+" "+ am_pm);
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
                item.setShadowLayer(1,1,1,Color.BLACK);
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

    private void show_map_dialog()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Maps");
        dialog.setContentView(R.layout.fragment_map);
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                location_edit.setText(place.getName()+","+place.getAddress());
                lat = place.getLatLng().latitude;
                lang = place.getLatLng().longitude;
                MainActivity.this.getSupportFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
                dialog.dismiss();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d("RCV", "An error occurred: " + status);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                MainActivity.this.getSupportFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
            }
        });
        dialog.show();
    }

    private void addArray()
    {
        if(checkFields())
            return;
        else {
            String food_name = editString(food_name_edit);
            int food_group = Arrays.asList(Food.food_groups).indexOf(editString(food_group_edit));
            String date = editString(date_edit);
            String time = editString(time_edit);
            int meal = Arrays.asList(Food.food_groups).indexOf(editString(food_group_edit)); //returns -1 if empty.
            String note = editString(note_edit).replace(" ","").length() > 0 ? editString(note_edit) : "Not Available";
            String user = editString(user_name_edit);
            Food food = new Food(food_name, food_group,date,time,meal,note,user,lat,lang);
            Food.foodArray.add(food);
        }

    }

    private boolean checkFields()
    {
        boolean hasEmptyField = false;
        Map<String,EditText> field_map = new HashMap<String,EditText>();
        field_map.put("Food Name",food_name_edit);
        field_map.put("Food Group",food_group_edit);
        field_map.put("Date",date_edit);
        field_map.put("Time",time_edit);
        field_map.put("Name of Reporter",user_name_edit);
        for(Map.Entry m:field_map.entrySet())
        {
            if(!hasEmptyField)
                hasEmptyField = isEmptyOrSpace((String)m.getKey(),(EditText)m.getValue());
            else
                isEmptyOrSpace((String)m.getKey(),(EditText)m.getValue());
        }
        return hasEmptyField;
    }

    private boolean isEmptyOrSpace(String fieldName,EditText field)
    {
        String input = editString(field).replace(" ","");
        if(input.isEmpty())
        {
            field.setError(fieldName + " is required");
            return true;
        }
        else
            return false;
    }

    private String editString(EditText field)
    {
        return field.getText().toString();
    }

    

}
