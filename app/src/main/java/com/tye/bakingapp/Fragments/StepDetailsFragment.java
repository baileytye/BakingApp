package com.tye.bakingapp.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tye.bakingapp.Activities.DetailsActivity;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.Models.Step;
import com.tye.bakingapp.R;

import org.w3c.dom.Text;

import java.util.Objects;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String EXTRA_STEP = "extra_step";

    private Step mStep;

    private TextView textView;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StepDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepDetailsFragment newInstance(Step step) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(EXTRA_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        textView = rootView.findViewById(R.id.textView);

        if(savedInstanceState == null){
            Intent intent = Objects.requireNonNull(getActivity()).getIntent();

            if(intent != null) {
                if (intent.hasExtra(EXTRA_STEP)) {
                    mStep = intent.getParcelableExtra(EXTRA_STEP);
                }
            }

        } else {
            if(savedInstanceState.containsKey(EXTRA_STEP)) {
                mStep = savedInstanceState.getParcelable(EXTRA_STEP);
            }
        }

        if (mStep != null) {
            textView.setText(mStep.getDescription());
        }

        // Inflate the layout for this fragment
        return rootView;
    }

}
