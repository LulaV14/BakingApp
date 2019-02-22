package com.example.lulavillalobos.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lulavillalobos.bakingapp.Model.Ingredient;
import com.example.lulavillalobos.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private static final String TAG = IngredientsAdapter.class.getSimpleName();
    private List<Ingredient> ingredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Ingredient ingredient = ingredients.get(i);
        viewHolder.tvQuantity.setText(ingredient.getQuantity().toString());
        viewHolder.tvMeasure.setText(ingredient.getMeasure());
        viewHolder.tvIngredientName.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;

        @BindView(R.id.tv_measure)
        TextView tvMeasure;

        @BindView(R.id.tv_ingredient_name)
        TextView tvIngredientName;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
