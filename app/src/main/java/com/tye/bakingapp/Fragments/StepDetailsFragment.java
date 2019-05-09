package com.tye.bakingapp.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.tye.bakingapp.Models.Step;
import com.tye.bakingapp.R;

import java.util.Objects;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String EXTRA_STEP = "extra_step";

    private Step mStep;


    private SimpleExoPlayer simpleExoPlayer;

    @BindView(R.id.exo_player_view)  PlayerView playerView;
    @BindView(R.id.tv_step_instruction) TextView mStepInstructionTextView;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StepDetailsFragment.
     */
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

        ButterKnife.bind(this, rootView);

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
            mStepInstructionTextView.setText(mStep.getDescription());
        }

        if(simpleExoPlayer == null) {
            initializePlayer();
            prepareVideo();
        }
        // Inflate the layout for this fragment
        return rootView;
    }

    private void initializePlayer() {

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector,loadControl);
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.addListener(this);

    }

    private void prepareVideo(){

        if(mStep.getVideoURL().equals("")){
            playerView.setVisibility(View.GONE);
            return;
        }

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory( getContext(),
                Util.getUserAgent(getContext(), "BakingApp"));

        Uri uri = Uri.parse(mStep.getVideoURL());

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

        simpleExoPlayer.prepare(videoSource);
    }

    private void releasePlayer(){
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;
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
