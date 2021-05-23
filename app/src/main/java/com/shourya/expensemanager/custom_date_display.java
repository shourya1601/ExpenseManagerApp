package com.shourya.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class custom_date_display extends AppCompatActivity {

    //for selected category
    String category,startdate,enddate;
    TextView tvsum;

    //for recycler view
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<RVListitem> listItems;

    //to access DatabaseAdapter
    DatabaseAdapter da=new DatabaseAdapter(custom_date_display.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_date_display);

        setTitle("CUSTOM DATE RANGE");

        tvsum=findViewById(R.id.tvsum);

        Intent i=getIntent();
        startdate=i.getExtras().getString("startdate");
        enddate=i.getExtras().getString("enddate");
        category=i.getExtras().getString("category");

        //opening database adapter
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        double sum=0;
        try {
            if(category.equals("ALL CATEGORIES"))
                sum=da.customDateSum(""+startdate,""+enddate);
            else
                sum=da.customDateSum(""+startdate,""+enddate,category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvsum.setText("  Sum: Rs. "+sum+" ");

        //setting data in recycler view from dastabase
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(custom_date_display.this));

        listItems=new ArrayList<>();

        //to retrieve data between selected dates
        Cursor c= null;
        try {
            if(category.equals("ALL CATEGORIES"))
                c = da.customDateData(""+startdate,""+enddate);
            else
                c=da.customDateData(""+startdate,""+enddate,category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(c.moveToNext())
        {
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(c.getLong(3)));
            RVListitem li=new RVListitem(""+c.getInt(0),""+date,"Rs. "+c.getDouble(1),""+c.getString(2),"Note-"+c.getString(4));
            listItems.add(li);
        }
        c.close();

        adapter=new RVAdapter(listItems,custom_date_display.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

}