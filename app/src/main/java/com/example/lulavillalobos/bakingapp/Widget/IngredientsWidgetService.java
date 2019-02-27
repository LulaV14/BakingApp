package com.example.lulavillalobos.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.lulavillalobos.bakingapp.Database.AppDatabase;
import com.example.lulavillalobos.bakingapp.Model.Ingredient;
import com.example.lulavillalobos.bakingapp.Model.Recipe;
import com.example.lulavillalobos.bakingapp.R;

import java.util.List;

public class IngredientsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    AppDatabase database;
    List<Ingredient> ingredients;
    private static final int recipe_id = 1;

    public IngredientsRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        database = AppDatabase.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {
        Recipe recipe = database.recipeDao().getSimpleRecipeById(1);
        ingredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {
        database.close();
        ingredients.clear();
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredients == null || ingredients.size() == 0) return null;
        Ingredient ingredient = ingredients.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_ingredient_item);
        views.setTextViewText(R.id.widget_quantity, ingredient.getQuantity().toString());
        views.setTextViewText(R.id.widget_measure, ingredient.getMeasure());
        views.setTextViewText(R.id.widget_ingredient_name, ingredient.getIngredient());

        Bundle extras = new Bundle();
        extras.putInt("EXTRAS_RECIPE_ID", recipe_id);
        Intent fillIntent = new Intent();
        fillIntent.putExtra("ingredient", ingredient); //TODO: check
        fillIntent.putExtras(extras);

        // Make it possible to distinguish the individual on-click
        // action of a given item
        views.setOnClickFillInIntent(R.id.widget_item_container, fillIntent);

        // Return the RemoteViews object.
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
