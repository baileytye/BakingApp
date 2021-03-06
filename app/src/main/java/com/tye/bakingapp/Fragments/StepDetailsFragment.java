package com.tye.bakingapp.Fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.tye.bakingapp.Models.Recipe;
import com.tye.bakingapp.Models.Step;
import com.tye.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tye.bakingapp.Fragments.RecipeFragment.EXTRA_RECIPE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    private final static String EXTRA_POSITION = "extra_position";
    private final static String EXTRA_PLAY_WHEN_READY = "extra_play_when_ready";

    private Step mStep;
    private Recipe mRecipe;
    private int mStepNumber;

    private long currentPosition;
    private boolean playWhenReady;


    private SimpleExoPlayer simpleExoPlayer;

    @BindView(R.id.exo_player_view)  PlayerView playerView;
    @Nullable
    @BindView(R.id.tv_step_instruction) TextView mStepInstructionTextView;
    @Nullable
    @BindView(R.id.b_next_step) Button mNextStepButton;
    @Nullable
    @BindView(R.id.b_previous_step) Button mPreviousStepButton;
    @Nullable
    @BindView(R.id.tv_no_video) TextView mNoVideoTextView;
    @Nullable
    @BindView(R.id.iv_step_image) ImageView mStepImageView;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StepDetailsFragment.
     */
    public static StepDetailsFragment newInstance(int stepNumber, Recipe recipe) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(Intent.EXTRA_INDEX, stepNumber);
        args.putParcelable(EXTRA_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(EXTRA_RECIPE) && savedInstanceState.containsKey(Intent.EXTRA_INDEX)) {
                mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
                mStepNumber = savedInstanceState.getInt(Intent.EXTRA_INDEX);
            }
            if(savedInstanceState.containsKey(EXTRA_PLAY_WHEN_READY)){
                playWhenReady = savedInstanceState.getBoolean(EXTRA_PLAY_WHEN_READY);
            }
            if(savedInstanceState.containsKey(EXTRA_POSITION)){
                currentPosition = savedInstanceState.getLong(EXTRA_POSITION);
            }
        } else if(getArguments() != null){
            mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
            mStepNumber = getArguments().getInt(Intent.EXTRA_INDEX);
            playWhenReady = false;
            currentPosition = 0;
        }

        mStep = mRecipe.getSteps().get(mStepNumber);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        boolean isTablet = (rootView.findViewById(R.id.tablet_steps_layout) != null);

        ButterKnife.bind(this, rootView);

        //If is a phone
        if(!isTablet) {
            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (mStepInstructionTextView != null) {
                    mStepInstructionTextView.setText(mStep.getDescription());
                }
                mNextStepButton.setOnClickListener(v -> {
                    if(mStepNumber < mRecipe.getSteps().size() - 1) {
                        mStep = mRecipe.getSteps().get(++mStepNumber);
                        updateStepUI();
                    }
                });

                mPreviousStepButton.setOnClickListener(v -> {
                    if(mStepNumber > 0) {
                        mStep = mRecipe.getSteps().get(--mStepNumber);
                        updateStepUI();
                    }
                });

                displayButtons();
            } else {
                rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                displayVideoOrImageIfPresent();
            }
        //Tablet case for portrait and landscape
        } else {
            if (mStepInstructionTextView != null) {
                mStepInstructionTextView.setText(mStep.getDescription());
            }
        }

        if(simpleExoPlayer == null) {
            initializePlayer();
            prepareVideo();
        }
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putInt(Intent.EXTRA_INDEX, mStepNumber);
        outState.putLong(EXTRA_POSITION, currentPosition);
        outState.putBoolean(EXTRA_PLAY_WHEN_READY, playWhenReady);
    }

    /**
     * Initialize exoplayer
     */
    private void initializePlayer() {

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector,loadControl);
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.addListener(this);
    }

    /**
     * Prepare video from url
     */
    private void prepareVideo(){

        if(!displayVideoOrImageIfPresent()) return;

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory( getContext(),
                Util.getUserAgent(getContext(), "BakingApp"));

        Uri uri = Uri.parse(mStep.getVideoURL());

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
        simpleExoPlayer.seekTo(currentPosition);

    }

    /**
     * Displays the current views given what is required from Step
     * @return returns true if video is present
     */
    private boolean displayVideoOrImageIfPresent(){

        boolean videoPresent = true;

        if(mStep.getVideoURL().equals("")) {
            playerView.setVisibility(View.GONE);
            if (mNoVideoTextView != null) mNoVideoTextView.setVisibility(View.VISIBLE);
            videoPresent = false;
        } else {
            playerView.setVisibility(View.VISIBLE);
            if(mNoVideoTextView != null) mNoVideoTextView.setVisibility(View.GONE);
        }
        if(mStep.getThumbnailURL().equals("")){
            if(mStepImageView != null) mStepImageView.setVisibility(View.INVISIBLE);
            return true;
        } else {
            if(mStepImageView != null) mStepImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(mStep.getThumbnailURL()).into(mStepImageView);
        }

        return videoPresent;
    }

    /**
     * Release the player and save states
     */
    private void releasePlayer(){

        currentPosition = simpleExoPlayer.getCurrentPosition();
        playWhenReady = simpleExoPlayer.getPlayWhenReady();
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;
    }

    /**
     * Updates UI with new step values
     */
    private void updateStepUI(){

        simpleExoPlayer.stop();
        prepareVideo();
        simpleExoPlayer.setPlayWhenReady(false);
        mStepInstructionTextView.setText(mStep.getDescription());

        displayButtons();
    }

    /**
     * Displays next/previous step buttons
     */
    private void displayButtons(){
        if(mStepNumber == mRecipe.getSteps().size() - 1){
            mNextStepButton.setVisibility(View.INVISIBLE);
        } else {
            mNextStepButton.setVisibility(View.VISIBLE);
        }

        if(mStepNumber == 0){
            mPreviousStepButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousStepButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(simpleExoPlayer == null) {
            initializePlayer();
            prepareVideo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(simpleExoPlayer == null) {
            initializePlayer();
            prepareVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(simpleExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (simpleExoPlayer != null) {
            releasePlayer();
        }
    }

}
