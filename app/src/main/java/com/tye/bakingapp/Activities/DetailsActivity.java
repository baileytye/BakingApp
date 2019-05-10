package com.tye.bakingapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import com.tye.bakingapp.Fragments.RecipeFragment;
import com.tye.bakingapp.Fragments.StepDetailsFragment;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;

public class DetailsActivity extends AppCompatActivity implements RecipeFragment.OnStepClickRecipeFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        if(getIntent() != null) {
            Log.v("DetailsActivity", "Intent is non null");
            if (getIntent().hasExtra(EXTRA_RECIPE)) {
               Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
               RecipeFragment fragment = RecipeFragment.newInstance(recipe);
               getSupportFragmentManager().beginTransaction().add(R.id.details_recipe_container, fragment).commit();
               ab.setTitle(recipe.getName());
            }
        }
    }

    @Override
    public void onStepClickFromFragment(int stepNumber, Recipe recipe) {
        if(findViewById(R.id.details_steps_container) == null) {
            Log.i("DetailsActivity", "Phone detected");
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtra(EXTRA_RECIPE, recipe);
            intent.putExtra(Intent.EXTRA_INDEX, stepNumber);
            startActivity(intent);
        } else {
            Log.i("DetailsActivity", "Tablet detected");
            StepDetailsFragment fragment = StepDetailsFragment.newInstance(stepNumber, recipe);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_steps_container, fragment).commit();
        }
    }
}
