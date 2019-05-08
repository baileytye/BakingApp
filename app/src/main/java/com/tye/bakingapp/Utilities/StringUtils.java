package com.tye.bakingapp.Utilities;

import com.tye.bakingapp.Models.Ingredient;

import java.util.List;

public class StringUtils {


    public static String combineIngredients(List<Ingredient> ingredients){

        StringBuilder s = new StringBuilder(150);

        for( Ingredient ingredient: ingredients){
            s.append(ingredient.getQuantity());
            s.append(" ");
            s.append(ingredient.getMeasure());
            s.append(" ");
            s.append(ingredient.getIngredient());
            s.append('\n');
        }

        return s.toString();
    }

}
