package com.example.lulavillalobos.bakingapp.API;

import com.example.lulavillalobos.bakingapp.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APIService {

    @Headers("Accept: application/json")
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
