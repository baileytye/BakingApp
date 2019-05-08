package com.tye.bakingapp.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;


public class FragmentRecipeAdapter extends RecyclerView.Adapter<FragmentRecipeAdapter.ViewHolder> {

    private static final int TYPE_INGREDIENTS = 0;
    private static final int TYPE_STEP = 1;

    Recipe mRecipe;

    public FragmentRecipeAdapter(Recipe recipe) {
        mRecipe = recipe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case TYPE_INGREDIENTS:
                view = inflater.inflate(R.layout.item_fragment_ingredients, parent, false);
                return new IngredientsViewHolder(view);
            case TYPE_STEP:
                view = inflater.inflate(R.layout.item_fragment_recipe_step, parent, false);
                return new StepsViewHolder(view);
            default:
                view = inflater.inflate(R.layout.item_fragment_recipe_step, parent, false);
                return new StepsViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_INGREDIENTS;
        } else return TYPE_STEP;
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size() + 1;
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);

        }
        public abstract void bindData(int position);

    }

    public class StepsViewHolder extends ViewHolder{

        public StepsViewHolder(View view) {
            super(view);
        }

        @Override
        public void bindData(int position) {

        }
    }

    public class IngredientsViewHolder extends ViewHolder{

        TextView textView;

        public IngredientsViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.textView);

        }

        @Override
        public void bindData(int position) {
            textView.setText(mRecipe.getName());
        }
    }

}
