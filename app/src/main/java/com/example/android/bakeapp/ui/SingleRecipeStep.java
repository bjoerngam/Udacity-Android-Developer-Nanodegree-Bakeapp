package com.example.android.bakeapp.ui;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 31.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Here we will display one single step of the recipe
 */
public class SingleRecipeStep extends AppCompatActivity
        implements RecipeSingleStepFragment.setNewContent {

    // UI related
    @BindView(R.id.ivPreviousStep)
    ImageView mPreviousStep;
    @BindView(R.id.ivNextStep)
    ImageView mNextStep;
    // Storing the current integer value
    private int mCurrentStepIntegerValue;
    private RecipeSingleStepFragment mRecipeSingleStepFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activiy_fragment_master_single_step);
        ButterKnife.bind(this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideUI();
        }
        else { showSystemUI(); }

        // getting the current object and its content via parcel
        final Recipe mCurrentRecipeStep = getIntent().getExtras().getParcelable("CurrentRecipeStep");
        final int mCurrentRecipePosition = getIntent().getExtras().getInt("CurrentPosition");
        mCurrentStepIntegerValue = mCurrentRecipePosition;

        mRecipeSingleStepFragment = new RecipeSingleStepFragment();
        mRecipeSingleStepFragment
                .setRecipeStepVideoURL(mCurrentRecipeStep.getStepVideoURL(mCurrentRecipePosition));
        mRecipeSingleStepFragment
                .setRecipeStepDescription(mCurrentRecipeStep
                        .getStepsShortDescription(mCurrentRecipePosition));
        mRecipeSingleStepFragment.setContext(getBaseContext());
        setTitle(mCurrentRecipeStep.getRecipeName());

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction
                .add(R.id.frMasterSingleStep, mRecipeSingleStepFragment)
                .commit();

        mNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentStepIntegerValue++;

                if (mCurrentStepIntegerValue < mCurrentRecipeStep.getStepsSize()) {

                    newContent
                            (mCurrentRecipeStep.getStepsDescription(mCurrentStepIntegerValue)
                                    , mCurrentRecipeStep.getStepVideoURL(mCurrentStepIntegerValue)
                                    , mCurrentRecipeStep.getStepThumbnailURL(mCurrentStepIntegerValue));
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.last_step), Toast.LENGTH_LONG).show();
                }
            }
        });
        mPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentStepIntegerValue > 0) {
                    mCurrentStepIntegerValue--;
                    newContent
                            (mCurrentRecipeStep.getStepsDescription(mCurrentStepIntegerValue)
                                    , mCurrentRecipeStep.getStepVideoURL(mCurrentStepIntegerValue)
                            , mCurrentRecipeStep.getStepThumbnailURL(mCurrentStepIntegerValue));
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.first_step), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Here we are updating the fragment with the new content
    @Override
    public void newContent(String mStepsDescription, String mStepVideo, String mThumbnailURL) {
        RecipeSingleStepFragment mFragment = new RecipeSingleStepFragment();
        mFragment.setRecipeStepDescription(mStepsDescription);
        if (!TextUtils.isEmpty(mStepVideo)) {
            mFragment.setContext(getBaseContext());
            mFragment.setRecipeStepVideoURL(mStepVideo);
            mFragment.setRecipeThumbnailURL(mThumbnailURL);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.frMasterSingleStep, mFragment)
                .commit();
    }


    private void hideUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN | //Full screen mode
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |     //Stable when using multiple flags
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | //avoid artifacts when FLAG_FULLSCREEN
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        mNextStep.setVisibility(View.INVISIBLE);
        mPreviousStep.setVisibility(View.INVISIBLE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        Log.i("Show", "Main");
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mNextStep.setVisibility(View.VISIBLE);
        mPreviousStep.setVisibility(View.VISIBLE);
    }
}


