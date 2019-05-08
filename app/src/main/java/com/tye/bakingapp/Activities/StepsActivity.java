package com.tye.bakingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tye.bakingapp.Models.Step;
import com.tye.bakingapp.R;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;

public class StepsActivity extends AppCompatActivity {

    private Step mStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Intent intent = getIntent();
        if(intent != null) {
            if (intent.hasExtra(EXTRA_RECIPE)) {
                mStep = intent.getParcelableExtra(EXTRA_RECIPE);
            }
        }
    }
}
