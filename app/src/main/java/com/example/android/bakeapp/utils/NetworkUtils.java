package com.example.android.bakeapp.utils;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.bakeapp.data.Recipe;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bjoern on 08.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description:
 */
public class NetworkUtils extends AsyncTask <Void, Void, ArrayList<Recipe>>
{
    /**BASE URL**/
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    /**PATH URL**/
    private static final String PATH_URL = "topher";
    private static final String PATH_YEAR = "2017";
    private static final String PATH_MONTH = "May";
    private static final String PATH_PROJECT = "59121517_baking";
    private static final String FILE_NAME = "baking.json";

    public ArrayList<Recipe> recipes;

    public NetworkUtils () { }

    public static URL buildURL(){
        Uri buildURI;
        buildURI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_URL)
                .appendPath(PATH_YEAR)
                .appendPath(PATH_MONTH)
                .appendPath(PATH_PROJECT)
                .appendPath(FILE_NAME)
                .build();
        URL url = null;
        try {
            url = new URL(buildURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    @Override
    protected ArrayList<Recipe> doInBackground(Void... voids) {
        OkHttpClient clientHTTP = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(buildURL())
                .addHeader("content-type", "application/json")
                .build();
        try{ Response response = clientHTTP.newCall(request).execute();
            recipes = BakeJSON.extractFeatureFromJson(response.body().string());
        }catch (IOException e){ e.printStackTrace();}
        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {
        super.onPostExecute(recipes);
    }
}