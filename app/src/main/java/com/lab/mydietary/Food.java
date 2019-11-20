package com.lab.mydietary;

import java.util.ArrayList;


public class Food {
    static String[] food_groups = {"Wholemeal and Grains","Nature's Vegetables","Muscle Building Proteins","Colorful Fruits","Delicious Fats"};
    static int[] images_groups={R.drawable.grainsgroup,R.drawable.vegegroup,R.drawable.meatgroup,R.drawable.fruitgroup,R.drawable.fatsgroup};
    static public ArrayList<Food> foodArray = new ArrayList<Food>();

    public Food(String name, int group,String date, String time, int meal, String note, String user, double lat, double lang) {
        this.name = name;
        this.group = group;
        this.date = date;
        this.time = time;
        this.meal = meal;
        this.note = note;
        this.user = user;
        this.lat = lat;
        this.lang = lang;
    }

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

    private String name;
    private int group;
    private String date;
    private String time;
    private int meal;
    private String note;
    private String user;
    private double lat;
    private double lang;


}
