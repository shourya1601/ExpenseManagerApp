package com.shourya.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class update_category extends AppCompatActivity {

    Button enter;
    EditText cat;

    //to ues DatabaseAdapter class
    DatabaseAdapter da=new DatabaseAdapter(update_category.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        setTitle("UPDATE");

        //opening the database
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        enter=findViewById(R.id.enter);
        cat=findViewById(R.id.etCat);

        Intent intent=getIntent();
        String old_category=intent.getExtras().getString("category");
        cat.setText(old_category);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_category=cat.getText().toString().toUpperCase().trim();
                Cursor check=null;
                try {
                    check=da.isExistCategory(new_category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(new_category.equals("")){
                    Toast.makeText(update_category.this,"Please enter valid category",Toast.LENGTH_LONG).show();
                }
                else if(check.moveToFirst())
                {
                    Toast.makeText(update_category.this,"Category already exists",Toast.LENGTH_LONG).show();

                }
                else{
                    boolean b=false;
                    boolean c=false;
                    try {
                        b=da.updateDataCategory(old_category,new_category);
                        c=da.updateDataIncome(old_category,new_category);
                        if (b && c)
                            Toast.makeText(update_category.this, "Data updated", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(update_category.this, "Invalid Entry", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
    }
}