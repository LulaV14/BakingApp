package com.example.lulavillalobos.bakingapp.Database;

import android.arch.persistence.room.TypeConverter;

import com.example.lulavillalobos.bakingapp.Model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientListConverter {
    private static Gson gson = new Gson();
    private static Type type = new TypeToken<List<Ingredient>>(){}.getType();

    @TypeConverter
    public static List<Ingredient> stringToIngredientList(String json) {
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String ingredientListToString(List<Ingredient> ingredientList) {
        return gson.toJson(ingredientList, type);
    }
}
