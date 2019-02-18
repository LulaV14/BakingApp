package com.example.lulavillalobos.bakingapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        if (intent.hasExtra("RECIPE_OBJECT")) {
            recipe = intent.getParcelableExtra("RECIPE_OBJECT");
            if (recipe == null) {
                // TODO: call "closeOnError" method
                Log.e(TAG, "No Recipe object serialized");
            }

            //TODO: send recipe object to fragment
        } else {
            // TODO: closeOnError()
            Log.e(TAG, "No intent extra data found");
        }
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
