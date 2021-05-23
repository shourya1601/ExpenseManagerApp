package com.shourya.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class manage_categories extends AppCompatActivity{

    //for recycler view
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<RVListItemCategories> listItems;

    //for pie chart
    PieChart pieChart;
    ArrayList<PieEntry> pieEntry=new ArrayList<>();
    ArrayList<Integer> color=new ArrayList<>();

    //to access database adapter
    DatabaseAdapter da=new DatabaseAdapter(manage_categories.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        setTitle("MANAGE CATEGORIES");

        //opening database adapter
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //linking views
        pieChart=findViewById(R.id.pie);
        recyclerView=findViewById(R.id.rv);

        //for pie chart
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("EXPENSES");
        pieChart.setCenterTextSize(12);
        pieChart.setHoleColor(Color.rgb(247, 213, 243));

        addDataSet();

        //on clicking on a pie entry
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe=(PieEntry)e;
                Toast.makeText(manage_categories.this,pe.getLabel()+":"+e.getY(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //for adding data in recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        listItems=new ArrayList<>();

        //retrieving data from database
        Cursor c=da.getAllDataCategories();
        while(c.moveToNext())
        {
            String id=""+c.getInt(0);
            String category=c.getString(1);
            RVListItemCategories li= null;
            try {
                li = new RVListItemCategories(id,category,"Rs. "+da.categorySum(category));
            } catch (Exception e) {
                e.printStackTrace();
            }
            listItems.add(li);
        }
        c.close();

        adapter=new RVAdapterCategories(listItems,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=new Intent(manage_categories.this, add_category.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void addDataSet() {
        //retrieving data from database
        Cursor c=da.getAllDataCategories();
        int i=0;
        while(c.moveToNext())
        {
            String category=c.getString(1);
            float val=0.0f;
            try {
                val=(float)da.categorySum(category);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(val!=0)
            {
                Random rnd = new Random();
                int clr = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                color.add(clr);
                pieEntry.add(new PieEntry(val,category));
            }
            i++;
        }
        c.close();

        PieDataSet pds=new PieDataSet(pieEntry,"Category details");
        pds.setSliceSpace(2);
        pds.setValueTextSize(15);
        pds.setColors(color);

        Legend legend=pieChart.getLegend();
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setTextSize(10);
        legend.setFormSize(10);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(5);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        PieData pd=new PieData(pds);
        pieChart.setData(pd);
        pieChart.animateY(1000);
        pieChart.getDescription().setText("Category Wise Expenses");
        pieChart.getDescription().setTextSize(16);
        pieChart.invalidate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

}