package com.example.android.bakeapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bjoern on 10.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The class for the ingredients
 */
public class Ingredient implements Parcelable {

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    // Everything around a single ingredient
    private String ingredientName;
    private String ingredientQuantity;
    private String ingredientMeasure;

    /**
     * The default constructor
     *
     * @param ingredientName the name of thr ingredient.
     * @param ingredientQuantity the quantity of the ingredient.
     * @param ingredientMeasure the measure of the ingredient.
     */
    public Ingredient (String ingredientName,
                       String ingredientQuantity,
                       String ingredientMeasure ){

        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientMeasure = ingredientMeasure;
    }

    /**
     * The constructor for the serialization
     * @param parcelable the parcel object
     */

    public Ingredient (Parcel parcelable){
        ingredientName = parcelable.readString();
        ingredientQuantity = parcelable.readString();
        ingredientMeasure = parcelable.readString();
    }

    public String getIngredientName(){
        return ingredientName;
    }

    public String getIngredientQuantity(){
        return ingredientQuantity;
    }

    // Everything around the serialization

    public String getIngredientMeasure(){
        return ingredientMeasure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredientName);
        parcel.writeString(ingredientQuantity);
        parcel.writeString(ingredientMeasure);
    }
    //
}
