package com.example.lulavillalobos.bakingapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lulavillalobos.bakingapp.Model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> loadAllRecipes();

    @Insert
    void insertRecipe(Recipe recipe);

    @Query("SELECT * FROM recipe WHERE id = :id")
    LiveData<Recipe> loadRecipeById(int id);
}
