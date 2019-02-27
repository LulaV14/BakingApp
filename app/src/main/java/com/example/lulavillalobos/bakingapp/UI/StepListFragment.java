package com.example.lulavillalobos.bakingapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lulavillalobos.bakingapp.Adapters.StepListAdapter;
import com.example.lulavillalobos.bakingapp.Database.AppDatabase;
import com.example.lulavillalobos.bakingapp.Model.Step;
import com.example.lulavillalobos.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment implements StepListAdapter.OnStepClickListener {

    private static final String TAG = StepListFragment.class.getSimpleName();
    private List<Step> steps;
    private StepListAdapter stepListAdapter;
    private OnStepListClickListener onStepListClickListener;

    @BindView(R.id.rv_step_list)
    RecyclerView recyclerView;

    @BindView(R.id.tv_ingredients_step)
    TextView tvIngredientsStep;

    public interface OnStepListClickListener {
        void onStepItemSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            onStepListClickListener = (OnStepListClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    public StepListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(null);

        tvIngredientsStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStepListClickListener.onStepItemSelected(-1);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("step_list")) {
            steps = savedInstanceState.getParcelableArrayList("step_list");
            stepListAdapter = new StepListAdapter(steps, this);
            recyclerView.setAdapter(stepListAdapter);
        } else {
            if (steps != null) {
                stepListAdapter = new StepListAdapter(steps, this);
                recyclerView.setAdapter(stepListAdapter);
            } else {
                Log.e(TAG, "Error: Steps List is null");
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("step_list", (ArrayList<Step>)steps);
        super.onSaveInstanceState(outState);
    }

    // Setter for steps list
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public void onStepSelected(int step_position) {
        onStepListClickListener.onStepItemSelected(step_position);
    }
}
