package com.shourya.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_category extends AppCompatActivity {

    Button enter;
    EditText cat;

    //to ues DatabaseAdapter class
    DatabaseAdapter da=new DatabaseAdapter(add_category.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        setTitle("ADD CATEGORY");

        //opening the database
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        enter=findViewById(R.id.enter);
        cat=findViewById(R.id.etCat);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=cat.getText().toString().toUpperCase().trim();
                Cursor check=null;
                try {
                    check=da.isExistCategory(category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(category.equals("")){
                    Toast.makeText(add_category.this,"Please enter valid category",Toast.LENGTH_LONG).show();
                }
                else if(check.moveToFirst())
                {
                    Toast.makeText(add_category.this,"Category already exists",Toast.LENGTH_LONG).show();

                }
                else{
                    long n = 0;
                    try {
                        n = da.insertDataCategory(category);
                        if (n > 0)
                            Toast.makeText(add_category.this, ""+category+" added", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(add_category.this, "Invalid Entry", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();

                }
            }
        });
    }
}