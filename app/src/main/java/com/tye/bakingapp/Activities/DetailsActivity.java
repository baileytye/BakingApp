package com.tye.bakingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tye.bakingapp.Fragments.RecipeFragment;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.Models.Step;
import com.tye.bakingapp.R;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;
import static com.tye.bakingapp.Fragments.StepDetailsFragment.EXTRA_STEP;

public class DetailsActivity extends AppCompatActivity implements RecipeFragment.OnRecipeStepClickListener {



    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if(intent != null) {
            if (intent.hasExtra(EXTRA_RECIPE)) {
                Log.i("DETAILS ACTIVITY", "Recieved recipe");
                mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
            }
        }

    }

    @Override
    public void OnRecipeStepClick(int position) {

    }
}
