package csc413.com.t7.hungerstrike.RowAdapters;
/**
 * Created by Mardan Anwar and Anu on 10/20/2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import csc413.com.t7.hungerstrike.R;
import csc413.com.t7.hungerstrike.RecipeStrings.RecipeTie;
import csc413.com.t7.hungerstrike.SQLite.DBInterface;
import csc413.com.t7.hungerstrike.VolleySingleton;

/**
 * Adapter for displaying the recipes found by RecipesFinder.java and
 * shown in RecipesFragment.java */
public class RowAdapter extends ArrayAdapter<RecipeTie>
{
    RecipeTie recipe;
    ImageLoader mImageLoader;
    private Context context;
    private List<RecipeTie> recipes;
    private DBInterface database;
    //private LayoutInflater inflater;

    public RowAdapter(Context context, List<RecipeTie> recipes, DBInterface database)
    {
        super(context, R.layout.recipe_brief_layout, recipes);
        this.context = context;
        this.recipes = recipes;
        this.database = database;
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /** The three elements shown per row */
    static class ViewHolder
    {
        public TextView recipe_title;
        public NetworkImageView recipe_image;
        public TextView recipe_info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;
        // reuse views
        if (rowView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.recipe_brief_layout, parent, false);

            /** configure view holder */
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.recipe_title = (TextView) rowView.findViewById(R.id.recipe_row_name);
            viewHolder.recipe_image = (NetworkImageView) rowView.findViewById(R.id.recipe_row_image);
            viewHolder.recipe_info = (TextView) rowView.findViewById(R.id.recipe_row_info);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        recipe = recipes.get(position);

        // Get the ImageLoader through your singleton class.
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();

        holder.recipe_image.setImageUrl(recipe.getImage_url(), mImageLoader);
        /** Retrieves image for the recipe */
//        ImageLoader.getInstance().displayImage(recipe.getImage_url(), holder.recipe_image);
        // holder.recipe_image.setImageBitmap(recipe.getImage_url());
        holder.recipe_title.setText(recipe.getTitle());

        holder.recipe_info.setText(recipe.getRId());



        //Show image for the edamamRecipe
        //holder.recipe_image.setImage(edamamRecipe.getImage());
//        holder.recipe_title.setText(recipe.getLabel());

//        holder.recipe_image.setImageUrl(recipe.getImage(), mImageLoader);


//        int[] nUtilised_nMissing = recipe.getNUtilised_nMissing(database.getAllIngredients());
//        holder.recipe_info.setText("Utilise "+ Integer.toString(nUtilised_nMissing[0])
//                                 + " of your stock.  Require " + Integer.toString(nUtilised_nMissing[1]) +" more.");

        return rowView;
    }
}
