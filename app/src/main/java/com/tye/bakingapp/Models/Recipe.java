package com.tye.bakingapp.Models;

import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    private class Ingredient{

        private String ingredient;
        private float quantity;
        private String measure;

        public Ingredient(String ingredient, float quantity, String measure) {
            this.ingredient = ingredient;
            this.quantity = quantity;
            this.measure = measure;
        }
    }

    private class Step{

        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoURL;
            this.thumbnailURL = thumbnailURL;
        }
    }

}
