package com.tye.bakingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tye.bakingapp.Fragments.RecipeFragment;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;

public class DetailsActivity extends AppCompatActivity implements RecipeFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_RECIPE = "extra_recipe";

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if(intent != null) {
            if (intent.hasExtra(EXTRA_RECIPE)) {
                mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
            }
        }

    }

    @Override
    public void onListFragmentInteraction() {

    }
}
