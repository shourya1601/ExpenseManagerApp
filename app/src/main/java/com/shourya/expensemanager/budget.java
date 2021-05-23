package com.shourya.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class budget extends AppCompatActivity {

    DatabaseAdapter da=new DatabaseAdapter(budget.this);

    EditText etAmt;
    TextView budget,spent,left;
    Button save;

    Double sum=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        setTitle("BUDGET");

        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        etAmt=findViewById(R.id.etAmt);
        budget=findViewById(R.id.budget);
        spent=findViewById(R.id.spent);
        left=findViewById(R.id.left);
        save=findViewById(R.id.save);

        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        String startdate=""+cal.getTimeInMillis();
        cal.add(Calendar.MONTH,1);
        String enddate=""+cal.getTimeInMillis();

        sum=0.0;
        try {
            sum=da.customDateSum(startdate,enddate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Double.parseDouble(etAmt.getText().toString())==0.0)
                {
                    Toast.makeText(getApplicationContext(),"Budget cannot be zero",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Budget amount saved",Toast.LENGTH_LONG).show();
                    SharedPreferences sp=getSharedPreferences("MYREF",0);
                    SharedPreferences.Editor ed=sp.edit();
                    ed.putFloat("val",Float.parseFloat(etAmt.getText().toString()));
                    ed.commit();
                    SharedPreferences sp1=getSharedPreferences("MYREF",0);
                    Float total=sp1.getFloat("val",0);
                    budget.setText("Budget Amount: \nRs. "+total);
                    spent.setText("Spent: \nRs. "+sum);
                    left.setText("Left: \nRs. "+(total-sum));
                    etAmt.setText(""+total);
                }
            }
        });

        SharedPreferences sp=getSharedPreferences("MYREF",0);
        Float total=sp.getFloat("val",0);
        budget.setText("Budget Amount: \nRs. "+total);
        spent.setText("Spent: \nRs. "+sum);
        left.setText("Left: \nRs. "+(total-sum));
        etAmt.setText(""+total);
    }


}