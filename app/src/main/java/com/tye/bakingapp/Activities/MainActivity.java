package com.tye.bakingapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tye.bakingapp.Adapters.MainRecipeListAdapter;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;
import com.tye.bakingapp.Utilities.RecipeProvider;
import com.tye.bakingapp.Utilities.SimpleIdlingResource;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;

public class MainActivity extends AppCompatActivity implements MainRecipeListAdapter.ListItemClickListener, RecipeProvider.ReceiveRecipeCallback {

    private static final String EXTRA_SCROLL_POSITION = "extra_scroll_position";

    //Views
    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    @BindView(R.id.tv_main_error) TextView mErrorTextView;
    @BindView(R.id.pb_loading) ProgressBar mProgressBar;

    private MainRecipeListAdapter mRecipeAdapter;
    private List<Recipe> mRecipes;
    private RecipeProvider mRecipeProvider;
    private LinearLayoutManager mLayoutManager;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if(findViewById(R.id.tablet_main_container) == null) {
            mLayoutManager = new LinearLayoutManager(this);
        } else {
            mLayoutManager = new GridLayoutManager(this, 3);
        }

        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_SCROLL_POSITION)) {
                mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(EXTRA_SCROLL_POSITION));
            }
        }

        mRecipeAdapter = new MainRecipeListAdapter(this, this);
        mRecipeProvider = new RecipeProvider(this);

        fetchRecipes();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    /**
     * Fetch recipes using retrofit2
     */
    private void fetchRecipes(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecipeProvider.retrieveRecipeList(mIdlingResource);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(EXTRA_SCROLL_POSITION, mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onListItemClicked(int position) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_RECIPE, mRecipes.get(position));
        startActivity(intent);

    }

    @Override //Providing Up navigation
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Implemented from recipe provider as callback for receiving recipes
     * @param recipes
     */
    @Override
    public void onDone(List<Recipe> recipes) {

        mErrorTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mRecipes = recipes;
        mRecipeAdapter.setRecipes(mRecipes);
        mRecipeAdapter.notifyDataSetChanged();
        Log.i("ON_CREATE", "Number of recipes: " + mRecipes.size());
    }

    /**
     * Implemented from recipe provider as callback when recipe fetch fails
     * @param error code
     */
    @Override
    public void onFail(String error){

        mErrorTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mErrorTextView.setText(error + "\nClick to retry...");
        mErrorTextView.setOnClickListener(v -> fetchRecipes());
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
