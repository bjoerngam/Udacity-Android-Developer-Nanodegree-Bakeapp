package com.example.android.bakeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.android.bakeapp.adapter.RecipeAdapter;
import com.example.android.bakeapp.data.Recipe;
import com.example.android.bakeapp.ui.RecipeSteps;
import com.example.android.bakeapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    //Some fixed strings
    public final static String INTENT_EXTRAS = "ingredients";
    public final static String SELECTED_RECIPE = "recipe_value";

    // UI related binding via Butter knife
    @BindView(R.id.rv_recipeOverview)
    RecyclerView mRVRecipeOverview;
    private RecipeAdapter mAdapter;
    private ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Everything around the RecyclerView
        mRVRecipeOverview.setLayoutManager(new LinearLayoutManager(this));
        mRVRecipeOverview.setHasFixedSize(true);
        mRVRecipeOverview.setItemAnimator(new DefaultItemAnimator());

        if (isNetworkConnected()) {
            try {
                //** Getting the JSON results ** //
                mRecipes = new NetworkUtils().execute().get();
                if (mRecipes.isEmpty())
                {Toast.makeText
                        (this, getString(R.string.json_object_null), Toast.LENGTH_LONG).show();

                }
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            mAdapter = new RecipeAdapter(this, mRecipes);
            mRVRecipeOverview.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // Setting the shared preference.
                    setSelectedRecipe(position);
                    // Displaying the correct view.
                    Intent intent = new Intent(view.getContext(), RecipeSteps.class);
                    intent.putExtra(INTENT_EXTRAS, (Parcelable) mRecipes.get(position));
                    view.getContext().startActivity(intent);
                }

            });

        } else {
            Toast.makeText
                    (this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        if (mAdapter != null)
            mRVRecipeOverview.setAdapter(mAdapter);
    }

    private boolean isNetworkConnected() {
        // The helper function for checking if the network connection is working.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void setSelectedRecipe(int position) {
        // Storing the selected recipe
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.android.bakeapp", Context.MODE_PRIVATE);
        prefs.edit().putInt(SELECTED_RECIPE, position).apply();
    }
}
