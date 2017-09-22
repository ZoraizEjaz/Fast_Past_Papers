package zoraiz.fast_past_papers.Activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import zoraiz.fast_past_papers.R;

public class FilterPaper extends AppCompatActivity implements OnItemSelectedListener, View.OnClickListener{

    String spin1Value="";
    String spin2Value="";
    EditText year;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_paper);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter");

        Intent intent = getIntent();
        String courseName = intent.getStringExtra("courseName");

        TextView courseLabel = (TextView) findViewById(R.id.courseName);
        courseLabel.setText("Course : "+courseName);

        year = (EditText) findViewById(R.id.year);
        year.setOnClickListener(this);

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);

        // Spinner element
        Spinner mySpinner1 = (Spinner) findViewById(R.id.spinner1);

        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> spinnerAdapter1;
        ArrayAdapter<String> spinnerAdapter2;

        // Spinner Drop down elements
        List<String> paperTypes = new ArrayList<String>();
        paperTypes.add("All");
        paperTypes.add("Mid 1");
        paperTypes.add("Mid 2");
        paperTypes.add("Final");

        // Spinner Drop down elements
        List<String> semesterTypes = new ArrayList<String>();
        semesterTypes.add("All");
        semesterTypes.add("Fall");
        semesterTypes.add("Spring");
        semesterTypes.add("Summer");
        

        spinnerAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, paperTypes);
        spinnerAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, semesterTypes);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner1.setAdapter(spinnerAdapter1);
        mySpinner2.setAdapter(spinnerAdapter2);


        //spinner 1 setonclick listner
        mySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                // On selecting a spinner item
                spin1Value = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + spin1Value, Toast.LENGTH_LONG).show();


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //spinner 2 set onlick listner

        mySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // On selecting a spinner item
                spin2Value = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + spin2Value, Toast.LENGTH_LONG).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.year:

                break;
            case  R.id.search:
                //1st call a function to search from server and save searched data in list
                //2nd move user to paper activity
                boolean check=true;
                if(spin1Value.equals(""))
                {
                    check=false;
                    Toast.makeText(getApplicationContext(),"Please Select Paper Type", Toast.LENGTH_SHORT).show();
                }
                if(spin2Value.equals(""))
                {
                    check=false;
                    Toast.makeText(getApplicationContext(),"Please Select Semester Type", Toast.LENGTH_SHORT).show();
                }
                if(year.getText().toString().length()<=3)
                {
                    check=false;
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Year", Toast.LENGTH_SHORT).show();
                    year.setError("Invalid year");
                }

//                if(Integer.valueOf(year.getText().toString()) <= 2007)
//                {
//                    Toast.makeText(getApplicationContext(),"Sorry,We Don't Have That Paper", Toast.LENGTH_SHORT).show();
//                    check=false;
//                }

                if(check)
                {
                    Intent i= new Intent(this, Papers.class);
                    startActivity(i);

                }


                break;




        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
            {
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}