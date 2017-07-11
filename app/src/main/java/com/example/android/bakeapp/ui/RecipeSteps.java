package com.example.android.bakeapp.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakeapp.R.id.tvRecipeIngredients;

/**
 * Created by bjoern on 10.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Inflate the either the mobile phone or the tablet
 * layout.
 */
public class RecipeSteps extends Activity
        implements RecipeFragment.OnSelectedStep{

    @BindView(tvRecipeIngredients)
    TextView mRecipeIngredients;

    private Recipe mCurrentRecipe;
    private RecipeSingleStepFragment mRecipeSingleStepFragment;

    private int mCurrentStepIntegerValue;
    private Bundle savedState = null;

    private final static String CURRENT_STATE = "current_state";
    private final static String CURRENT_POSITION = "current_position";
    private final static String CURRENT_RECIPE = "current_recipe";

    //Some fixed strings
    public final static String INTENT_EXTRAS = "ingredients";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_master);
        ButterKnife.bind(this);

        if (savedInstanceState != null && savedState == null){
            // If we have already a fragment here we are getting the bundle
            savedState = savedInstanceState.getBundle(CURRENT_STATE);
            // Getting back the objects.
            mCurrentRecipe = savedState.getParcelable(CURRENT_RECIPE);
            mCurrentStepIntegerValue = savedState.getInt(CURRENT_POSITION);
            mRecipeIngredients.setText(mCurrentRecipe.getIngredients());

            RecipeFragment mRecipeFragment = new RecipeFragment();
            mRecipeFragment.setRecipeObject(mCurrentRecipe);
            mRecipeFragment.setRecipeName(mCurrentRecipe.getRecipeName());
            setTitle(mCurrentRecipe.getRecipeName());

            mRecipeSingleStepFragment = new RecipeSingleStepFragment();
            mRecipeSingleStepFragment
                    .setRecipeStepDescription(mCurrentRecipe
                            .getStepsDescription(mCurrentStepIntegerValue));

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                    .replace(R.id.frMasterSingleStep, mRecipeSingleStepFragment)
                    .replace(R.id.frMasterFragment, mRecipeFragment)
                    .commit();
        }

        if (checkScreenSize(getBaseContext())) {
            // getting the current object and its content via parcel
            mCurrentRecipe = getIntent().getExtras().getParcelable(INTENT_EXTRAS);

            mRecipeIngredients.setText(mCurrentRecipe.getIngredients());

            RecipeFragment mRecipeFragment = new RecipeFragment();
            mRecipeFragment.setRecipeObject(mCurrentRecipe);
            mRecipeFragment.setRecipeName(mCurrentRecipe.getRecipeName());
            setTitle(mCurrentRecipe.getRecipeName());

            // getting the current object and its content via parcel
            mCurrentStepIntegerValue = getIntent().getExtras().getInt(CURRENT_POSITION);

            mRecipeSingleStepFragment = new RecipeSingleStepFragment();
            mRecipeSingleStepFragment
                    .setRecipeStepDescription(mCurrentRecipe
                            .getStepsDescription(mCurrentStepIntegerValue));

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                    .add(R.id.frMasterFragment, mRecipeFragment)
                    .add(R.id.frMasterSingleStep, mRecipeSingleStepFragment)
                    .commit();

        }

        else {
            // The fragment layout for the cellphone version.

            Recipe mCurrentRecipe = getIntent().getExtras().getParcelable(INTENT_EXTRAS);
            mRecipeIngredients.setText(mCurrentRecipe.getIngredients());
            RecipeFragment mRecipeFragment = new RecipeFragment();
            mRecipeFragment.setRecipeObject(mCurrentRecipe);
            mRecipeFragment.setRecipeName(mCurrentRecipe.getRecipeName());
            setTitle(mCurrentRecipe.getRecipeName());

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                    .add(R.id.frMasterFragment, mRecipeFragment)
                    .commit();
        }
    }

    /**
     * Here we are checking if it is a cellphone or tablet.
     * @return true if tablet and false if not.
     */
     public static boolean checkScreenSize(Context context){
        return (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onItemSelected(int position) {
        if (checkScreenSize(getBaseContext()))
        {
            // If we are using a tablet
            mRecipeSingleStepFragment =
                    RecipeSingleStepFragment.newInstance(
                    mCurrentRecipe.getStepsDescription(position),
                    mCurrentRecipe.getStepVideoURL(position),
                    mCurrentRecipe.getStepThumbnailURL(position));

            //Replacing the single step fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                    .replace(R.id.frMasterSingleStep, mRecipeSingleStepFragment)
                    .commit();
        }
    }

    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        state.putParcelable(CURRENT_RECIPE, mCurrentRecipe);
        state.putInt(CURRENT_POSITION, mCurrentStepIntegerValue);
        return state;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(CURRENT_STATE, saveState());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savedState = saveState();
    }
}


