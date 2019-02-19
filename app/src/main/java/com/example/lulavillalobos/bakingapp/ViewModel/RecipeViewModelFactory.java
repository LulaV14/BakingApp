package com.example.lulavillalobos.bakingapp.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.lulavillalobos.bakingapp.Database.AppDatabase;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase database;
    private final int recipeId;

    public RecipeViewModelFactory(AppDatabase database, int recipeId) {
        this.database = database;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeViewModel(database, recipeId);
    }
}
