package com.tye.bakingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.tye.bakingapp.Adapters.MainRecipeListAdapter;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;
import com.tye.bakingapp.Utilities.RecipeProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;

public class MainActivity extends AppCompatActivity implements MainRecipeListAdapter.ListItemClickListener, RecipeProvider.ReceiveRecipeCallback {

    //Views
    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;

    private MainRecipeListAdapter mRecipeAdapter;
    private List<Recipe> mRecipes;
    private RecipeProvider mRecipeProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        LinearLayoutManager layoutManager;

        if(findViewById(R.id.tablet_main_container) == null) {
            layoutManager = new LinearLayoutManager(this);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }


        mRecipeAdapter = new MainRecipeListAdapter(this, this);

        mRecipeProvider = new RecipeProvider(this);
        mRecipeProvider.retrieveRecipeList();

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    @Override
    public void onListItemClicked(int position) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_RECIPE, mRecipes.get(position));
        startActivity(intent);

    }

    @Override //Providing Up navigation
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDone(List<Recipe> recipes) {
        mRecipes = recipes;
        mRecipeAdapter.setRecipes(mRecipes);
        mRecipeAdapter.notifyDataSetChanged();
        Log.i("ON_CREATE", "Number of recipes: " + mRecipes.size());
    }
}
