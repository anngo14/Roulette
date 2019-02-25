package com.example.andrew.roulette;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Random;

import static java.util.Random.*;

public class Spinner extends AppCompatActivity {

    private TextView mTextMessage;
    ArrayList<String> list = new ArrayList<String>();
    PieChart pieChart;
    private int[] percent;
    private int degree = 0, degreeOld = 0;
    private float HALF_SECTOR = 360f / list.size() / 2f;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent main = new Intent();
                        main.putExtra("editFromSpin", list);
                        setResult(4, main);
                        finish();
                        break;
                    case R.id.spin:
                        degreeOld = degree % 360;
                        degree = (int) (Math.random() * 360) + 1080;
                        RotateAnimation rotate = new RotateAnimation(degreeOld, degree, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(3000);
                        rotate.setFillAfter(true);
                        rotate.setInterpolator(new DecelerateInterpolator());
                        rotate.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                TextView out = (TextView) findViewById(R.id.output);
                                out.setText("SPINNING!");
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                TextView out = (TextView) findViewById(R.id.output);
                                out.setText(randomize(360 - (degree % 360)));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        pieChart.startAnimation(rotate);
                        break;
                    case R.id.editData:
                        Intent edit = new Intent(Spinner.this, editItems.class);
                        edit.putExtra("items", list);
                        startActivityForResult(edit, 5);
                        break;
                }
                return false;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        setTitle("Roulette");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        list = getIntent().getStringArrayListExtra("items");
        pieChart = (PieChart) findViewById(R.id.pie);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setTransparentCircleAlpha(0);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        addDataSet();
    }

    private void addDataSet( )
    {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        for(int i = 0; i < list.size(); i++)
        {
            yEntry.add(new PieEntry(list.size()/100.0f * 100f, list.get(i)));
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "Percent");
        pieDataSet.setSliceSpace(5);
        pieDataSet.setDrawValues(false);
        pieDataSet.setColor(Color.rgb(245,245,245));
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(26);
        pieChart.invalidate();
    }

    public String randomize(int degrees)
    {
        System.out.println("" + degrees);
        if(degrees + 270 >= 360)
        {
            degrees -= 360;
        }
        int index = pieChart.getIndexForAngle(degrees + 270);
        return list.get(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 5)
        {
            list = data.getStringArrayListExtra("editedList");
            addDataSet();
        }
    }

}