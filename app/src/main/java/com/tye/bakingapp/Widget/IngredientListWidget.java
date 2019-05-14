package com.tye.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.tye.bakingapp.Activities.AppWidgetConfigure;
import com.tye.bakingapp.Activities.DetailsActivity;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;
import com.tye.bakingapp.Utilities.StringUtils;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientListWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget);
        //views.setTextViewText(R.id.appwidget_recipe_name, );


        Intent intent = new Intent(context, DetailsActivity.class);
        //intent.setAction("start_details");
        //intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_RECIPE, recipe);

        views.setTextViewText(R.id.appwidget_recipe_name, recipe.getName());
        views.setTextViewText(R.id.appwidget_ingredients, StringUtils.combineIngredients(recipe.getIngredients()));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_linear_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            String serializedRecipe = AppWidgetConfigure.loadIngredientsFromPref(context, appWidgetId);
            if(serializedRecipe != null) {
                Recipe recipe = Recipe.create(serializedRecipe);
                updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
            }
        }
    }

}

