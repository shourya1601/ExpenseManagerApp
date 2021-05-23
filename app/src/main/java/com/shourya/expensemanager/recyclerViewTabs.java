package com.shourya.expensemanager;

import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class recyclerViewTabs {

    TextView tvsum;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<RVListitem> listItems;
    Context context;

    public recyclerViewTabs(TextView tvsum, RecyclerView recyclerView, RecyclerView.Adapter adapter, List<RVListitem> listItems, String startdate, String enddate, Context context) {
        this.tvsum=tvsum;
        this.recyclerView=recyclerView;
        this.adapter=adapter;
        this.listItems=listItems;
        this.context=context;

        DatabaseAdapter da=new DatabaseAdapter(context);
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        double sum=0;
        try {
            sum=da.customDateSum(""+startdate,""+enddate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvsum.setText("Sum: "+sum);

        //setting data in recycler view from dastabase
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        listItems=new ArrayList<>();

        //to retrieve data between selected dates
        Cursor c= null;
        try {
            c = da.customDateData(""+startdate,""+enddate);
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

        adapter=new RVAdapter(listItems,context);
        recyclerView.setAdapter(adapter);

    }
}
