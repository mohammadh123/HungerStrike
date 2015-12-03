package csc413.com.t7.hungerstrike.RecipeFinders;
/**
 * Created by Mardan Anwar and Anu on 11/30/2015.
 */
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import csc413.com.t7.hungerstrike.RecipeStrings.RecipeTie;
import csc413.com.t7.hungerstrike.VolleySingleton;

public class RecipesFinder
{
    private String key;
    public String apiName;

    private String EdamamKey;
    private String EdamamAPIName;

    private Context parentActivity;

    private AtomicInteger nRequests;

    private int max_queries = 8;

    private List<RecipeTie> foundRecipes;

    private VolleySingleton vollySingleton;

    private boolean too_many_queries(int nqueries, String[] recipe_ids)
    {
        return (nqueries + recipe_ids.length) >= max_queries;
    }

    public RecipesFinder(String apiName, String key, String EdamamKey, Context context,
                         final VolleySingleton vollySingleton, final List<RecipeTie> foundRecipes
            , final AtomicInteger nRequests)
    {
        this.EdamamAPIName = EdamamAPIName;
        this.EdamamKey = EdamamKey;

        this.apiName = apiName;
        this.key = key;

        this.parentActivity = context;
        this.vollySingleton = vollySingleton;

        this.foundRecipes = foundRecipes;

        this.nRequests = nRequests;
    }

    public void setMax_queries(int max_queries) {
        this.max_queries = max_queries;
    }

    /** Give Ingredients Request a JSON response from the provided URL / ingredients. */
    public void query(List<String> ingredients)
    {
        JsonObjectRequest jsObjRequest, jsObjRequest2;

        // Default RecipeTie Handlers
        Yummly_Handler api_handler = new Yummly_Handler(nRequests, max_queries);

        jsObjRequest = api_handler.jsObjRequest(parentActivity, ingredients, foundRecipes);
        jsObjRequest2 = api_handler.jsObjRequest2(parentActivity, ingredients, foundRecipes);

//        jsObjRequest.setTag(apiName);

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(parentActivity).addToRequestQueue(jsObjRequest);
        VolleySingleton.getInstance(parentActivity).addToRequestQueue(jsObjRequest2);

    }

    /** Inquire and save ingredients to the given RecipeTie */
    public RecipeTie loadRecipeIngredients(final RecipeTie recipe)
    {
        JsonObjectRequest jsObjRequest, jsObjRequest2;

        Yummly_Handler api_handler = new Yummly_Handler(nRequests, max_queries);

        jsObjRequest = api_handler.jsObjRequest(parentActivity, recipe);
        jsObjRequest2 = api_handler.jsObjRequest2(parentActivity, recipe);

//        jsObjRequest.setTag(apiName);

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(parentActivity).addToRequestQueue(jsObjRequest);
        VolleySingleton.getInstance(parentActivity).addToRequestQueue(jsObjRequest2);

        return recipe;
    }

//** START OF RECIPE PARSE */

    public class Yummly_Handler
    {
//        public String search_url2 = "https://api.edamam.com/search?";
//        public String get_url2 = "https://api.edamam.com/search?";

        public String search_url = "http://api.yummly.com/v1/api/recipes?";
        public String get_url = "http://api.yummly.com/v1/api/recipes?";

        public AtomicInteger nRequests;
        public int max_queries;

        public Yummly_Handler(AtomicInteger nRequests, int max_queries)
        {
            this.nRequests = nRequests;
            this.max_queries = max_queries;
        }

        public JsonObjectRequest jsObjRequest(final Context parentActivity, List<String> ingredients, final List<RecipeTie> foundRecipes)
        {
            String url = search_url + String.format("_app_id=ed502541&_app_key=%s&q=%s", key, TextUtils.join(",", ingredients.toArray()));
//            String url2 = search_url2 + String.format("q=%s&app_id=5b5cdfb6&app_key=%s", TextUtils.join(",", ingredients.toArray()), EdamamKey);

            // Add 1 to the number of requests made
            nRequests.incrementAndGet();
            // Retrieve recipes
            JsonObjectRequest jsObjR = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try
                    {
                        JSONArray recipes = response.getJSONArray("matches");
                        nRequests.decrementAndGet();
                        for (int i = 0; i < java.lang.Math.min(max_queries, recipes.length()); i++)
                        {
                            // Load ingredients of the recipes
                            foundRecipes.add(loadRecipeIngredients(jsonToRecipe(recipes.getJSONObject(i))));
                        }
                    } catch (JSONException ex) {
                        if (response.has("error")) {
                            Toast.makeText(parentActivity, "Limit reached", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(parentActivity, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub}
                        }
                    });
            return jsObjR;
        }

