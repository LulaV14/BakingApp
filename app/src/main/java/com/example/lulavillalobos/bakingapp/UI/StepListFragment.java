package com.example.lulavillalobos.bakingapp.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lulavillalobos.bakingapp.Adapters.StepListAdapter;
import com.example.lulavillalobos.bakingapp.Database.AppDatabase;
import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.Model.Step;
import com.example.lulavillalobos.bakingapp.R;
import com.example.lulavillalobos.bakingapp.ViewModel.RecipeViewModel;
import com.example.lulavillalobos.bakingapp.ViewModel.RecipeViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class StepListFragment extends Fragment {

    private static final String TAG = StepListFragment.class.getSimpleName();
    private int recipe_id;
    private List<Step> steps;
    private AppDatabase database;
    private RecyclerView recyclerView;
    private StepListAdapter stepListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        recyclerView = rootView.findViewById(R.id.rv_step_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(null);

        database = AppDatabase.getInstance(getContext());

        // TODO: implement onClickListener

        return rootView;
    }

    public void setupStepList() {
        RecipeViewModelFactory factory = new RecipeViewModelFactory(database, recipe_id);
        RecipeViewModel viewModel =
                ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                steps = recipe.getSteps();
                stepListAdapter = new StepListAdapter(steps);
                recyclerView.setAdapter(stepListAdapter);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("step_list")) {
            steps = savedInstanceState.getParcelableArrayList("step_list");
            stepListAdapter = new StepListAdapter(steps);
            recyclerView.setAdapter(stepListAdapter);
        } else {
            //get recipe_id from bundle
            if (getArguments() != null) {
                recipe_id = getArguments().getInt("RECIPE_ID");
                setupStepList();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("step_list", (ArrayList<Step>)steps);
        super.onSaveInstanceState(outState);
    }
}
