package com.example.android.bakeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.data.Recipe;

/**
 * Created by bjoern on 31.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description:
 */
public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeViewHolder> {

    Context mContext;
    Recipe mCurrentRecipe;
    OnItemClickListener mItemClickListener;
    /**
     * Default constructor
     *
     * @param context       // Contains the context
     * @param currentRecipe // Contains the current recipe object.
     */

    public RecipeStepAdapter(Context context, Recipe currentRecipe) {
        this.mCurrentRecipe = currentRecipe;
        this.mContext = context;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_items, parent, false);
        return new RecipeViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
        holder.setRecipeStepsShortDescription(mCurrentRecipe.getStepsShortDescription(position));
    }

    @Override
    public int getItemCount() {
        return mCurrentRecipe.getStepsSize();

    }

    public  class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener  {

        TextView mStepsShortDescription;
        TextView mSteps;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mStepsShortDescription = (TextView) itemView.findViewById(R.id.tvStepShortDescription);
            mStepsShortDescription.setOnClickListener(this);
            mSteps = (TextView) itemView.findViewById(R.id.tvStepDescription);
        }

        private void setRecipeStepsShortDescription(String mDescription) {
            mStepsShortDescription.setText(mDescription);
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
