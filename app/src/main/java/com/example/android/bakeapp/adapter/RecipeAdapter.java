package com.example.android.bakeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 10.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The recipe adapter
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    Context mContext;
    ArrayList<Recipe> mRecipe;
    OnItemClickListener mItemClickListener;
    int mPosition;

    public RecipeAdapter(Context mContext, ArrayList<Recipe> mRecipe) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        holder
                .bindData(mRecipe.get(position).getRecipeName()
                        , mRecipe.get(position).getRecipeImage()
                ,mRecipe.get(position).getRecipeServings());
        mPosition = position;
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_recipeName)
        TextView mRecipeName;

        @BindView(R.id.ivRecipeImage)
        ImageView mRecipeImage;

        @BindView(R.id.tvServings)
        TextView mRecipeServings;

        RecipeViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mRecipeName.setOnClickListener(this);
        }
        private void bindData (String recipeName, String recipeImage, String recipeServings){
            mRecipeName.setText(recipeName);
            mRecipeServings.setText(recipeServings);
            // Is a image present
            if(!TextUtils.isEmpty(recipeImage)){
                mRecipeImage.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(recipeImage).into(mRecipeImage);
            }
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}