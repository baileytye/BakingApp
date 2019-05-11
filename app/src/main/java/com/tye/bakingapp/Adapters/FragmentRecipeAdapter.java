package com.tye.bakingapp.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;
import com.tye.bakingapp.Utilities.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentRecipeAdapter extends RecyclerView.Adapter<FragmentRecipeAdapter.ViewHolder> {

    private static final int TYPE_INGREDIENTS = 0;
    private static final int TYPE_STEP = 1;

    final private OnStepClickedAdapterListener mOnStepClickedAdapterListener;

    Recipe mRecipe;
    private boolean mIsTablet;
    private int mSelectedPosition;


    public interface OnStepClickedAdapterListener {
        void onStepClickedFromAdapter(int stepNumber, Recipe recipe);
    }

    public FragmentRecipeAdapter(Recipe recipe, OnStepClickedAdapterListener listener, boolean isTablet, int selectedPosition) {
        mRecipe = recipe;
        mOnStepClickedAdapterListener = listener;
        mSelectedPosition = selectedPosition;
        mIsTablet = isTablet;
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
        holder.itemView.setSelected(mSelectedPosition == position);
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

        @BindView(R.id.tv_step_directions) TextView stepDirectionsTextView;
        @BindView(R.id.tv_step_number) TextView stepNumberTextView;

        Context mContext;

        public StepsViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //TODO: save instance state for rotation
                    if(mIsTablet) {
                        notifyItemChanged(mSelectedPosition);
                        mSelectedPosition = getLayoutPosition();
                        notifyItemChanged(mSelectedPosition);
                    }


                    mOnStepClickedAdapterListener.onStepClickedFromAdapter(
                            getAdapterPosition() - 1, mRecipe);
                }
            });

            mContext = view.getContext();

            ButterKnife.bind(this, view);
        }

        @Override
        public void bindData(int position) {
            stepDirectionsTextView.setText(mRecipe.getSteps().get(position - 1).getShortDescription());
            stepNumberTextView.setText(mContext.getString(R.string.step_number, position - 1));
        }
    }

    public class IngredientsViewHolder extends ViewHolder{

        TextView itemIngridientsTextView;

        public IngredientsViewHolder(View view) {
            super(view);

            itemIngridientsTextView = view.findViewById(R.id.tv_item_ingredients);

        }

        @Override
        public void bindData(int position) {
            itemIngridientsTextView.setText(StringUtils.combineIngredients(mRecipe.getIngredients()));
        }
    }

}
