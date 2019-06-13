package com.example.andrew.roulette;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RouletteList roulette = new RouletteList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configureNextButton();
        configureSpinButton();
        configureDeleteButton();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, roulette.getItemList());
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
            intent1.putExtra("items", roulette);
            startActivityForResult(intent1, 3);
        }
        if( id == R.id.action_save)
        {
            showBox();
        }
        if( id == R.id.action_favorites)
        {
            Intent fav = new Intent(this, Favorites.class);
            startActivityForResult(fav, 6);
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
                spin.putExtra("items", roulette);
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
                ArrayList<String> temp = roulette.getItemList();
                temp.clear();
                setTitle("Roulette");
                roulette.setItemList(temp);
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, roulette.getItemList());
                ListView listView = (ListView) findViewById(R.id.list);

                listView.setAdapter(adapter);
            }
        });
    }

    public void showBox()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Title");
        dialog.setContentView(R.layout.title_box);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Title: ");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        final EditText editText = (EditText) dialog.findViewById(R.id.txttitle);
        editText.setText(roulette.getListName());
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText.getText().toString();

                if(title.compareTo("") == 0)
                {
                    dialog.dismiss();
                }
                else
                {
                    roulette.setListName(title);
                    saveRouletteList();
                }

                dialog.dismiss();
            }
        });
        Button bt2 = (Button)dialog.findViewById(R.id.btno);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2)
        {
            String message = data.getStringExtra("newItem");
            ArrayList<String> temp = roulette.getItemList();
            temp.add(message);
            roulette.setItemList(temp);
            if(message.compareTo("") == 0)
            {
                temp = roulette.getItemList();
                temp.remove(message);
                roulette.setItemList(temp);
            }

        }
        else if(requestCode == 3)
        {
            roulette = data.getParcelableExtra("editedList");
            if(roulette.getItemList().contains(""))
            {
                ArrayList<String> temp = roulette.getItemList();
                temp.remove("");
                roulette.setItemList(temp);
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, roulette.getItemList());
            ListView listView = (ListView) findViewById(R.id.list);

            listView.setAdapter(adapter);
        }
        else if(requestCode == 4)
        {
            roulette = data.getParcelableExtra("editFromSpin");
            if(roulette.getItemList().contains(""))
            {
                ArrayList<String> temp = roulette.getItemList();
                temp.remove("");
                roulette.setItemList(temp);
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, roulette.getItemList());
            ListView listView = (ListView) findViewById(R.id.list);

            listView.setAdapter(adapter);
        }
        else if(requestCode == 6)
        {
            roulette = data.getParcelableExtra("loadFromFavorite");
            setTitle(roulette.getListName());
            if(roulette.getItemList().contains(""))
            {
                ArrayList<String> temp = roulette.getItemList();
                temp.remove("");
                roulette.setItemList(temp);
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, roulette.getItemList());
            ListView listView = (ListView) findViewById(R.id.list);

            listView.setAdapter(adapter);
        }
    }

    public void saveRouletteList()
    {
        FileOutputStream output = null;
        FileOutputStream output2 = null;
        String fileName = roulette.getListName();
        final String fileNameList = "roulette_list_names";

        String content = fileName+" ";
        try {
            output = openFileOutput(fileName, Context.MODE_PRIVATE);
            output2 = openFileOutput(fileNameList, Context.MODE_APPEND);
            output.write(roulette.toString().getBytes());
            output2.write(content.getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + fileNameList, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(output2 != null)
            {
                try {
                    output2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
