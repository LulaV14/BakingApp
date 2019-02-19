package com.example.lulavillalobos.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private final RecipeOnClickHandler recipeOnClickHandler;
    private List<Recipe> recipes;

    public RecipeAdapter(List<Recipe> recipes, RecipeOnClickHandler clickHandler) {
        this.recipes = recipes;
        this.recipeOnClickHandler = clickHandler;
    }

    public interface RecipeOnClickHandler {
        void onClick(int recipe_position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Recipe recipe = recipes.get(i);
        // TODO: set image on viewHolder.recipe_image (using Picasso)
        viewHolder.recipe_name.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        @BindView(R.id.iv_recipe_image)
        ImageView recipe_image;
        @BindView(R.id.tv_recipe_name)
        TextView recipe_name;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recipeOnClickHandler.onClick(getAdapterPosition());
        }
    }
}
