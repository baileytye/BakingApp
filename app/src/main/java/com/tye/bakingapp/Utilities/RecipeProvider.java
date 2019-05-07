package com.tye.bakingapp.Utilities;

import android.content.Context;
import android.util.Log;

import com.tye.bakingapp.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeProvider {

    public interface ReceiveRecipeCallback{
        public void onDone(List<Recipe> recipes);
    }

    private ReceiveRecipeCallback callback;

    public RecipeProvider(ReceiveRecipeCallback c){
        callback = c;
    }

    public void retrieveRecipeList(){

        Log.i("Receive Recipes", "Retrieving recipes...");

        //Use retrofit to get data from movie database
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkRecipeRetrofit recipeProvider = retrofit.create(NetworkRecipeRetrofit.class);

        Call<List<Recipe>> call;
        call = recipeProvider.getRecipeList();

        //Retrieve data from server and then tell the adapter that data has changed
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){

                    Log.e("HTTP Request Error", String.valueOf(response.code()));
                    return;
                }
                if (response.body() != null) {
                    callback.onDone(response.body());
                } else {
                    Log.e("Movie List Error", "Movie list is null");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("Retrofit Call Error", t.getMessage());
            }
        });

    }

}
