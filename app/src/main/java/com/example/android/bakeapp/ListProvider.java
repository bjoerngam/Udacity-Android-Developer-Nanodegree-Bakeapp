package com.example.android.bakeapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.android.bakeapp.data.Recipe;
import com.example.android.bakeapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 */

public class ListProvider implements RemoteViewsFactory {
    private final static String INTENT_STRING = "ingredients";
    private ArrayList<Recipe> mRecipes;
    private Context context = null;
    private int appWidgetId;
    private int sharedPrefsResult;

    public ListProvider(Context context, Intent intent) {
        // Getting the shared preference value.
        SharedPreferences prefs =
                context.getSharedPreferences("com.example.android.bakeapp", Context.MODE_PRIVATE);
        sharedPrefsResult = prefs.getInt(MainActivity.SELECTED_RECIPE, 0);
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        gettingJSONData();
    }

    private void gettingJSONData() {
        try {
            //** Getting the JSON results ** //
            mRecipes = new NetworkUtils().execute().get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if (sharedPrefsResult >= 0) {
            // If the user has already selected a recipe the size
            // returning recipes is one.
            return 1;
        }
        return mRecipes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {

        if (sharedPrefsResult >= 0) {
            // A shared pref is present so we will display the ingredients of
            // selected recipe.
            final RemoteViews remoteView = new RemoteViews(
                    context.getPackageName(), R.layout.list_item_widget);
            remoteView.setTextViewText
                    (R.id.tvWidgetRecipeName, mRecipes.get(sharedPrefsResult).getIngredients());
            return remoteView;
        } else {
            // If the user running the app for the first time he can
            // select one recipe.
            final RemoteViews remoteView = new RemoteViews(
                    context.getPackageName(), R.layout.list_item_widget);
            remoteView.setTextViewText(R.id.tvWidgetRecipeName, mRecipes.get(position).getRecipeName());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(INTENT_STRING, (Parcelable) mRecipes.get(position));
            Log.i("ListProvider ", Integer.toString(position));
            remoteView.setOnClickFillInIntent(R.id.tvWidgetRecipeName, fillInIntent);
            return remoteView;
        }

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}
