package com.tye.bakingapp.Utilities;

import com.tye.bakingapp.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkRecipeRetrofit {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipeList();

}
