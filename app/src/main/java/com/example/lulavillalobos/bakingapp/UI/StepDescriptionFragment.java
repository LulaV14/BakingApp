package com.example.lulavillalobos.bakingapp.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lulavillalobos.bakingapp.Adapters.IngredientsAdapter;
import com.example.lulavillalobos.bakingapp.Model.Ingredient;
import com.example.lulavillalobos.bakingapp.Model.Step;
import com.example.lulavillalobos.bakingapp.R;
import com.example.lulavillalobos.bakingapp.Widget.IngredientsWidgetProvider;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDescriptionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = StepDescriptionFragment.class.getSimpleName();
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private int mIndex;
    private SimpleExoPlayer player;
    private long player_position;
    private boolean play_when_ready;
    private IngredientsAdapter ingredientsAdapter;

    @BindView(R.id.tv_step_description)
    TextView tvStepDescription;

    @BindView(R.id.btn_next_step)
    Button btnNextStep;

    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;

    @BindView(R.id.btn_add_to_widget)
    Button btnAddToWidget; //TODO: change

    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;

    @BindView(R.id.iv_step_image)
    ImageView ivStepImage;

    @BindView(R.id.rv_ingredients_list)
    RecyclerView rvIngredientsList;

    @BindView(R.id.step_description_container)
    LinearLayout stepDescriptionContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_description, container, false);
        ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvIngredientsList.setLayoutManager(layoutManager);
        rvIngredientsList.setAdapter(null);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                rvIngredientsList.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation()
        );
        rvIngredientsList.addItemDecoration(dividerItemDecoration);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("recipe_ingredients")) {
            mIngredients = savedInstanceState.getParcelableArrayList("recipe_ingredients");
            mSteps = savedInstanceState.getParcelableArrayList("step_list");
            mIndex = savedInstanceState.getInt("step_index");
            player_position = savedInstanceState.getLong("player_position");
            play_when_ready = savedInstanceState.getBoolean("play_when_ready");
        }

        setViewData();
        btnNextStep.setOnClickListener(this);
        btnPreviousStep.setOnClickListener(this);

        btnAddToWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IngredientsWidgetProvider.class);
                intent.setAction(IngredientsWidgetProvider.UPDATE_INGREDIENTS_ACTION);
                getActivity().sendBroadcast(intent);
            }
        });
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public void setStepList(List<Step> steps) {
        mSteps = steps;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("recipe_ingredients", (ArrayList<Ingredient>) mIngredients);
        outState.putParcelableArrayList("step_list", (ArrayList<Step>) mSteps);
        outState.putInt("step_index", mIndex);
        outState.putLong("player_position", player_position);
        outState.putBoolean("play_when_ready", play_when_ready);
        super.onSaveInstanceState(outState);
    }

    private void setViewData() {
        if (mIndex != -1) {
            Step selectedStep = mSteps.get(mIndex);
            tvStepDescription.setText(selectedStep.getDescription());
            if (mIndex < mSteps.size() - 1) {
                Step nextStep = mSteps.get(mIndex + 1);
                btnNextStep.setText("Go To Next Step:\n" + nextStep.getShortDescription());
                btnNextStep.setVisibility(View.VISIBLE);
            } else {
                btnNextStep.setVisibility(View.GONE);
            }

            if (mIndex != 0) {
                Step previousStep = mSteps.get(mIndex - 1);
                btnPreviousStep.setText("Go To Previous Step:\n" + previousStep.getShortDescription());
                btnPreviousStep.setVisibility(View.VISIBLE);
            } else {
                btnPreviousStep.setVisibility(View.GONE);
            }

            if (!selectedStep.getThumbnailURL().isEmpty()) {
                ivStepImage.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(selectedStep.getThumbnailURL())
                        .into(ivStepImage);
            } else {
                ivStepImage.setVisibility(View.GONE);
            }


            String videoUrl = selectedStep.getVideoURL();
            releasePlayer();
            if (!videoUrl.isEmpty()) {
                playerView.setVisibility(View.VISIBLE);
                playerView.requestFocus();
                initializePlayer(videoUrl);
            } else {
                playerView.setVisibility(View.GONE);
            }
            rvIngredientsList.setVisibility(View.GONE);
            stepDescriptionContainer.setVisibility(View.VISIBLE);
        } else {
            // show ingredients list
            ingredientsAdapter = new IngredientsAdapter(mIngredients);
            rvIngredientsList.setAdapter(ingredientsAdapter);
            rvIngredientsList.setVisibility(View.VISIBLE);
            stepDescriptionContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnNextStep.getId()) {
            mIndex++;
        } else {
            mIndex--;
        }
        setViewData();
    }

    private void initializePlayer(String url) {
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(player);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(url),
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            player.prepare(mediaSource);
            if (player_position != 0 && play_when_ready) {
                player.seekTo(player_position);
                player.setPlayWhenReady(play_when_ready);
            }
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player_position = player.getCurrentPosition();
            play_when_ready = player.getPlayWhenReady();
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}
