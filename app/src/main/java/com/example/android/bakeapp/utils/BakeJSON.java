package com.example.android.bakeapp.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.android.bakeapp.data.Ingredient;
import com.example.android.bakeapp.data.Recipe;
import com.example.android.bakeapp.data.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by bjoern on 08.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description:
 */
public class BakeJSON {

    private static final String TAG = BakeJSON.class.getSimpleName();
    //**Static JSON values for the JSONArrays **//
    private static final String JSON_RECIPE_NAME = "name";
    private static final String JSON_INGREDIENTS = "ingredients";
    private static final String JSON_STEPS = "steps";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_SERVINGS = "servings";

    //** Names of the ingredients JSON values **//
    private static final String JSON_INGREDIENTS_QUANTITY = "quantity";
    private static final String JSON_INGREDIENTS_MEASURE = "measure";
    private static final String JSON_INGREDIENTS_NAME = "ingredient";

    //** Name of the steps JSON values **//
    private static final String JSON_STEPS_SHORTDESCRIPTION = "shortDescription";
    private static final String JSON_STEPS_DESCRIPTION = "description";
    private static final String JSON_STEPS_VIDEOURL = "videoURL";
    private static final String JSON_STEPS_THUMBNAILURL = "thumbnailURL";

    //** Figure out if we are getting a JSON object **//
    private static final String JSON_STRING = "[\n  {\n    \"id\": ";


    @Nullable
    public static ArrayList<Recipe> extractFeatureFromJson(String requestJSON) {

        ArrayList<Recipe> recipes = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(requestJSON)) {
            return null;
        }
        if (checkJSON(requestJSON)) {
            //** The different information for of the recipies **//
            try {
                JSONArray jsonRecipes = new JSONArray(requestJSON);

                for (int i = 0; i < jsonRecipes.length(); i++) {
                    JSONObject jsonRecipe = jsonRecipes.getJSONObject(i);
                    String recipeName = jsonRecipe.getString(JSON_RECIPE_NAME);
                    String recipeImage = jsonRecipe.getString(JSON_IMAGE);
                    String recipeServings = jsonRecipe.getString(JSON_SERVINGS);

                    JSONArray jsonIngredients = jsonRecipe.getJSONArray(JSON_INGREDIENTS);
                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    for (int j = 0; j < jsonIngredients.length(); j++) {
                        JSONObject jsonIngredient = jsonIngredients.getJSONObject(j);
                        String name = jsonIngredient.getString(JSON_INGREDIENTS_NAME);
                        String quantity = jsonIngredient.getString(JSON_INGREDIENTS_QUANTITY);
                        String measure = jsonIngredient.getString(JSON_INGREDIENTS_MEASURE);
                        Ingredient ingredient = new Ingredient(name, quantity, measure);
                        ingredients.add(ingredient);
                    }

                    JSONArray jsonSteps = jsonRecipe.getJSONArray(JSON_STEPS);
                    ArrayList<Steps> steps = new ArrayList<>();
                    for (int k = 0; k < jsonSteps.length(); k++) {
                        JSONObject jsonStep = jsonSteps.getJSONObject(k);
                        String shortDescription = jsonStep.getString(JSON_STEPS_SHORTDESCRIPTION);
                        String description = jsonStep.getString(JSON_STEPS_DESCRIPTION);
                        String videoUrl = jsonStep.getString(JSON_STEPS_VIDEOURL);
                        String thumbNail = jsonStep.getString(JSON_STEPS_THUMBNAILURL);
                        if (!thumbNail.isEmpty()) {
                            videoUrl = thumbNail;
                        }
                        Steps stepsObject = new Steps(shortDescription, description, videoUrl, thumbNail);
                        steps.add(stepsObject);
                    }
                    Recipe recipe =
                            new Recipe(recipeName, recipeImage, recipeServings, ingredients, steps);
                    recipes.add(recipe);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }

    /**
     * Check if the result string starts with the basic JSON information
     * And also if we find some of the basic JSON elements and if so its
     * a correct JSON response.
     *
     * @param jsonString JSONString
     * @return true if so and false if not.
     */
    public static boolean checkJSON(String jsonString){
        if (jsonString.contains("ingredients")
                && jsonString.contains("name")
                && jsonString.contains("quantity")
                && jsonString.contains(JSON_STRING)
                && jsonString.contains("measure")){
            return TRUE;
        }
        return FALSE;
    }

}
