package com.tye.bakingapp.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tye.bakingapp.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeProvider {

    public interface ReceiveRecipeCallback{
        void onDone(List<Recipe> recipes);
        void onFail(String error);
    }

    private final ReceiveRecipeCallback callback;

    public RecipeProvider(ReceiveRecipeCallback c){
        callback = c;
    }

    /**
     * Retrieves recipe list from server
     * @param idlingResource for testing
     */
    public void retrieveRecipeList(@Nullable final SimpleIdlingResource idlingResource){

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

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
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
                if(!response.isSuccessful()){

                    Log.e("HTTP Request Error", String.valueOf(response.code()));
                    callback.onFail("HTTP Error " + response.code());
                    return;
                }
                if (response.body() != null) {
                    callback.onDone(response.body());
                } else {
                    Log.e("Recipe List Error", "Recipes is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
                callback.onFail(t.getMessage());
                Log.e("Retrofit Call Error", t.getMessage());
            }
        });

    }

}
