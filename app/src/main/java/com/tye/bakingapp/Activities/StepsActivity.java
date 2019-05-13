package com.tye.bakingapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tye.bakingapp.Fragments.StepDetailsFragment;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.Models.Step;
import com.tye.bakingapp.R;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;

public class StepsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(savedInstanceState == null) {
            if (intent != null) {
                if (intent.hasExtra(EXTRA_RECIPE) && intent.hasExtra(Intent.EXTRA_INDEX)) {
                    Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
                    int stepNumber = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
                    StepDetailsFragment fragment = StepDetailsFragment.newInstance(stepNumber, recipe);
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_steps_container, fragment).commit();
                    ab.setTitle(recipe.getName());
                }
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
