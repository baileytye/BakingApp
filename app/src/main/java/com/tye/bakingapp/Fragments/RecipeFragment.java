package com.tye.bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.tye.bakingapp.Adapters.FragmentRecipeAdapter;
import com.tye.bakingapp.Models.Recipe;
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

    private OnStepClickRecipeFragmentListener mListener;

    private Recipe mRecipe;
    private int mSelected;

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
            if(savedInstanceState.containsKey(Intent.EXTRA_INDEX)){
                mSelected = savedInstanceState.getInt(Intent.EXTRA_INDEX);
            } else {
                mSelected = RecyclerView.NO_POSITION;
            }
        } else if (getArguments() != null) {
                mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        boolean isTablet = (Objects.requireNonNull(getActivity()).findViewById(R.id.details_steps_container) != null);

        Log.i("Recipe Fragment", "isTablet: " + isTablet);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new FragmentRecipeAdapter(mRecipe, this, isTablet, mSelected));
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
        outState.putInt(Intent.EXTRA_INDEX, mSelected);

    }

    @Override
    public void onStepClickedFromAdapter(int stepNumber, Recipe recipe) {

        //Steps start after ingredients, add 1
        mSelected = stepNumber + 1;
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
