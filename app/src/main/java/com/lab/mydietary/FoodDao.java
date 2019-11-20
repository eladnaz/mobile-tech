package com.lab.mydietary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    public long[] insert(Food...foods);

    @Update
    public void update(Food...foods);

    @Delete
    public void delete(Food food);

    @Query("Select * FROM Food")
    public List<Food> getFoods();

    @Query("Select * FROM Food WHERE id = :id")
    public Food getFoodWithId(int id);
}
