package com.example.lulavillalobos.bakingapp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lulavillalobos.bakingapp.Adapters.StepListAdapter;
import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;

public class StepListFragment extends Fragment {

    private static final String TAG = StepListFragment.class.getSimpleName();
    private Recipe recipe;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecipeActivity recipeActivity = (RecipeActivity) getActivity();
        recipe = recipeActivity.getRecipe();

        final View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_step_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        StepListAdapter stepListAdapter = new StepListAdapter(11); // TODO: send (recipe.getSteps().size() + 1
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(stepListAdapter);

        // TODO: implement onClickListener

        return rootView;
    }
}
