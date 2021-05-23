package com.shourya.expensemanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.os.IResultReceiver;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;

    //for database
    DatabaseAdapter da=new DatabaseAdapter(MainActivity.this);

    //variable declarations
    TabHost tab;
    ViewFlipper vf;
    VideoView mVideo,mVideo1;
    MediaController mVideoConroller,mVideoConroller1;
    ScrollView scrollView;

    RecyclerView.Adapter adapter;

    //for tab WEEK
    PieChart pieChart;
    ArrayList<PieEntry> pieEntry=new ArrayList<>();
    ArrayList<Integer> color=new ArrayList<>();
    TextView tv1,tvsum1;
    RecyclerView recyclerView1;
    List<RVListitem> listItems1;

    //for tab MONTH
    PieChart pieChart1;
    ArrayList<PieEntry> pieEntry1=new ArrayList<>();
    ArrayList<Integer> color1=new ArrayList<>();
    TextView tv2,tvsum2;
    RecyclerView recyclerView2;
    List<RVListitem> listItems2;

    //for tab YEAR
    PieChart pieChart2;
    ArrayList<PieEntry> pieEntry2=new ArrayList<>();
    ArrayList<Integer> color2=new ArrayList<>();
    TextView tv3,tvsum3;
    RecyclerView recyclerView3;
    List<RVListitem> listItems3;

    //for Bar Chart
    BarChart barChart;
    ArrayList<BarEntry> barEntry=new ArrayList<>();
    ArrayList<String> mydate=new ArrayList<>();
    Calendar cal_graph=Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //opening database
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar cal=Calendar.getInstance();

        //linking views for tab week
        pieChart=findViewById(R.id.pie1);
        tv1=findViewById(R.id.tv1);
        tvsum1=findViewById(R.id.tvsum1);
        recyclerView1=findViewById(R.id.rv1);

        //linking views for tab month
        pieChart1=findViewById(R.id.pie2);
        tv2=findViewById(R.id.tv2);
        tvsum2=findViewById(R.id.tvsum2);
        recyclerView2=findViewById(R.id.rv2);

        //linking views for tab month
        pieChart2=findViewById(R.id.pie3);
        tv3=findViewById(R.id.tv3);
        tvsum3=findViewById(R.id.tvsum3);
        recyclerView3=findViewById(R.id.rv3);

        //for bar Chart
        barChart=findViewById(R.id.bar);

        //linking views
        vf=findViewById(R.id.vf);
        tab=findViewById(R.id.tabHost);
        mVideo=findViewById(R.id.vv);
        mVideo1=findViewById(R.id.vv1);
        scrollView=findViewById(R.id.scrollview);

        //view flipper design
        vf.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
        vf.setFlipInterval(7000);
        vf.startFlipping();

        //tabs setup
        tab.setup();
        TabHost.TabSpec spec1=tab.newTabSpec("Home");
        spec1.setContent(R.id.tabcontent1);
        spec1.setIndicator("HOME");
        tab.addTab(spec1);

        TabHost.TabSpec spec2=tab.newTabSpec("Week");
        spec2.setContent(R.id.tabcontent2);
        spec2.setIndicator("WEEK");
        tab.addTab(spec2);

        TabHost.TabSpec spec3=tab.newTabSpec("Month");
        spec3.setContent(R.id.tabcontent3);
        spec3.setIndicator("MONTH");
        tab.addTab(spec3);

        TabHost.TabSpec spec4=tab.newTabSpec("Year");
        spec4.setContent(R.id.tabcontent4);
        spec4.setIndicator("YEAR");
        tab.addTab(spec4);

        //for video 1
        Uri mVideoUri=Uri.parse("android.resource://"+getPackageName()+"/raw/first");
        mVideo.setVideoURI(mVideoUri);
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mVideoConroller=new MediaController(MainActivity.this);
                        mVideo.setMediaController(mVideoConroller);
                        mVideoConroller.setMediaPlayer(mVideo);
                        mVideoConroller.setAnchorView(mVideo);
                    }
                });
            }
        });
        mVideo.seekTo( 1 );
        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideo.start();
                if(mVideo1.isPlaying())
                    mVideo1.pause();
            }
        });
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.pause();
                mVideo.seekTo(1);
            }
        });


        //for video2
        Uri mVideoUri1=Uri.parse("android.resource://"+getPackageName()+"/raw/second");
        mVideo1.setVideoURI(mVideoUri1);
        mVideo1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mVideoConroller1=new MediaController(MainActivity.this);
                        mVideo1.setMediaController(mVideoConroller1);
                        mVideoConroller1.setMediaPlayer(mVideo1);
                        mVideoConroller1.setAnchorView(mVideo1);
                    }
                });
            }
        });
        mVideo1.seekTo( 1 );
        mVideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideo1.start();
                if(mVideo.isPlaying())
                    mVideo.pause();
            }
        });
        mVideo1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.pause();
                mVideo1.seekTo(1);
            }
        });

        //to hide media player when scrolling activity
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE) {
                    mVideoConroller.hide();
                    mVideoConroller1.hide();
                }
                return false;
            }
        });

        //for income button
        ImageButton income=findViewById(R.id.income);
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,income.class);
                startActivity(intent);
            }
        });

        //for cudtom date range
        ImageButton cdr=findViewById(R.id.cdr);
        cdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,customDateRange.class);
                startActivity(intent);
            }
        });

        //for manage categories button
        ImageButton categories=findViewById(R.id.cat);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,manage_categories.class);
                startActivity(intent);
            }
        });

        //for passbook button
        ImageButton passbook=findViewById(R.id.passbook);
        passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,passbook.class);
                startActivity(intent);
            }
        });

        //for set Reminder button
        ImageButton budget=findViewById(R.id.budget);
        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,budget.class);
                startActivity(intent);
            }
        });

        //for graphs button
        ImageButton help=findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,help.class);
                startActivity(intent);
            }
        });

        //working for tab week
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        String ed=sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH,1);
        String enddate=""+cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH,-7);
        String startdate=""+cal.getTimeInMillis();
        String sd=sdf.format(cal.getTime());

        String text1="  "+sd+"-"+ed+"  ";
        tv1.setText(text1);

        try {
            new drawPieChart(pieChart,pieEntry,color,startdate,enddate,getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new recyclerViewTabs(tvsum1,recyclerView1,adapter,listItems1,startdate,enddate,this);

        //working for tab month
        cal.add(Calendar.DAY_OF_MONTH,6);
        cal.set(Calendar.DAY_OF_MONTH,1);
        String mt=(String) DateFormat.format("MMM",cal.getTime());
        String startdate1=""+cal.getTimeInMillis();
        cal.add(Calendar.MONTH,1);
        String enddate1=""+cal.getTimeInMillis();
        String yr=(String) DateFormat.format("yyyy",cal.getTime());

        String text2="  "+mt+"/"+yr+"  ";
        tv2.setText(text2);

        try {
            new drawPieChart(pieChart1,pieEntry1,color1,startdate1,enddate1,getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new recyclerViewTabs(tvsum2,recyclerView2,adapter,listItems2,startdate1,enddate1,this);

        //working for tab year
        cal.set(Calendar.MONTH,0);
        String startdate2=""+cal.getTimeInMillis();
        String year=(String) DateFormat.format("yyyy",cal.getTime());
        cal.add(Calendar.YEAR,1);
        String enddate2=""+cal.getTimeInMillis();

        String text3="  "+year+"  ";
        tv3.setText(text3);

        try {
            new drawPieChart(pieChart2,pieEntry2,color2,startdate2,enddate2,getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new recyclerViewTabs(tvsum3,recyclerView3,adapter,listItems3,startdate2,enddate2,this);

        //for graph
        new drawBarChart(barChart,barEntry,mydate,cal_graph,this);
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime+2000>System.currentTimeMillis())
        {
            super.onBackPressed();
            finishAffinity();
            return;
        }
        else
        {
            Toast.makeText(MainActivity.this,"Press again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime=System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent i=new Intent(getApplicationContext(),about.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
}