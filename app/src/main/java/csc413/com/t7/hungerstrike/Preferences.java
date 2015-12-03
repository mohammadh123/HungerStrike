package csc413.com.t7.hungerstrike;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by Mohammad on 11/11/2015.
 */
public class Preferences extends Activity {
    CheckBox OilFree, PeanutFree, Vegan, SugarFree;
    View.OnClickListener checkBoxListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_layout);

        DisplayMetrics display_metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display_metrics);

        int width = display_metrics.widthPixels;
        int height = display_metrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        OilFree = (CheckBox) findViewById(R.id.OFCheckBox);
        PeanutFree = (CheckBox) findViewById(R.id.PFCheckBox);
        Vegan = (CheckBox) findViewById(R.id.VeganCheckBox);
        SugarFree = (CheckBox) findViewById(R.id.SFCheckBox);

        String pf = PeanutFree.getText().toString();
        String of = OilFree.getText().toString();
        String v = Vegan.getText().toString();
        String sf = SugarFree.getText().toString();

        checkBoxListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.OFCheckBox:
                        if(OilFree.isChecked()){
                            Toast.makeText(getBaseContext(), "Oil Free is checked", Toast.LENGTH_LONG).show();
                            //API call for Kosher
                        } else {
                            Toast.makeText(getBaseContext(), "Oil Free is unchecked", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.PFCheckBox:
                        if(PeanutFree.isChecked()){
                            Toast.makeText(getBaseContext(), "Peanut Free is checked", Toast.LENGTH_LONG).show();
                            //API call for Pork
                        } else {
                            Toast.makeText(getBaseContext(), "Peanut Free is unchecked", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.VeganCheckBox:
                        if(Vegan.isChecked()){
                            Toast.makeText(getBaseContext(), "Vegan is checked", Toast.LENGTH_LONG).show();
                            //API call for vegetarian
                        } else {
                            Toast.makeText(getBaseContext(), "Vegan is unchecked", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.SFCheckBox:
                        if(SugarFree.isChecked()){
                            Toast.makeText(getBaseContext(), "Sugar Free is checked", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getBaseContext(), "Sugar Free is unchecked", Toast.LENGTH_LONG).show();
                        }
                }
            }
        };
        OilFree.setOnClickListener(checkBoxListener);
        PeanutFree.setOnClickListener(checkBoxListener);
        Vegan.setOnClickListener(checkBoxListener);
        SugarFree.setOnClickListener(checkBoxListener);

    }
}
