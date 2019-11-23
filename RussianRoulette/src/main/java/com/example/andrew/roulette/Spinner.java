//Outdated Spinner Class
package com.example.andrew.roulette;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.github.mikephil.charting.animation.Easing;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.graphics.Typeface.BOLD;

public class Spinner extends AppCompatActivity {

    private TextView mTextMessage;
    RouletteList roulette = new RouletteList();
    PieChart pieChart;
    List<Integer> colors = new ArrayList<Integer>();
    private int[] percent;
    private int degree = 0, degreeOld = 0;
    private float HALF_SECTOR = 360f / roulette.getItemList().size() / 2f;
    int backButtonCount = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent main = new Intent();
                        main.putExtra("editFromSpin", roulette);
                        setResult(4, main);
                        finish();
                        break;
                    case R.id.spin:
                        if(roulette.getItemList().size() == 0)
                        {
                            TextView out = (TextView) findViewById(R.id.output);
                            out.setText("ERROR! No items in Roulette");
                            break;
                        }
                        degreeOld = degree % 360;
                        degree = (int) (Math.random() * 360) + 1800;
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
                        edit.putExtra("items", roulette);
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
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        roulette = getIntent().getParcelableExtra("items");
        setTitle(roulette.getListName());
        if(roulette.getItemList().contains(""))
        {
            ArrayList<String> temp = roulette.getItemList();
            temp.remove("");
            roulette.setItemList(temp);
        }
        if(roulette.getItemList().size() == 0)
        {
            TextView out = (TextView) findViewById(R.id.error);
            out.setText("EMPTY LIST!");
        }
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
        final int[] MY_COLORS = {
            Color.BLUE,
            Color.CYAN,
            Color.GRAY,
            Color.GREEN,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
        };

        for(int c: MY_COLORS) {
            colors.add(c);
        }
        Collections.shuffle(colors);
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        for(int i = 0; i < roulette.getItemList().size(); i++)
        {
            yEntry.add(new PieEntry(roulette.getItemList().size()/100.0f * 100f, roulette.getItemList().get(i)));
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "Percent");
        pieDataSet.setSliceSpace(5);
        pieDataSet.setDrawValues(false);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextTypeface(Typeface.defaultFromStyle(BOLD));
        pieChart.setEntryLabelTextSize(26);
        pieChart.invalidate();
    }

    public String randomize(int degrees)
    {
        if(degrees + 270 >= 360)
        {
            degrees -= 360;
        }
        int index = pieChart.getIndexForAngle(degrees + 270);
        return roulette.getItemList().get(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 5)
        {
            roulette = data.getParcelableExtra("editedList");
            if(roulette.getItemList().contains(""))
            {
                ArrayList<String> temp = roulette.getItemList();
                temp.remove("");
                roulette.setItemList(temp);
            }
            if(roulette.getItemList().size() == 0)
            {
                TextView out = (TextView) findViewById(R.id.error);
                out.setText("EMPTY LIST!");
            }
            addDataSet();
        }
    }
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
