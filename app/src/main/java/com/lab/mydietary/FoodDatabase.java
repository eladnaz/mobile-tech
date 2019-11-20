package com.lab.mydietary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version = 1)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDao getFoodDao();
}
