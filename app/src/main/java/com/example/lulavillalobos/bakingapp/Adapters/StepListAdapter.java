package com.example.lulavillalobos.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lulavillalobos.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {
    private static final String TAG = StepListAdapter.class.getSimpleName();
    private Integer stepListSize;// TODO: change to ArrayList / List

    public StepListAdapter(Integer stepListSize) {
        this.stepListSize = stepListSize;
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
        if (i == 0) {
            viewHolder.step_name.setText("Recipe Ingredients");
        } else {
            viewHolder.step_name.setText("Recipe Step " + i + "Description");
        }
    }

    @Override
    public int getItemCount() {
        return stepListSize;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_name)
        TextView step_name;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
