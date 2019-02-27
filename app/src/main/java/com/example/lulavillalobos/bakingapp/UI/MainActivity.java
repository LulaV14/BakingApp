package com.example.lulavillalobos.bakingapp.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.lulavillalobos.bakingapp.API.APIController;
import com.example.lulavillalobos.bakingapp.API.APIService;
import com.example.lulavillalobos.bakingapp.Adapters.RecipeAdapter;
import com.example.lulavillalobos.bakingapp.Database.AppDatabase;
import com.example.lulavillalobos.bakingapp.Helpers.AppExecutors;
import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;
import com.example.lulavillalobos.bakingapp.ViewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeOnClickHandler {

    private final static String TAG = MainActivity.class.getSimpleName();

    private RecipeAdapter recipeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Recipe> recipes;
    private AppDatabase database;

    @BindView(R.id.rv_recipes)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(null);

        database = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey("recipe_list")) {
            recipes = savedInstanceState.getParcelableArrayList("recipe_list");
            recipeAdapter = new RecipeAdapter(recipes, MainActivity.this::onClick);
            recyclerView.setAdapter(recipeAdapter);
        } else {
            showRecipes();
        }
    }

    public void showRecipes() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                MainActivity.this.recipes = recipes;
                Log.d(TAG, "Updating list of recipes from LiveData in ViewModel");
                if (recipes == null || recipes.size() <= 0) {
                 getRecipes();
                } else {
                    recipeAdapter = new RecipeAdapter(recipes, MainActivity.this::onClick);
                    recyclerView.setAdapter(recipeAdapter);
                }
            }
        });
    }

    public void getRecipes() {
        APIService apiService = APIController.getClient().create(APIService.class);
        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    try {
                        recipes = response.body();
                        //Save each recipe retrieved in the db
                        for (Recipe recipe : recipes) {
                            AppExecutors
                                    .getInstance()
                                    .diskIO()
                                    .execute(insertRecipeRunnable(recipe));
                        }
                    } catch(NullPointerException e) {
                        Log.e(TAG, e.getMessage());
                    }
                } else {
                    Log.e(TAG, response.errorBody().toString());
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(
                        MainActivity.this,
                        "There was an error while trying to get recipes.",
                        Toast.LENGTH_LONG
                ).show();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onClick(int recipe_position) {
        Recipe clickedRecipe = recipes.get(recipe_position);
        Intent recipeIntent = new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra("RECIPE_ID", clickedRecipe.getId());
        startActivity(recipeIntent);

    }

    private Runnable insertRecipeRunnable(Recipe recipe) {
        return new Runnable() {
            @Override
            public void run() {
                database.recipeDao().insertRecipe(recipe);
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("recipe_list", (ArrayList<Recipe>)recipes);
        super.onSaveInstanceState(outState);
    }
}
