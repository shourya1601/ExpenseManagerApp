package com.shourya.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class customDateRange extends AppCompatActivity {

    //variables for views
    EditText etStart,etEnd;
    Button enter;
    Spinner spinner;

    //for selected category
    String category="ALL CATEGORIES";

    //for dates
    Calendar cal_start=Calendar.getInstance();
    Calendar cal_end=Calendar.getInstance();

    //to access DatabaseAdapter
    DatabaseAdapter da=new DatabaseAdapter(customDateRange.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_date_range);

        setTitle("CUSTOM DATE RANGE");

        //linking vies
        etStart=findViewById(R.id.etStart);
        etEnd=findViewById(R.id.etEnd);
        enter=findViewById(R.id.enter);
        spinner=findViewById(R.id.spinner);

        //opening database adapter
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //displaying default date(today)
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat sdf=new SimpleDateFormat(myFormat);
        etStart.setText(sdf.format(cal_start.getTime()));
        etEnd.setText(sdf.format(cal_end.getTime()));

        //to select start date
        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(customDateRange.this, startDate, cal_start.get(Calendar.YEAR), cal_start.get(Calendar.MONTH), cal_start.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //to select end date
        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(customDateRange.this, endDate, cal_end.get(Calendar.YEAR), cal_end.get(Calendar.MONTH), cal_end.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayList<String> items=new ArrayList<String>();
        items.add("ALL CATEGORIES");
        Cursor c = da.getAllDataCategories();
        while(c.moveToNext())
        {
            items.add(c.getString(1));
        }
        c.close();
        ArrayAdapter<String> aa=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        //selecting item from spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category= items.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty
            }
        });

        //on clicking enter button
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //storing start date
                setZeroTime(cal_start);
                long startdate=cal_start.getTimeInMillis();

                //storing end date
                cal_end.set(Calendar.HOUR_OF_DAY,23);
                cal_end.set(Calendar.MINUTE,59);
                cal_end.set(Calendar.SECOND,59);
                cal_end.set(Calendar.MILLISECOND,999);
                long enddate=cal_end.getTimeInMillis();

                Intent i=new Intent(getApplicationContext(),custom_date_display.class);
                i.putExtra("startdate",""+startdate);
                i.putExtra("enddate",""+enddate);
                i.putExtra("category",category);
                startActivity(i);
            }
        });
    }

    //to set date in edit text
    private void updateLabel(Calendar cal,EditText et)
    {
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat sdf=new SimpleDateFormat(myFormat);
        et.setText(sdf.format(cal.getTime()));
    }

    //to set time to zero
    private void setZeroTime(Calendar cal)
    {
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
    }

    //displaying the date picker dialog to select start date
    DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal_start.set(Calendar.YEAR, year);
            cal_start.set(Calendar.MONTH, month);
            cal_start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(cal_start,etStart);
        }
    };

    //displaying the date picker dialog to select end date
    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal_end.set(Calendar.YEAR, year);
            cal_end.set(Calendar.MONTH, month);
            cal_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(cal_end,etEnd);
        }
    };

}