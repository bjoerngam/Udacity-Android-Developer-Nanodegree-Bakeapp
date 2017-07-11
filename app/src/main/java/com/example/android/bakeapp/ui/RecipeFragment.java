package com.example.android.bakeapp.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.adapter.RecipeStepAdapter;
import com.example.android.bakeapp.data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 22.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: This fragment class is storing the basic fragment including:
 * the StepsShortDescription List and the recipename.
 */

public class RecipeFragment extends Fragment{

    @BindView(R.id.list)
    RecyclerView list;

    private Recipe mRecipe;
    private ArrayList<Recipe> mList;
    private RecipeStepAdapter mRecipeStepsAdapter;
    private String mRecipeName;

    private final static String CURRENT_RECIPE_STEP = "CurrentRecipeStep";
    private final static String CURRENT_POSITION = "CurrentPosition";

    private OnSelectedStep mCallback;

    public interface OnSelectedStep{
        void onItemSelected(int position);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);

        // RecyclerView settings
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());

        mList = new ArrayList<>();
        mList.add(mRecipe);
        mRecipeStepsAdapter = new RecipeStepAdapter(getContext(), mRecipe);

        list.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsAdapter.SetOnItemClickListener(new RecipeStepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(RecipeSteps.checkScreenSize(getContext())) {
                    // If we are using a tablet.
                    mCallback.onItemSelected(position);
                }
                else{
                    // If we are using a mobile phone.
                    Intent intent = new Intent(view.getContext(), SingleRecipeStep.class);
                    // Adding the current recipe object to the intent
                    intent.putExtra(CURRENT_RECIPE_STEP, (Parcelable) mRecipe);
                    // Adding the current position to the intent. So we can
                    // easily getting the correct content of the steps list for example.
                    intent.putExtra(CURRENT_POSITION, position);
                    view.getContext().startActivity(intent);
                }
            }
        });
        return rootView;
    }

    public void setRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public void setRecipeObject(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSelectedStep) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSelectedStep");
        }
    }
}
