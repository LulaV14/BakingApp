package com.example.lulavillalobos.bakingapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.lulavillalobos.bakingapp.Database.AppDatabase;
import com.example.lulavillalobos.bakingapp.Model.Recipe;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Recipe>> recipes;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving recipes from the database");
        recipes = database.recipeDao().loadAllRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() { return recipes; }
}
