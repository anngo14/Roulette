package com.example.andrew.roulette;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.graphics.Typeface.BOLD;

public class Spinner2 extends AppCompatActivity {

    private TextView mTextMessage;
    RouletteList roulette = new RouletteList();
    PieChart pieChart;
    List<Integer> colors = new ArrayList<Integer>();
    private int[] percent;
    private int degree = 0, degreeOld = 0;
    private float HALF_SECTOR = 360f / roulette.getItemList().size() / 2f;
    int backButtonCount = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spinner_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent edit = new Intent(Spinner2.this, editItems2.class);
            edit.putExtra("items", roulette);
            setResult(7, edit);
            finish();
        }
        if( id == R.id.action_home) {
            Intent main = new Intent();
            main.putExtra("editFromSpin", roulette);
            setResult(4, main);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextMessage = (TextView) findViewById(R.id.message);
        //Enables the back button in the toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        configureSpinButton();

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
        pieChart.setTouchEnabled(false);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        addDataSet();
    }
    private void addDataSet( )
    {
        final int[] MY_COLORS = {
                Color.parseColor("#ffb4be"),
                Color.parseColor("#ffc2aa"),
                Color.parseColor("#ffe498"),
                Color.parseColor("#daffa3"),
                Color.parseColor("#a6ffb3"),
                Color.parseColor("#9dffe7"),
                Color.parseColor("#a7f3ff"),
                Color.parseColor("#a7beff"),
                Color.parseColor("#c3baff"),
                Color.parseColor("#f5ccff"),
                Color.parseColor("#c9c6c4"),
                Color.parseColor("#f0eaf2")
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
        pieDataSet.setSliceSpace(0);
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
    public void configureSpinButton(){
        Button sp = (Button) findViewById(R.id.spinBtn);
        sp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(roulette.getItemList().size() == 0)
                {
                    TextView out = (TextView) findViewById(R.id.output);
                    out.setText("ERROR! No items in Roulette");
                    return;
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
                        out.setText("");
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
            }
        });
    }
}
