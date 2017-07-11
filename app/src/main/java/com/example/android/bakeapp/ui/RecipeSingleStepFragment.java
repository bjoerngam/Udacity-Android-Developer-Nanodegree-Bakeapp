package com.example.android.bakeapp.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakeapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 01.06.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: In this fragment we are displaying the ExoPlayer and the StepDescripion.
 */
public class RecipeSingleStepFragment extends Fragment {

    // UI global variables
    @BindView(R.id.tvStepDescription)
    TextView tvStepDescription;
    @BindView(R.id.svRecipeStep)
    SimpleExoPlayerView seRecipeStepVideo;
    @BindView(R.id.ivThumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.cvStepDescription)
    CardView cvStepDescription;

    private String mRecipeStepDescription;
    private String mRecipeStepVideoURL;
    private String mRecipeThumbnailURL;

    private Context mContext;
    private View rootView;
    private SimpleExoPlayer mPlayer;

    private final static String FRAGMENT_VIDEO_URL = "video_url";
    private final static String FRAGMENT_THUMBNAIL_URL = "thumbnail_url";
    private final static String FRAGMENT_STEP_DESCRIPTION = "step_description";

    public RecipeSingleStepFragment() {
    }

    public void setRecipeStepDescription(String mRecipeStepDescription) {
        this.mRecipeStepDescription = mRecipeStepDescription;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getRecipeStepVideoURL() {
        return mRecipeStepVideoURL;
    }

    public void setRecipeStepVideoURL(String mRecipeStepVideoURL) {
        if (!TextUtils.isEmpty(mRecipeStepVideoURL))
            this.mRecipeStepVideoURL = mRecipeStepVideoURL;
        else {
            this.mRecipeStepVideoURL = null;
        }
    }

    /**
     * Check if there is an thumbnail or if there is a mp4 file present instead
     * Ifso we will assign this value to to video URL.
     * @param mRecipeThumbnailURL
     */
    public void setRecipeThumbnailURL( String mRecipeThumbnailURL){
        if (!TextUtils.isEmpty(mRecipeThumbnailURL)){
            this.mRecipeThumbnailURL = mRecipeThumbnailURL;
        }
    }

    public String getmThumbnailURL(){
        return mRecipeThumbnailURL;
    }

    public String getmRecipeStepDescription() {
        return mRecipeStepDescription;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            RecipeSingleStepFragment recipeSingleStepFragment = new RecipeSingleStepFragment();
            recipeSingleStepFragment
                    .setRecipeThumbnailURL(savedInstanceState.getString(FRAGMENT_THUMBNAIL_URL));
            recipeSingleStepFragment
                    .setRecipeStepVideoURL(savedInstanceState.getString(FRAGMENT_VIDEO_URL));
            recipeSingleStepFragment
                    .setRecipeStepDescription(savedInstanceState.getString(FRAGMENT_STEP_DESCRIPTION));

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frMasterSingleStep, recipeSingleStepFragment)
                    .commit();

        }
            // If the user is using a cellphone
            rootView = inflater.inflate(R.layout.single_recipe_step, container, false);
            ButterKnife.bind(this, rootView);
            readBundle(getArguments());
            tvStepDescription.setText(getmRecipeStepDescription());
            if (!TextUtils.isEmpty(getmThumbnailURL())){
                ivThumbnail.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(getmThumbnailURL())
                        .resize(220,300)
                        .into(ivThumbnail);
            }
            setVideoPlayer();
            return rootView;
    }

    // Here we setting up the ExoPlayer
    // We are using the variable mRecipeStepVideoURL for getting the correct URL
    public void setVideoPlayer() {
        if (TextUtils.isEmpty(getRecipeStepVideoURL())) {
            seRecipeStepVideo.setVisibility(View.INVISIBLE);
        } else {
            // Check what kind of orientation we are using
            if (! TextUtils.isEmpty(getRecipeStepVideoURL())) {
                videoOrientation();
            }
            // I am using the default example by Google inside the ExoPlayer tutorial.
            seRecipeStepVideo.setVisibility(View.VISIBLE);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mPlayer =
                    ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            // Bind the player to the view.
            seRecipeStepVideo.setPlayer(mPlayer);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "BakingApp"));

            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mRecipeStepVideoURL),
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            mPlayer.prepare(videoSource);
        }
    }

    public void videoOrientation(){

        if (getResources().getConfiguration().smallestScreenWidthDp < 500){
            mobileFullScreen();
        } else { tabletFullScreen(); }
    }

    public  void tabletFullScreen(){

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && ! TextUtils.isEmpty(getRecipeStepVideoURL())){
            // The screen is in landscape mode
            hideSystemUI();
            //Remove the basic UI elements.
            tvStepDescription.setVisibility(View.INVISIBLE);
            cvStepDescription.setVisibility(View.VISIBLE);
            ivThumbnail.setVisibility(View.GONE);
            LinearLayout llStep = (LinearLayout) getActivity().findViewById(R.id.llIngredients);
            llStep.setVisibility(View.GONE);

            //increasing the FrameLayout
            FrameLayout frMaster = (FrameLayout) getActivity().findViewById(R.id.frMasterSingleStep);
            LinearLayout.LayoutParams parms2 = (LinearLayout.LayoutParams) frMaster.getLayoutParams();
            parms2.height = LinearLayout.LayoutParams.MATCH_PARENT;
            frMaster.setLayoutParams(parms2);

            // Adjust the Exo Player layout
            LinearLayout.LayoutParams
                    params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,0,0);
            seRecipeStepVideo.setLayoutParams(params);
            seRecipeStepVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        } else {
            // The screen is in portrait mode
            showSystemUI();
            tvStepDescription.setVisibility(View.VISIBLE);
            cvStepDescription.setVisibility(View.VISIBLE);
            ivThumbnail.setVisibility(View.INVISIBLE);
            LinearLayout llStep = (LinearLayout) getActivity().findViewById(R.id.llIngredients);
            llStep.setVisibility(View.VISIBLE);
        }
    }

    public void mobileFullScreen(){

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !TextUtils.isEmpty(getRecipeStepVideoURL())){
            // The screen is in landscape mode
            hideSystemUI();
            tvStepDescription.setVisibility(View.INVISIBLE);
            cvStepDescription.setVisibility(View.INVISIBLE);
            ivThumbnail.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams
                    params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            params.leftMargin = 0;
            params.rightMargin = 0;
            params.topMargin = 0;
            seRecipeStepVideo.setLayoutParams(params);
            seRecipeStepVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        } else {
            // The screen is in portrait mode
            showSystemUI();
            tvStepDescription.setVisibility(View.VISIBLE);
            cvStepDescription.setVisibility(View.VISIBLE);
            ivThumbnail.setVisibility(View.INVISIBLE);
        }

    }

    public interface setNewContent {
        // We have to use this interface to interact with the fragments
        void newContent(String mStepsDescription, String mStepVideo, String mStepThumbnailURL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // When the view is destroyed we are also releasing
        // the ExoPlayer.
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     * source: http://www.androiddesignpatterns.com/2012/05/using-newinstance-to-instantiate.html
     */
    public static RecipeSingleStepFragment newInstance
    (String mSingleStep, String mVideoURL, String mThumbnailURL) {
        RecipeSingleStepFragment myFragment = new RecipeSingleStepFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_STEP_DESCRIPTION, mSingleStep);
        args.putString(FRAGMENT_VIDEO_URL, mVideoURL);
        args.putString(FRAGMENT_THUMBNAIL_URL, mThumbnailURL);
        myFragment.setArguments(args);
        return myFragment;
    }

    /**
     * We are using this function for reading the bundle.value.
     * @param bundle bundle object
     */
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            setRecipeStepDescription(bundle.getString(FRAGMENT_STEP_DESCRIPTION));
            setRecipeStepVideoURL(bundle.getString(FRAGMENT_VIDEO_URL));
            setRecipeThumbnailURL(bundle.getString(FRAGMENT_THUMBNAIL_URL));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // When the view is destroyed we are also releasing
        // the ExoPlayer.
        if (mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_STEP_DESCRIPTION, getmRecipeStepDescription());
        outState.putString(FRAGMENT_THUMBNAIL_URL, getmThumbnailURL());
        outState.putString(FRAGMENT_VIDEO_URL, getRecipeStepVideoURL());
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mPlayer != null){
        mPlayer.release();
        mPlayer = null;
        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
