package com.example.lulavillalobos.bakingapp.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.lulavillalobos.bakingapp.Database.AppDatabase;
import com.example.lulavillalobos.bakingapp.Model.Recipe;

public class RecipeViewModel extends ViewModel {

    private LiveData<Recipe> recipe;

    public RecipeViewModel(AppDatabase database, int recipeId) {
        recipe = database.recipeDao().loadRecipeById(recipeId);
    }

    public LiveData<Recipe> getRecipe() { return recipe; }
}