        public JsonObjectRequest jsObjRequest(final Context parentActivity, final RecipeTie recipe) {
            String url = get_url + String.format("_app_id=ed502541&_app_key=%s&q=%s", key, recipe.getRId());
            // Add 1 to the number of requests made
            nRequests.incrementAndGet();
            // Read ingredients for each recipe
            final JsonObjectRequest jsObjR = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() //ingredientResponseListener(parentActivity,recipe),
            {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        nRequests.decrementAndGet();
                        JSONArray ingredients = response.getJSONObject("matches").getJSONArray("ingredients");
                        for (int i = 0; i < ingredients.length(); i++) {
                            // recipe.addIngredient(new Ingredient(ingredients.getString(i), 0));
                        }
                    } catch (JSONException ex) {
                        if (response.has("error")) {
                            Toast.makeText(parentActivity, "Limit reached", Toast.LENGTH_SHORT).show();
                        } else {
                            ex.printStackTrace();
                        }
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                        }
                    });
            return jsObjR;

        }


        public RecipeTie jsonToRecipe(JSONObject jsonObject)
        {
            final RecipeTie recipe = new RecipeTie();
            String title = "";
            String recipe_id = "";
            String image_url = "";
            String apiname = "yummly";
            String[] ingredientsarray = new String[50];
            try {
                title = Html.fromHtml(jsonObject.getString("recipeName")).toString();
            } catch (JSONException ex) {
                Toast.makeText(parentActivity, "Error parsing title", Toast.LENGTH_SHORT).show();
            }
            try {
                recipe_id = Html.fromHtml(jsonObject.getString("id")).toString();
            } catch (JSONException ex) {
                Toast.makeText(parentActivity, "Error parsing recipe_id", Toast.LENGTH_SHORT).show();
            }
            try {
                image_url = Html.fromHtml(jsonObject.getJSONArray("smallImageUrls").getString(0)).toString();
            } catch (JSONException ex) {
                Toast.makeText(parentActivity, "Error parsing recipe_id", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONArray ingredientjson = jsonObject.getJSONArray("ingredients");

                for (int j = 0; j < ingredientjson.length(); j++) {
                    ingredientsarray[j] = Html.fromHtml(ingredientjson.getString(j)).toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            recipe.setTitle(title);
            recipe.setRId(recipe_id);
            recipe.setImage_url(image_url);
            recipe.setApiname(apiname);
            recipe.setIngredientarr(ingredientsarray);
            return recipe;
        }












        public String search_url2 = "https://api.edamam.com/search?";
        public String get_url2 = "https://api.edamam.com/search?";

        public JsonObjectRequest jsObjRequest2(final Context parentActivity, List<String> ingredients, final List<RecipeTie> foundEdamamRecipes)
        {
            //   q=beef    &app_id=de16cbcb     &app_key=af009801f57f9b9d0813b8f2a8d246f3
            String url = search_url2 + String.format("q=%s&app_id=5b5cdfb6&app_key=%s", TextUtils.join(",", ingredients.toArray()), EdamamKey);

            // Add 1 to the number of requests made
            nRequests.incrementAndGet();
            // Retrieve recipes
            JsonObjectRequest jsObjR2 = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        JSONArray recipes = response.getJSONArray("hits");
                        nRequests.decrementAndGet();
                        for (int i = 0; i < java.lang.Math.min(max_queries,recipes.length()); i++)
                        {
                            // Load ingredients of the recipes
                            foundEdamamRecipes.add(loadRecipeIngredients(jsonToRecipe2(recipes.getJSONObject(i))));
                        }
                    }
                    catch (JSONException ex)
                    {
                        if (response.has("error"))
                        {
                            Toast.makeText(parentActivity, "Limit reached", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            // TODO Auto-generated method stub
                        }
                    });
            return jsObjR2;
        }


        public JsonObjectRequest jsObjRequest2(final Context parentActivity, final RecipeTie edamamRecipe)
        {
            String url = get_url2 + String.format("q=%s&app_id=5b5cdfb6&app_key=%s", edamamRecipe.getTitle(), EdamamKey);
            // Add 1 to the number of requests made
            nRequests.incrementAndGet();
            // Read ingredients for each edamamRecipe
            JsonObjectRequest jsObjR2 = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        nRequests.decrementAndGet();
                        JSONArray ingredients = response.getJSONObject("recipe").getJSONArray("ingredientLines");
                        for (int i = 0; i < ingredients.length(); i++)
                        {
                            //  edamamRecipe.addIngredient(new Ingredient(ingredients.getString(i), 0));
                        }
                    }
                    catch (JSONException ex)
                    {
                        if (response.has("error"))
                        {
                            Toast.makeText(parentActivity, "Limit reached", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            // TODO Auto-generated method stub
                        }
                    });
            return jsObjR2;
        }

        public RecipeTie jsonToRecipe2(JSONObject jsonObject)
        {
            final RecipeTie edamamRecipe = new RecipeTie();
            String title = "";
            String recipe_id = "";
            String image_url = "";
            String apiname = "edamam";
            String[] ingredientsarray = new String[50];

            try {
                title = Html.fromHtml(jsonObject.getJSONObject("recipe").getString("label")).toString();

            } catch (JSONException ex) {
                Toast.makeText(parentActivity, "Error parsing title", Toast.LENGTH_SHORT).show();
            }
            try {
                recipe_id = Html.fromHtml(jsonObject.getJSONObject("recipe").getString("uri")).toString();

            } catch (JSONException ex) {
                Toast.makeText(parentActivity, "Error parsing recipe_id", Toast.LENGTH_SHORT).show();
            }



            try {
                image_url = Html.fromHtml(jsonObject.getJSONObject("recipe").getString("image")).toString();
            } catch (JSONException ex) {
                Toast.makeText(parentActivity, "Error parsing source_url",
                        Toast.LENGTH_SHORT).show();
            }
            try {
                JSONArray ingredientjson = jsonObject.getJSONObject("recipe").getJSONArray("ingredients");

                for (int j = 0; j < ingredientjson.length(); j++) {
                    ingredientsarray[j] = Html.fromHtml(ingredientjson.getJSONObject(j).getString("food")).toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            edamamRecipe.setIngredientarr(ingredientsarray);
            edamamRecipe.setTitle(title);
            edamamRecipe.setRId(recipe_id);
            edamamRecipe.setImage_url(image_url);
            edamamRecipe.setApiname(apiname);
            return edamamRecipe;
        }
    }
}

