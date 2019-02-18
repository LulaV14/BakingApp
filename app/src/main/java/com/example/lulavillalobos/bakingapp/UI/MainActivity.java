package com.example.lulavillalobos.bakingapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.lulavillalobos.bakingapp.API.APIController;
import com.example.lulavillalobos.bakingapp.API.APIService;
import com.example.lulavillalobos.bakingapp.Adapters.RecipeAdapter;
import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeOnClickHandler {

    private final static String TAG = MainActivity.class.getSimpleName();

    private RecipeAdapter recipeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Recipe> recipes;

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

        showRecipes();
    }

    public void showRecipes() {
        APIService apiService = APIController.getClient().create(APIService.class);
        Call<ArrayList<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    try {
                        recipes = response.body();
                        recipeAdapter = new RecipeAdapter(recipes, MainActivity.this::onClick);
                        recyclerView.setAdapter(recipeAdapter);
                    } catch(NullPointerException e) {
                        Log.e(TAG, e.getMessage());
                    }
                } else {
                    Log.e(TAG, response.errorBody().toString());
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
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
        recipeIntent.putExtra("RECIPE_OBJECT", clickedRecipe);
        startActivity(recipeIntent);

    }
}
