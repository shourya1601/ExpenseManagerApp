package com.shourya.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class update_listitem extends AppCompatActivity {

    //variables for views
    EditText etAmt,etDate,etNote;
    Spinner spinner;
    Button enter;

    //variable for getting date
    Calendar cal=Calendar.getInstance();

    //variables for storing values to be put in table
    String mCat,mNote;
    long mDate;
    double mAmt;

    //variables to store old values
    String mCatold,mNoteold;
    long mDateold;
    double mAmtold;

    int id;

    //to ues DatabaseAdapter class
    DatabaseAdapter da=new DatabaseAdapter(update_listitem.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_listitem);

        setTitle("UPDATE");

        //opening the database
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //da.deleteTableCategories();
        //da.deleteTableIncome();
        //da.test();

        //linking views
        linkViews();

        //Displaying default date i.e current date
        updateLabel();

        //filling items in spinner view from database
        ArrayList<String> items=new ArrayList<String>();
        Cursor c = da.getAllDataCategories();
        while(c.moveToNext())
        {
            items.add(c.getString(1));
        }
        c.close();
        ArrayAdapter<String> aa=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        //retrieving data from clicked list item
        Bundle bdl=getIntent().getExtras();
        etAmt.setText(bdl.getString("amount").substring(4));
        etDate.setText(bdl.getString("date"));
        etNote.setText(bdl.getString("note").substring(5));
        spinner.setSelection(getIndex(bdl.getString("category")));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cal.setTime(sdf.parse(etDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.set(Calendar.HOUR,1);

        id=bdl.getInt("ID");
        mAmtold=Double.parseDouble(bdl.getString("amount").substring(4));
        mNoteold=bdl.getString("note").substring(5);
        mCatold=mCat=bdl.getString("category");
        mDateold=cal.getTimeInMillis();

        //selecting item from spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCat= items.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty
            }
        });

        //Clicking to set date
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(update_listitem.this, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //action to be performed on Enter press
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAmt.getText().toString().equals(""))
                    Toast.makeText(update_listitem.this,"Enter Amount", Toast.LENGTH_LONG).show();
                else {
                    //displaying alert dialog to confirm insertion
                    showDialog();
                }
            }
        });
    }

    private int getIndex(String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equals(myString)){
                return i;
            }
        }
        return 0;
    }

    //specifying the format of date and setting the selected date in edittext(etDate)
    private void updateLabel()
    {
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat sdf=new SimpleDateFormat(myFormat);
        etDate.setText(sdf.format(cal.getTime()));
    }

    //displaying the date picker dialog to select date
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    //show alert dialog
    public void showDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(update_listitem.this);
        builder.setMessage("Are you sure you want to continue?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAmt = Double.parseDouble(etAmt.getText().toString());
                mDate = cal.getTimeInMillis();
                mNote = etNote.getText().toString();
                boolean b=false;
                try {
                    b=da.updateDataIncome(id,mAmt, mCat, mDate, mNote);
                    if (b)
                        Toast.makeText(update_listitem.this, "Data updated", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(update_listitem.this, "Invalid Entry", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.setTitle("Alert Dialog");
        alert.show();
    }

    public void linkViews()
    {
        etAmt=findViewById(R.id.etAmt);
        etDate=findViewById(R.id.etDate);
        etNote=findViewById(R.id.etNote);
        enter=findViewById(R.id.enter);
        spinner=findViewById(R.id.spinner);
    }


}