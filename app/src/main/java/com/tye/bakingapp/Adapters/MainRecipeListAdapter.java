package com.tye.bakingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainRecipeListAdapter extends RecyclerView.Adapter<MainRecipeListAdapter.ItemViewHolder> {

    final private ListItemClickListener mListItemClickListener;

    private int mNumberOfItems;

    private List<Recipe> mRecipes;

    private final Context mContext;

    public MainRecipeListAdapter(ListItemClickListener listener, Context context) {
        mListItemClickListener = listener;
        mNumberOfItems = 0;
        mContext = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_recipe, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if(mRecipes != null) holder.bind(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    private void setNumberOfItems(int num) {
        mNumberOfItems = num;
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        setNumberOfItems(recipes.size());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_item_recipe) TextView mItemNameTextView;
        @BindView(R.id.iv_item_recipe) ImageView mItemImageView;


        ItemViewHolder(View view){

            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(mContext, "Position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            mListItemClickListener.onListItemClicked(getAdapterPosition());
        }

        void bind(Recipe recipe){
            mItemNameTextView.setText(recipe.getName());
            if(recipe.getImage().equals("")){
                mItemImageView.setVisibility(View.GONE);
            } else {
                //Could add loading view until ready but not enough time
                Picasso.get().load(recipe.getImage()).into(mItemImageView);
                mItemImageView.setVisibility(View.VISIBLE);
            }

        }
    }

    public interface ListItemClickListener{
        void onListItemClicked(int position);
    }
}
