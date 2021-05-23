package com.shourya.expensemanager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Random;

public class drawPieChart {

    com.github.mikephil.charting.charts.PieChart pieChart;
    ArrayList<PieEntry> pieEntry;
    ArrayList<Integer> color;
    Context context;

    public drawPieChart(com.github.mikephil.charting.charts.PieChart pieChart, ArrayList<PieEntry> pieEntry, ArrayList<Integer> color, String startdate, String enddate, Context context) throws Exception {
        this.pieChart = pieChart;
        this.pieEntry=pieEntry;
        this.color=color;
        this.context=context;

        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(30);
        pieChart.setHoleColor(Color.rgb(247, 213, 243));
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("EXPENSES");
        pieChart.setCenterTextSize(12);

        addDataSet(startdate,enddate);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe=(PieEntry)e;
                Toast.makeText(context,pe.getLabel()+":"+e.getY(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void addDataSet(String startdate,String enddate) {
        DatabaseAdapter da=new DatabaseAdapter(context);
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cursor c=da.getAllDataCategories();
        int i=0;
        while(c.moveToNext())
        {
            String category=c.getString(1);
            float val=0.0f;
            try {
                val=(float)da.customDateSum(startdate,enddate,category);
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
        pds.setValueTextSize(12);
        pds.setColors(color);

        Legend legend=pieChart.getLegend();
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setTextSize(10);
        legend.setFormSize(10);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(5);
        //legend.;
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        PieData pd=new PieData(pds);
        pieChart.setData(pd);
        pieChart.animateY(1000);
        pieChart.invalidate();
        pieChart.getDescription().setText("CATEGORY WISE EXPENSE");
        pieChart.getDescription().setTextSize(30);
        pieChart.getDescription().setPosition(870,1350);
    }
}
