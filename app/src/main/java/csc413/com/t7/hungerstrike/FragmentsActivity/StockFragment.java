package csc413.com.t7.hungerstrike.FragmentsActivity;
/**
 * Created by Mardan Anwar on 10/20/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import csc413.com.t7.hungerstrike.Preferences;
import csc413.com.t7.hungerstrike.R;
import csc413.com.t7.hungerstrike.RecipeStrings.Ingredient;
import csc413.com.t7.hungerstrike.SQLite.DBInterface;

public class StockFragment extends Fragment
{
    /**
     * This fragment allows user to input a list of ingredients.
     * The ingredients are saved in a database and be used in the other fragments
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private DBInterface dbInterface;
    /** Returns a new instance of this fragment for the given section number */
    public static StockFragment newInstance(int sectionNumber)
    {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        /** Button for adding an ingredient */
        Button addIngredientButton = (Button) rootView.findViewById(R.id.addIngredientButton);
        //addIngredientButton.setEnabled(false);

        /** Add listener to enable or disable button depending on input or no input */
        EditText ing_name = (EditText) rootView.findViewById(R.id.ing_name);
        ing_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check_form_enable_button(rootView);
            }
        });

        addIngredientButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Ingredient Name
                EditText ing_name = (EditText) rootView.findViewById(R.id.ing_name);
                // Define the Ingredient instance
                Ingredient ing = new Ingredient();
                ing.setName(ing_name.getText().toString());

                ing.setPreferred(1);
                // Add ingredient to the Ingredient database
                long status = dbInterface.insertIngredient(ing);

                if (status != -1)
                {
                    /** Update ListView into data base */
                    ListView listView = (ListView) rootView.findViewById(R.id.stockListview);
                    ArrayAdapter<Ingredient> adapter = (ArrayAdapter<Ingredient>) listView.getAdapter();
                    adapter.add(ing);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    /** Ingredient already exists */
                    Toast.makeText(getActivity(), "Already have it", Toast.LENGTH_SHORT).show();
                }

                // Clear form
                ing_name.setText("");
            }
        });
        /** Clear stock */
        Button clearStockButton = (Button) rootView.findViewById(R.id.clearStockButton);
        clearStockButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dbInterface.deleteAllIngredients();
                ListView listView = (ListView) rootView.findViewById(R.id.stockListview);
                ArrayAdapter<Ingredient> adapter = (ArrayAdapter<Ingredient>) listView.getAdapter();
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });
        dbInterface = new DBInterface(getActivity());
        dbInterface.open();

        List<Ingredient> ingredients = dbInterface.getAllIngredients();
        // Set adapter for ListView
        ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(getActivity(),
                android.R.layout.simple_list_item_1,ingredients);
        ListView listView = (ListView) rootView.findViewById(R.id.stockListview);
        listView.setAdapter(adapter);

        Button prefBtn = (Button) rootView.findViewById(R.id.button);
        prefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Preferences.class));
            }
        });

        return rootView;
    }

    @Override
    public void onPause()
    {
        dbInterface.close();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        dbInterface.open();
        super.onResume();
    }

    public static void check_form_enable_button(View rootView)
    {
        /** Input ingredients */
        EditText ing_name = (EditText) rootView.findViewById(R.id.ing_name);

        /** Button for adding an ingredient */
        Button addIngredientButton = (Button) rootView.findViewById(R.id.addIngredientButton);
        if ( ing_name.getText().toString().length() > 0)
        {
            addIngredientButton.setEnabled(true);
        }
        else
        {
            addIngredientButton.setEnabled(false);
        }
    }
}