package csc413.com.t7.hungerstrike;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import csc413.com.t7.hungerstrike.SQLite.DBHelper;
import csc413.com.t7.hungerstrike.SQLite.DBInterface;

/**
 * Created by Mohammad on 11/11/2015.
 */
public class Preferences extends Activity
{
    private Context context;
    DBInterface dbinterface;
    DBHelper dbhelper;

//    public static final String DIET_PREFRENCES = "Diet Preferences";
//    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

    CheckBox Vegetarian, LowFat, Vegan;
    View.OnClickListener checkBoxListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_layout);

        DisplayMetrics display_metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display_metrics);

        int width = display_metrics.widthPixels;
        int height = display_metrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        Vegetarian = (CheckBox) findViewById(R.id.OFCheckBox);
        LowFat = (CheckBox) findViewById(R.id.OFCheckBox);
        Vegan = (CheckBox) findViewById(R.id.VeganCheckBox);

//        final String vegetarian = Vegetarian.getText().toString();
//        final String lowfat = LowFat.getText().toString();
//        final String vegan = Vegan.getText().toString();

        /** Setting up boolean -preferences -Mardan */
        final SharedPreferences preference = getSharedPreferences("pref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preference.edit();

        final boolean checkboxstate1 = preference.getBoolean("vegetarian", false);
        Vegetarian.setChecked(checkboxstate1);

        final boolean checkboxstate2 = preference.getBoolean("lowfat", false);
        LowFat.setChecked(checkboxstate2);

        final boolean checkboxstate3 = preference.getBoolean("vegan", false);
        Vegan.setChecked(checkboxstate3);

//        final Ingredient allergy = new Ingredient();

        checkBoxListener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                switch(v.getId())
                {
                    case R.id.PFCheckBox:
                        if(Vegetarian.isChecked())
                        {
                            Toast.makeText(getBaseContext(), "Vegetarian is checked", Toast.LENGTH_LONG).show();

                            /** By Mardan */
                            //                        allergy.setAllergy(pf);
                            //                        /** Add ingredient to the Ingredient database */
                            //                        dbInterface.insertAllergy(allergy);
//                            dbhelper.getInstance(context).updatePreference("vegetarian");
//                            SQLiteDatabaseHelper.getInstance(RecipePage.this).addGroceryRow("vegetarian");

                            editor.putBoolean("vegetarian", true);
                            editor.commit();
                        }
                        else
                        {
                            editor.putBoolean("vegetarian", false);
                            editor.commit();
                            Toast.makeText(getBaseContext(), "Vegetarian is unchecked", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.OFCheckBox:
                        if(LowFat.isChecked())
                        {
                            Toast.makeText(getBaseContext(), "Low Fat is checked", Toast.LENGTH_LONG).show();

                            /** By Mardan */
                            //                        allergy.setAllergy(of);
                            //                        /** Add ingredient to the Ingredient database */
                            //                        dbInterface.insertAllergy(allergy);

                            // SQLiteDatabaseHelper.getInstance(RecipePage.this).takeFromInventoryRow(curItem.getName(), curItem.getQuantity());

                            // We need an Editor object to make preference changes.
                            // All objects are from android.context.Context

                            editor.putBoolean("lowfat", true);
                            editor.commit();
                        }
                        else
                        {
                            // SQLiteDatabaseHelper.getInstance(RecipePage.this).addGroceryRow(curItem);
                            editor.putBoolean("lowfat", false);
                            editor.commit();


                            Toast.makeText(getBaseContext(), "Low Fat is unchecked", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.VeganCheckBox:
                        if(Vegan.isChecked())
                        {
                            Toast.makeText(getBaseContext(), "Vegan is checked", Toast.LENGTH_LONG).show();


                            /** By Mardan */
                            //                        allergy.setAllergy(veg);
                            //                        /** Add ingredient to the Ingredient database */
                            //                        dbInterface.insertAllergy(allergy);

                            editor.putBoolean("vegan", true);
                            editor.commit();
                        }
                        else
                        {
                            editor.putBoolean("vegan", false);
                            editor.commit();
                            Toast.makeText(getBaseContext(), "Vegan is unchecked", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        };
        Vegetarian.setOnClickListener(checkBoxListener);
        LowFat.setOnClickListener(checkBoxListener);
        Vegan.setOnClickListener(checkBoxListener);
    }
}
