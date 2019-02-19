package com.example.lulavillalobos.bakingapp.Database;

import android.arch.persistence.room.TypeConverter;

import com.example.lulavillalobos.bakingapp.Model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StepListConverter {
    private static Gson gson = new Gson();
    private static Type type = new TypeToken<List<Step>>(){}.getType();

    @TypeConverter
    public static List<Step> stringToStepList(String json) {
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String stepListToString(List<Step> stepList) {
        return gson.toJson(stepList, type);
    }
}
