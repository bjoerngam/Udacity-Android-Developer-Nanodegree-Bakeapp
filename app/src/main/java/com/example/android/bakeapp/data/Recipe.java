package com.example.android.bakeapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by bjoern on 08.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Here we are store all of the Recipe information
 * and we are using Parcelable for the serialization of the
 * Recipe objects.
 */
public class Recipe extends ArrayList<Recipe> implements Parcelable {

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }

        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe((parcel));
        }
    };
    // Everything inside the recipe object.
    private String recipeName;                                                  // contains all of the recipeNames
    private String recipeImage;                                                 // contains the recipes image
    private String recipeServings;                                              // contains the recipes servings
    private ArrayList<Steps> recipeSteps = new ArrayList<>();                  // contains all of the recipeSteps
    private ArrayList<Ingredient> recipeIngredients = new ArrayList<>();     // contains all of the recipeIngredients

    /**
     * Default constructor
     * @param recipeName            ArrayList of recipeNames
     * @param recipeSteps           ArrayList of recipeSteps
     * @param recipeIngredients     ArrayList of recipeIngredients
     */

    public Recipe (String recipeName,
                   String recipeImage,
                   String recipeServings,
                   ArrayList<Ingredient> recipeIngredients,
                   ArrayList<Steps> recipeSteps){

        // Assigning the constructor values to the members variables
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeServings = recipeServings;
        this.recipeSteps = recipeSteps;
        this.recipeIngredients = recipeIngredients;
    }

    /**
     * Parcel constructor for the serialization of the Recipe objects
     * @param parcelable            Parcel object
     */

    public Recipe (Parcel parcelable){
        recipeName = parcelable.readString();
        recipeImage = parcelable.readString();
        recipeServings = parcelable.readString();
        parcelable.readTypedList(this.recipeIngredients, Ingredient.CREATOR);
        parcelable.readTypedList(this.recipeSteps, Steps.CREATOR);
    }

    public String getRecipeImage() { return recipeImage;}

    public String getRecipeServings() { return  recipeServings;}

    public String getRecipeName () { return recipeName; }

    public String getStepsDescription (int value) {
        return recipeSteps.get(value).getStepsDescription(); }

    public String getStepsShortDescription (int value){
        return recipeSteps.get(value).getStepsShortDescription(); }

    public String getStepVideoURL(int value) {
        return recipeSteps.get(value).getStepsVideoURL();
    }

    public String getStepThumbnailURL(int value) {
        return recipeSteps.get(value).getStepsThumbnailURL();
    }
    public int getStepsSize() {
        return recipeSteps.size();
    }

    // Returns the complete list of ingredients
    public String getIngredients (){
        String completeIngredients = "";
        for (int i = 0 ; i < recipeIngredients.size(); i++){
            completeIngredients += recipeIngredients.get(i).getIngredientQuantity()
            + " "+ recipeIngredients.get(i).getIngredientMeasure()
                    + " " + recipeIngredients.get(i).getIngredientName()+"\n";}
        return completeIngredients;
    }

    // Serialization //
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.recipeName);
        parcel.writeString(this.recipeImage);
        parcel.writeString(this.recipeServings);
        parcel.writeTypedList(recipeIngredients);
        parcel.writeTypedList(recipeSteps);
    }
    /////
}
