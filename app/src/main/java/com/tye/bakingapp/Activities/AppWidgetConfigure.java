package com.tye.bakingapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.tye.bakingapp.Adapters.MainRecipeListAdapter;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;
import com.tye.bakingapp.Utilities.RecipeProvider;
import com.tye.bakingapp.Utilities.SimpleIdlingResource;
import com.tye.bakingapp.Widget.IngredientListWidget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppWidgetConfigure extends AppCompatActivity implements MainRecipeListAdapter.ListItemClickListener, RecipeProvider.ReceiveRecipeCallback {

    private static final String PREFS_NAME = "com.tye.bakingapp";
    private static final String PREF_PREFIX_KEY = "prefix_";

    //Views
    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    @BindView(R.id.tv_main_error) TextView mErrorTextView;
    @BindView(R.id.pb_loading) ProgressBar mProgressBar;

    private MainRecipeListAdapter mRecipeAdapter;
    private List<Recipe> mRecipes;
    private RecipeProvider mRecipeProvider;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_app_widget_configure);

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecipeAdapter = new MainRecipeListAdapter(this, this);

        mRecipeProvider = new RecipeProvider(this);
        mRecipeProvider.retrieveRecipeList(mIdlingResource);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    @Override
    public void onListItemClicked(int position) {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int appWidgetId;

        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

            RemoteViews views = new RemoteViews(getPackageName(),
                    R.layout.ingredient_list_widget);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            IngredientListWidget.updateAppWidget(this, appWidgetManager, appWidgetId, mRecipes.get(position));

            saveIngredientsToPref(this, appWidgetId, mRecipes.get(position));

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);

            Log.i("WIDGET", "Widget conifuration ending");
            finish();
        }

    }

    // Write the prefix to the SharedPreferences object for this widget
    private static void saveIngredientsToPref(Context context, int appWidgetId, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        String serializedRecipe = recipe.serialize();

        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "recipe", serializedRecipe);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadIngredientsFromPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY + appWidgetId + "recipe", null);
    }

    /**
     * Fetch recipes using retrofit2
     */
    private void fetchRecipes(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecipeProvider.retrieveRecipeList(mIdlingResource);
    }


    /**
     * Implemented from recipe provider as callback for receiving recipes
     * @param recipes recipes
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
