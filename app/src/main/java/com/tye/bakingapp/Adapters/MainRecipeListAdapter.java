package com.tye.bakingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.R;

import java.util.List;


public class MainRecipeListAdapter extends RecyclerView.Adapter<MainRecipeListAdapter.ItemViewHolder> {

    final private ListItemClickListener mListItemClickListener;

    private int mNumberOfItems;

    private List<Recipe> mRecipes;

    private Context mContext;

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

    public void setNumberOfItems(int num) {
        mNumberOfItems = num;
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        setNumberOfItems(recipes.size());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView mTestTextView;

        ItemViewHolder(View view){

            super(view);

            mTestTextView = view.findViewById(R.id.tv_item_recipe);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            mListItemClickListener.onListItemClicked(getAdapterPosition());
        }

        void bind(Recipe recipe){
            mTestTextView.setText(recipe.getName());
        }
    }

    public interface ListItemClickListener{
        void onListItemClicked(int position);
    }
}
