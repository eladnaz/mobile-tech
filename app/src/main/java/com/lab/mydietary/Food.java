package com.lab.mydietary;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName="Food")
public class Food implements Parcelable {
    @Ignore
    static String[] food_groups = {"Wholemeal and Grains","Nature's Vegetables","Muscle Building Proteins","Colorful Fruits","Delicious Fats"};
    @Ignore
    static int[] images_groups={R.drawable.grainsgroup,R.drawable.vegegroup,R.drawable.meatgroup,R.drawable.fruitgroup,R.drawable.fatsgroup};
    @Ignore
    static String[] meals = {"Snack","Breakfast","Lunch","Dinner","Supper","Tea Time","Brunch"};

    public Food(int id,String name, int group,String date, String time, int meal, String note, String user, double lat, double lang,String image,String address) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.date = date;
        this.time = time;
        this.meal = meal;
        this.note = note;
        this.user = user;
        this.lat = lat;
        this.lang = lang;
        this.image = image;
        this.address = address;
    }

    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        group = in.readInt();
        date = in.readString();
        time = in.readString();
        meal = in.readInt();
        note = in.readString();
        user = in.readString();
        lat = in.readDouble();
        lang = in.readDouble();
        image = in.readString();
        address = in.readString();
    }

    @Ignore
    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public void setLatLang(double lat,double lang){
        this.lat = lat;
        this.lang = lang;
    }
    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    @PrimaryKey(autoGenerate=true)
    @NonNull
    private int id;
    private String name;
    private int group;
    private String date;
    private String time;
    private int meal;
    private String note;
    private String user;
    private double lat;
    private double lang;




    private String image;
    private String address;

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }
    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(group);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeInt(meal);
        dest.writeString(note);
        dest.writeString(user);
        dest.writeDouble(lat);
        dest.writeDouble(lang);
        dest.writeString(image);
        dest.writeString(address);
    }
}
