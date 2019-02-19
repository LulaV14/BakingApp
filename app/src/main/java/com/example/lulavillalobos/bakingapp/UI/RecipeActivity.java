package com.example.lulavillalobos.bakingapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        if (intent.hasExtra("RECIPE_ID")) {
            int recipe_id = intent.getIntExtra("RECIPE_ID", -1);
            if (recipe_id == -1) {
                closeOnError();
                Log.e(TAG, "No Recipe object serialized");
            }

            //send as bundle
            Bundle bundle = new Bundle();
            bundle.putInt("RECIPE_ID", recipe_id);
            StepListFragment stepListFragment = new StepListFragment();
            stepListFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.step_list_fragment_container, stepListFragment).commit();
        } else {
            closeOnError();
            Log.e(TAG, "No intent extra data found");
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(
                this,
                "Error while trying to view recipe details.\nPlease try again.",
                Toast.LENGTH_LONG
        ).show();
    }
}
