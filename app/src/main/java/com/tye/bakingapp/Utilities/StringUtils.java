package com.tye.bakingapp.Utilities;

import com.tye.bakingapp.Models.Ingredient;

import java.text.DecimalFormat;
import java.util.List;

public class StringUtils {


    public static String combineIngredients(List<Ingredient> ingredients){

        StringBuilder s = new StringBuilder(150);
        DecimalFormat format = new DecimalFormat("0.#");

        int i = 0;
        for( Ingredient ingredient: ingredients){
            i++;
            s.append(format.format(ingredient.getQuantity()));
            s.append(" ");
            s.append(cleanMeasure(ingredient.getMeasure()));
            s.append(" ");
            s.append(ingredient.getIngredient());
            if(i != ingredients.size())
                s.append('\n');
        }

        return s.toString();
    }

    public static String cleanMeasure(String in){
        String out;
        switch(in){
            case "CUP":
                out = "cup";
                break;
            case "TBLSP":
                out = "tbsp";
                break;
            case "TSP":
                out = "tsp";
                break;
            case "G":
                out = "g";
                break;
            case "UNIT":
                out = "";
                break;
            case "K":
                out = "kg";
                break;
            case "OZ":
                out = "oz";
                break;
            default:
                out = in;
                break;
        }
        return out;
    }

}
