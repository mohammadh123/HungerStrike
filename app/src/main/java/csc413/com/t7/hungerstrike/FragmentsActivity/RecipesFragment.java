package csc413.com.t7.hungerstrike.FragmentsActivity;
/**
 * Created by Mardan Anwar on 12/03/2015.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import csc413.com.t7.hungerstrike.R;
import csc413.com.t7.hungerstrike.RecipeFinders.RecipesFinder;
import csc413.com.t7.hungerstrike.RecipeStrings.RecipeTie;
import csc413.com.t7.hungerstrike.RowAdapters.RowAdapter;
import csc413.com.t7.hungerstrike.SQLite.DBInterface;
import csc413.com.t7.hungerstrike.VolleySingleton;
import csc413.com.t7.hungerstrike.getrecipedetail;

public class RecipesFragment extends Fragment
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DBInterface database;
    private RecipesFinder recipeFinder;

    public final List<RecipeTie> foundRecipes = new ArrayList<RecipeTie>();

    public final AtomicInteger nRequests = new AtomicInteger(0);
    private VolleySingleton vollySingleton;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RecipesFragment newInstance(int sectionNumber)
    {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_yummly, container, false);

        database = new DBInterface(getActivity());
        database.open();

        Button searchButton = (Button) rootView.findViewById(R.id.findRecipeButton);
        // Check if there is network connection
        searchButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!isNetworkAvailable()) /** Check if there is network connection */
                {
                    Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    foundRecipes.clear();   /** Clear the list of found recipe */
                    // Show Loading dialog
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true, true);
                    ringProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialog)
                        {
                            clearSearch();
                            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Find recipes
                    findRecipes();

                    CountDownTimer timer = new CountDownTimer(30000, 1000)
                    {
                        public void onTick(long millisUntilFinished)
                        {
                            if (nRequests.get() > 0)
                            {
                                ringProgressDialog.dismiss();
                                // Update ListView
                                ListView listView = (ListView) rootView.findViewById(R.id.foundRecipesList);
                                ArrayAdapter<RecipeTie> adapter = (ArrayAdapter<RecipeTie>) listView.getAdapter();
                                adapter.notifyDataSetChanged();
                                TextView mTxtView = (TextView) rootView.findViewById(R.id.mText);
                                mTxtView.setText(nRequests.toString());
                            }
                        }
                        public void onFinish()
                        {
                            ringProgressDialog.dismiss();
                           /* if (nRequests.get() > 0)
                            {
                                TextView mTxtView = (TextView) rootView.findViewById(R.id.mText);
                                mTxtView.setText(nRequests.toString());
                                clearSearch();
                                Toast.makeText(getActivity(), "Time out", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    }.start();
                }
            }
        });

        Button refreshButton = (Button) rootView.findViewById(R.id.refresh);
        // Check if there is network connection
        refreshButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Update ListView
                ListView listView = (ListView) rootView.findViewById(R.id.foundRecipesList);
                ArrayAdapter<RecipeTie> adapter = (ArrayAdapter<RecipeTie>) listView.getAdapter();
                adapter.notifyDataSetChanged();
                TextView mTxtView = (TextView) rootView.findViewById(R.id.mText);
                mTxtView.setText(nRequests.toString());
            }
        });

        RowAdapter adapter = new RowAdapter(getActivity(),foundRecipes,database);
        ListView listView = (ListView) rootView.findViewById(R.id.foundRecipesList);
        listView.setAdapter(adapter);

//        queue = Volley.newRequestQueue(getActivity());
//        vollySingleton = VolleySingleton.RequestQueue(getActivity());

        vollySingleton = VolleySingleton.getInstance(getActivity());

        recipeFinder = new RecipesFinder("Yummly","0f46aa8f65b15b601ab91354337ccd44","62bb003f040913e957cf81070e39d44b",
                getActivity(), vollySingleton,foundRecipes, nRequests);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getActivity(), "list clicked", Toast.LENGTH_LONG).show();
                String[] allingredients = foundRecipes.get(position).getIngredientarr();

                Bundle extraa = new Bundle();
                extraa.putString("recipeId", foundRecipes.get(position).getRId());
                extraa.putString("imageurl", foundRecipes.get(position).getImage_url());
                extraa.putString("recipename", foundRecipes.get(position).getTitle());
                extraa.putString("apiname", foundRecipes.get(position).getApiname());
               extraa.putStringArray("ingredientarray", allingredients);
                Intent getDetails = new Intent(getActivity(), getrecipedetail.class);
                getDetails.putExtras(extraa);
                startActivity(getDetails);

            }
        });
        return rootView;
    }

        /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }*/

    public void findRecipes()
    {
        List <String> ingredientNames = database.getIngredientNames(true,false);
        recipeFinder.query(ingredientNames);
    }

    public void clearSearch()
    {
        vollySingleton.cancelAll(recipeFinder.apiName);
        nRequests.lazySet(0);
        foundRecipes.clear();
    }

    @Override
    public void onPause()
    {
//        queue.stop();
        database.close();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        database.open();
//        queue.start();
        super.onResume();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (vollySingleton != null)
        {
            vollySingleton.cancelAll(recipeFinder.apiName);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        vollySingleton.getInstance(getActivity()).cancelAll(recipeFinder.apiName);
        nRequests.lazySet(0);
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        /** if no network is available networkInfo will be null otherwise check if we are connected */
        return networkInfo != null && networkInfo.isConnected();
    }
}
