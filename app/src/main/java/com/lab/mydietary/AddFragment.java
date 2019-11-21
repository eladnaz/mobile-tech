package com.lab.mydietary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;



import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements OnMapReadyCallback {
    private final int CAMERA_REQUEST_CODE = 1367;
    private EditText food_group_edit,date_edit,time_edit,meal_edit,location_edit,food_name_edit,user_name_edit,note_edit;
    private FloatingActionButton add_btn;
    private ImageButton camera_btn;
    private RecyclerView food_group_list;
    private FoodGroupAdapter adapter;
    private CardView card_view_main;
    private RecyclerView.LayoutManager layoutManager;
    private double lat = 0;
    private double lang = 0;
    private FoodDatabase db;
    private SupportMapFragment mapFragment;
    private OnFragmentInteractionListener mListener;
    private ImageView camera_image,food_group_image_main;
    private String currentPhotoPath;
    private AutocompleteSupportFragment autocompleteFragment;
    private boolean hasImage = false;
    private Bundle data=null;
    private boolean isUpdate=false;
    private String oldImage;
    private int id = 0;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance(Food food) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        if(food != null)
            args.putParcelable("edit",food);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(this.getArguments() != null)
            data=this.getArguments();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        food_group_edit = getView().findViewById(R.id.food_group_edit);
        date_edit = getView().findViewById(R.id.date_edit);
        time_edit = getView().findViewById(R.id.time_edit);
        note_edit = getView().findViewById(R.id.note_edit);
        meal_edit = getView().findViewById(R.id.meal_edit);
        food_group_image_main = getView().findViewById((R.id.food_group_image_main));
        camera_btn = getView().findViewById(R.id.camera_btn);
        user_name_edit = getView().findViewById(R.id.user_name_edit);
        card_view_main = getView().findViewById(R.id.card_view_main);
        add_btn = getView().findViewById(R.id.add_btn);
        camera_image = getView().findViewById(R.id.camera_image);
        food_name_edit = getView().findViewById(R.id.food_name_edit);
        location_edit = getView().findViewById(R.id.location_edit);
        Places.initialize(getActivity(), getString(R.string.google_maps_key));
        FragmentManager addMng = getChildFragmentManager();
        mapFragment = (SupportMapFragment) addMng.findFragmentById(R.id.map);
        if(data != null)
        {
            isUpdate = true;
            Food food = data.getParcelable("edit");
            id = food.getId();
            food_name_edit.setText(food.getName());
            food_group_image_main.setImageResource(Food.images_groups[food.getGroup()]);
            food_group_edit.setText(Food.food_groups[food.getGroup()]);
            time_edit.setText(food.getTime());
            date_edit.setText(food.getDate());
            oldImage = food.getImage();
            meal_edit.setText(Food.meals[food.getMeal()]);
            note_edit.setText(food.getNote());
            user_name_edit.setText(food.getUser());
            location_edit.setText(food.getAddress());
            lat = food.getLat();
            lang = food.getLang();
            mapFragment.getMapAsync(this);
            camera_image.setImageBitmap(BitmapFactory.decodeFile(food.getImage()));
        }

        food_group_edit.setOnClickListener(v -> show_group_dialog());
        card_view_main.setOnClickListener(v -> show_group_dialog());
        date_edit.setOnClickListener(v -> show_date_dialog());
        time_edit.setOnClickListener(v -> show_time_dialog());
        meal_edit.setOnClickListener(v -> show_meal_dialog());
        location_edit.setOnClickListener(v -> show_map_dialog());
        add_btn.setOnClickListener(v -> addArray());
        camera_btn.setOnClickListener(v -> open_camera());
        db = Room.databaseBuilder(getActivity(),FoodDatabase.class,"Food").allowMainThreadQueries().build();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void show_date_dialog()
    {
        final Dialog dialog = new Dialog(getActivity());
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

    private void open_camera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("ERROR",ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        getActivity();
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap currentPhoto = BitmapFactory.decodeFile(currentPhotoPath);
            currentPhoto = rotate(90,currentPhoto);
            camera_image.setImageBitmap(currentPhoto);
            hasImage = true;
        }
    }

    public Bitmap rotate(float degree,Bitmap raw)
    {
        Matrix matrix = new Matrix();
        int width = raw.getWidth();
        int height = raw.getHeight();
        int newWidth = 1008;

        int newHeight  = 756;


        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(degree);
        Bitmap resizedBitmap = Bitmap.createBitmap(raw,0,0,width,height,matrix,true);
        return resizedBitmap;
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "temp";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] left_over = storageDir.listFiles();
        for(File file:left_over)
        {
            file.delete();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }




    private void show_time_dialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Time Picker");
        dialog.setContentView(R.layout.dialog_time);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker);
        Button set = dialog.findViewById((R.id.set_button));
        set.setOnClickListener(v -> {
            int hourFull = timePicker.getHour();
            hourFull = hourFull == 0 ? 12 : hourFull;
            int minute = timePicker.getMinute();
            String am_pm = hourFull >= 12 ? "PM" : "AM";
            time_edit.setText(String.format("%02d",(hourFull > 12 ? hourFull-12 : hourFull))+":"+String.format("%02d",minute)+" "+ am_pm);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void show_meal_dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        final ArrayAdapter<String> mealAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView item = (TextView) view.findViewById(android.R.id.text1);
                item.setShadowLayer(1,1,1, Color.BLACK);
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


        builder.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

        builder.setAdapter(mealAdapter, (dialog, which) -> {
            String meal = mealAdapter.getItem(which);
            meal_edit.setText(meal);

        });
        builder.show();
    }

    private void show_map_dialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Maps");
        dialog.setContentView(R.layout.fragment_map);
        autocompleteFragment = (AutocompleteSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                location_edit.setText(place.getName()+","+place.getAddress());
                lat = place.getLatLng().latitude;
                lang = place.getLatLng().longitude;
                mapFragment.getMapAsync(AddFragment.this);
                getActivity().getSupportFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
                dialog.dismiss();
            }

            @Override
            public void onError(Status status) {
                Log.d("ERROR", "An error occurred: " + status);
            }
        });
        dialog.setOnCancelListener(dialog1 -> getActivity().getSupportFragmentManager().beginTransaction().remove(autocompleteFragment).commit());
        dialog.show();
    }

    private void show_group_dialog()
    {
        food_group_edit = getView().findViewById(R.id.food_group_edit);
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Food Group");
        dialog.setContentView(R.layout.dialog_food_group);
        food_group_list =(RecyclerView)dialog.findViewById(R.id.food_group_list);
        layoutManager = new LinearLayoutManager(getActivity());
        food_group_list.setLayoutManager(layoutManager);
        food_group_list.setHasFixedSize(true);

        adapter = new FoodGroupAdapter(Food.food_groups,Food.images_groups,food_group_edit,dialog,card_view_main);
        food_group_list.setAdapter(adapter);
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
            String note = editString(note_edit).replace(" ","").length() > 0 ? editString(note_edit) : "NaN";
            String user = editString(user_name_edit);
            String address = editString(location_edit).replace(" ","").length() > 0 ? editString(location_edit) : "NaN";
            String imageUrl = "NaN";
            try {
                if(hasImage)
                    imageUrl = saveImageForDetails(((BitmapDrawable)camera_image.getDrawable()).getBitmap());
                else
                    imageUrl = "NaN";
            }
            catch(IOException ex)
            {
                Log.d("ERROR",ex.getMessage());
            }

            FoodDao dao = db.getFoodDao();
            if(isUpdate)
            {
                Food food = new Food(id,food_name, food_group,date,time,meal,note,user,lat,lang,imageUrl,address);
                File file = new File(oldImage);
                file.delete();
                dao.update(food);
                ListFragment myFragment = ListFragment.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                return;
            }
            else
            {
                Food food = new Food(id,food_name, food_group,date,time,meal,note,user,lat,lang,imageUrl,address);
                dao.insert(food);
                clearFields();
                return;
            }


        }

    }

    private String saveImageForDetails(Bitmap raw) throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + ".png";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = new File(storageDir,imageFileName);
        OutputStream out = new FileOutputStream(image);
        raw.compress(Bitmap.CompressFormat.PNG,100,out);
        out.flush();out.close();
        return image.getAbsolutePath();
    }

    private void clearFields()
    {
        food_name_edit.setText("");food_group_edit.setText("");date_edit.setText("");time_edit.setText("");meal_edit.setText("");note_edit.setText("");user_name_edit.setText("");location_edit.setText("");
        hasImage = false;
        camera_image.setImageResource(0);
        food_group_image_main.setImageResource(0);
        lat = 0;
        lang = 0;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng eating_place = new LatLng(lat,lang);
        googleMap.addMarker(new MarkerOptions().position(eating_place)
                .title(location_edit.getText().toString()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lang), 18.0f));

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapFragment = null;
        db.close();
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
