package com.example.lulavillalobos.bakingapp.API;

import com.example.lulavillalobos.bakingapp.Model.Recipe;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface APIService {

    @Headers("Accept: application/json")
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

    @GET
    Call<ResponseBody> downloadVideo(@Url String url);

}
