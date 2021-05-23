package com.shourya.expensemanager;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class drawBarChart {

    BarChart barChart;
    ArrayList<BarEntry> barEntry;
    ArrayList<String> mydate;
    Calendar cal;
    Context context;


    public drawBarChart(BarChart barChart, ArrayList<BarEntry> barEntry, ArrayList<String> mydate, Calendar cal, Context context) {
        this.barChart=barChart;
        this.barEntry=barEntry;
        this.mydate=mydate;
        this.cal=cal;
        this.context=context;

        DatabaseAdapter da=new DatabaseAdapter(context);
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        for(int i=0;i<30;++i)
        {
            String startdate = ""+cal.getTimeInMillis();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(cal.getTimeInMillis()));
            mydate.add(date);
            cal.add(Calendar.DAY_OF_MONTH,1);
            String enddate=""+cal.getTimeInMillis();
            float sum= 0;
            try {
                sum= (float) da.customDateSum(startdate,enddate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            barEntry.add(new BarEntry(i,sum));
            cal.add(Calendar.DAY_OF_MONTH,-2);
        }
        BarDataSet bds=new BarDataSet(barEntry,"Dates");
        BarData bd=new BarData(bds);
        bds.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(bd);
        barChart.setVisibleXRangeMaximum(7);
        barChart.setFitBars(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.enableScroll();
        barChart.setBackgroundColor(Color.rgb(255,204,255));
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setText("Last 30 days analysis");
        barChart.animateY(1000);
        barChart.invalidate();
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mydate));//add labels
        barChart.getXAxis().setLabelRotationAngle(90);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }
}
