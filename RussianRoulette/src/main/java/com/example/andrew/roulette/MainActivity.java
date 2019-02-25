package com.example.andrew.roulette;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> itemList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configureNextButton();
        configureSpinButton();
        configureDeleteButton();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, itemList);
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent1 = new Intent(MainActivity.this, editItems.class);
            intent1.putExtra("items", itemList);
            startActivityForResult(intent1, 3);
        }

        return super.onOptionsItemSelected(item);
    }

    public void configureNextButton()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(MainActivity.this, AddItem.class);
                startActivityForResult(next, 2);
            }
        });
    }

    public void configureSpinButton()
    {
        FloatingActionButton spin = (FloatingActionButton) findViewById(R.id.startButton);
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent spin = new Intent(MainActivity.this, Spinner.class);
                spin.putExtra("items", itemList);
                startActivityForResult(spin, 4);
            }
        });
    }

    public void configureDeleteButton()
    {
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.mapButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList.clear();
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, itemList);
                ListView listView = (ListView) findViewById(R.id.list);

                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2)
        {
            String message = data.getStringExtra("newItem");
            itemList.add(message);
        }
        else if(requestCode == 3)
        {
            itemList = data.getStringArrayListExtra("editedList");
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, itemList);
            ListView listView = (ListView) findViewById(R.id.list);

            listView.setAdapter(adapter);
        }
        else if(requestCode == 4)
        {
            itemList = data.getStringArrayListExtra("editFromSpin");
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, itemList);
            ListView listView = (ListView) findViewById(R.id.list);

            listView.setAdapter(adapter);
        }
    }
}
