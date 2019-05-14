package com.tye.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private final String ingredient;
    private final float quantity;
    private final String measure;

    public Ingredient(String ingredient, float quantity, String measure) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }

    private Ingredient(Parcel in){
        ingredient = in.readString();
        quantity = in.readFloat();
        measure = in.readString();
    }

    public String getIngredient() {
        return ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredient);
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}