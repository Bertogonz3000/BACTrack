package com.example.bgonz.trackyodrankv3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RadioGroup sexGroup, weightGroup;
    RadioButton rbSex, rbWeight;
    EditText weightEdit;
    LinearLayout homePageLayout,trackPageLayout,infoPageLayout;
    int drankCount, weightButtonId;
    TextView drankNum, bacView;
    String drankSelection;
    double gramsConsumed, volumeConsumed, alcoholContent, genderConstant, gramsWeight, bac;
    Spinner drankSpinner;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_start:
                    homeOn();
                    trackOff();
                    infoOff();
                    return true;
                case R.id.navigation_track:
                    homeOff();
                    trackOn();
                    infoOff();
                    return true;
                case R.id.navigation_info:
                    homeOff();
                    trackOff();
                    infoOn();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //sexGroup declarations

        sexGroup = (RadioGroup) findViewById(R.id.sexGroup);

        //Declaration for weightGroup items

        weightGroup = (RadioGroup) findViewById(R.id.weightGroup);
        weightEdit = (EditText) findViewById(R.id.weightEdit);

        //Declaration for the layouts, for use in page switching

        homePageLayout = (LinearLayout) findViewById(R.id.homePageLayout);
        trackPageLayout = (LinearLayout) findViewById(R.id.trackPageLayout);
        infoPageLayout = (LinearLayout) findViewById(R.id.infoPageLayout);

        //Declaration of drinkCount/Text
        drankNum = (TextView) findViewById(R.id.drankNum);
        drankCount = 0;

        //Declaration of the drink spinner
        drankSpinner = (Spinner) findViewById(R.id.drankSpinner);
        //Create an arrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this,R.array.dranksArray, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply the adapter to the spinner
        drankSpinner.setAdapter(adapter);

        drankSpinner.setOnItemSelectedListener(this);

        //textView for bac display
        bacView = (TextView) findViewById(R.id.bacView);

    }

    //Method to DRANK!

    public void drank (View v){
        drankCount++;
        drankNum.setText("You've had " + drankCount + " drink(s)");
    }

    //Method to access the clicked radioButtons in sexGroup

    public void sexClick (View v){

        int sexButtonId = sexGroup.getCheckedRadioButtonId();

        rbSex = (RadioButton) findViewById(sexButtonId);

        if (sexButtonId == R.id.maleButton){
            genderConstant =0.68;
        } else {
            genderConstant =0.55;

        }

    }

    //Method to access the clicked RadioButtons in weightGroup

    public void weightClick (View v){

        weightButtonId = weightGroup.getCheckedRadioButtonId();

        rbWeight = (RadioButton) findViewById(weightButtonId);


    }

    //Method to accept the users weight input

    public void weightEditClick (View v){

        if (rbWeight != null) {

        double userWeight = Double.parseDouble(weightEdit.getText().toString());

        Toast.makeText(getBaseContext(),
                "User weight = " + userWeight + " " + rbWeight.getText(), Toast.LENGTH_SHORT)
                .show();

        if (weightButtonId == R.id.poundsButton){
            gramsWeight = userWeight * 453.592;
        } else if (weightButtonId == R.id.kilogramsButton){
            gramsWeight = userWeight * 1000;
        }

        }


    }

    //Methods to switch between pages

    public void homeOff() {
    homePageLayout.setVisibility(View.GONE);
    }

    public void homeOn() {homePageLayout.setVisibility(View.VISIBLE);}

    public void trackOn(){
    trackPageLayout.setVisibility(View.VISIBLE);
    }

    public void trackOff(){
    trackPageLayout.setVisibility(View.GONE);
    }

    public void infoOn(){
    infoPageLayout.setVisibility(View.VISIBLE);
    }

    public void infoOff(){
    infoPageLayout.setVisibility(View.GONE);
    }

    //Methods to accept the user's alcohol choice

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
       String drankSelection = (String) parent.getItemAtPosition(pos).toString();

       //Rethink the way that gramsConsumed is created and used - the problem is that
       // it is not updated every time that DRANK is pressed, it's only updated when we change
       // drinks
      if (drankSelection == drankSpinner.getItemAtPosition(0)){
          volumeConsumed = 12.0;
          alcoholContent = 0.05;


       } else if (drankSelection == drankSpinner.getItemAtPosition(1)){
          volumeConsumed = 5.0;
          alcoholContent = 0.12;



      } else if (drankSelection == drankSpinner.getItemAtPosition(2)){
          volumeConsumed = 1.5;
          alcoholContent = 0.40;


      } else {
           Toast.makeText(getBaseContext(), "Please select a drink", Toast.LENGTH_SHORT).show();
      }
    }


    public void onNothingSelected(AdapterView<?> parent){
    }

    public void calculate(View v){
        gramsConsumed = ((volumeConsumed * drankCount) * 29.5735) * alcoholContent * 0.789;
        Toast.makeText(getBaseContext(), "gramsConsumed = " + gramsConsumed, Toast.LENGTH_SHORT).show();
        bac = (gramsConsumed / (gramsWeight * genderConstant)) * 100;
        bacView.setText("BAC = " + bac);

    }

}
