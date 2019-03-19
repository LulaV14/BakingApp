package com.example.lulavillalobos.bakingapp.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lulavillalobos.bakingapp.Database.AppDatabase;
import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;
import com.example.lulavillalobos.bakingapp.ViewModel.RecipeViewModel;
import com.example.lulavillalobos.bakingapp.ViewModel.RecipeViewModelFactory;

public class RecipeActivity extends AppCompatActivity implements StepListFragment.OnStepListClickListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();
    private StepListFragment stepListFragment;
    private StepDescriptionFragment stepDescriptionFragment;
    private AppDatabase database;
    private Recipe recipe;
    private int step_index;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = AppDatabase.getInstance(this);

        mTwoPane = (Boolean) (findViewById(R.id.step_description_fragment_container) != null &&
            getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        if (intent.hasExtra("RECIPE_ID")) {
            int recipe_id = intent.getIntExtra("RECIPE_ID", -1);
            if (recipe_id == -1) {
                closeOnError();
                Log.e(TAG, "No Recipe object serialized");
            }

            if (savedInstanceState == null) {
                setupRecipe(recipe_id);

                //check if it's single or two pane and act accordingly
                mTwoPane = findViewById(R.id.step_description_fragment_container) != null;
            } else {
                recipe = savedInstanceState.getParcelable("RECIPE_OBJECT");
                stepListFragment = (StepListFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, "stepListFragment");
                stepDescriptionFragment = (StepDescriptionFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, "stepDescriptionFragment");
            }
        } else {
            closeOnError();
            Log.e(TAG, "No intent extra data found");
        }
    }

    @Override
    public void onStepItemSelected(int position) {
        step_index = position;
        if (mTwoPane) {
            stepDescriptionFragment = new StepDescriptionFragment();
            stepDescriptionFragment.setIngredients(recipe.getIngredients());
            stepDescriptionFragment.setStepList(recipe.getSteps());
            stepDescriptionFragment.setIndex(step_index);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(
                    R.id.step_description_fragment_container,
                    stepDescriptionFragment,
                    "stepDescriptionFragment"
            ).commit();
        } else {
            stepDescriptionFragment = new StepDescriptionFragment();
            stepDescriptionFragment.setIngredients(recipe.getIngredients());
            stepDescriptionFragment.setStepList(recipe.getSteps());
            stepDescriptionFragment.setIndex(step_index);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(
                    R.id.step_list_fragment_container,
                    stepDescriptionFragment,
                    "stepDescriptionFragment"
            );
            ft.addToBackStack(null);
            ft.commit();
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

    public void setupRecipe(int recipe_id) {
        RecipeViewModelFactory factory = new RecipeViewModelFactory(database, recipe_id);
        RecipeViewModel viewModel =
                ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                RecipeActivity.this.recipe = recipe;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (mTwoPane) {
                    // set step description fragment
                    stepDescriptionFragment = new StepDescriptionFragment();
                    stepDescriptionFragment.setStepList(recipe.getSteps());
                    stepDescriptionFragment.setIngredients(recipe.getIngredients());
                    stepDescriptionFragment.setIndex(step_index);
                    ft.replace(
                            R.id.step_description_fragment_container,
                            stepDescriptionFragment,
                            "stepDescriptionFragment"
                    );
                    ft.addToBackStack(null);
//                    ft.commit();
                }

                // set step list fragment
                stepListFragment = new StepListFragment();
                stepListFragment.setSteps(recipe.getSteps());
                ft.replace(
                        R.id.step_list_fragment_container,
                        stepListFragment,
                        "stepListFragment"
                ).commit();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save fragment's instance
        super.onSaveInstanceState(outState);
        outState.putParcelable("RECIPE_OBJECT", recipe);
        if (stepListFragment != null && stepListFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "stepListFragment", stepListFragment);
        }

        if (stepDescriptionFragment != null && stepDescriptionFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "stepDescriptionFragment", stepDescriptionFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (mTwoPane) {
            super.onBackPressed();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStackImmediate();
                ft.commit();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();

                } else {
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
