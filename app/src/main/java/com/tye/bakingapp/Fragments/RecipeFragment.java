package com.tye.bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tye.bakingapp.Adapters.FragmentRecipeAdapter;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.Models.Step;
import com.tye.bakingapp.R;

import java.util.Objects;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStepClickRecipeFragmentListener}
 * interface.
 */
public class RecipeFragment extends Fragment implements FragmentRecipeAdapter.OnStepClickedAdapterListener {

    public static final String EXTRA_RECIPE = "extra_recipe";

    private int mColumnCount = 1;
    private OnStepClickRecipeFragmentListener mListener;

    private Recipe mRecipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeFragment() {
    }


    public static RecipeFragment newInstance(Recipe recipe) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_RECIPE)) {
                mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            }
        } else if (getArguments() != null) {
                mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new FragmentRecipeAdapter(mRecipe, this));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickRecipeFragmentListener) {
            mListener = (OnStepClickRecipeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_RECIPE, mRecipe);
    }

    @Override
    public void onStepClickedFromAdapter(int stepNumber, Recipe recipe) {

        mListener.onStepClickFromFragment(stepNumber, recipe);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStepClickRecipeFragmentListener {
        void onStepClickFromFragment(int stepNumber, Recipe recipe);
    }
}
