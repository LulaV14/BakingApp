package com.example.lulavillalobos.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lulavillalobos.bakingapp.Model.Step;
import com.example.lulavillalobos.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {
    private static final String TAG = StepListAdapter.class.getSimpleName();
    private final OnStepClickListener onStepClickListener;
    private List<Step> steps;

    public StepListAdapter(List<Step> steps, OnStepClickListener clickHandler) {
        this.steps = steps;
        this.onStepClickListener = clickHandler;
    }

    public interface OnStepClickListener {
        void onStepSelected(int step_position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.step_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Step step = steps.get(i);
        viewHolder.step_name.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_step_name)
        TextView step_name;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onStepClickListener.onStepSelected(getAdapterPosition());
            // TODO: handle background color change
        }
    }
}
