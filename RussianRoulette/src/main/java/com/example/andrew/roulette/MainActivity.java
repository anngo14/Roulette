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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    RouletteList roulette = new RouletteList();
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Saved Roulettes");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFileNames();
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, names);
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RouletteList temp = new RouletteList();
                ArrayList<String> items = loadList(names.get(i));
                temp.setListName(names.get(i));
                temp.setItemList(items);

                Intent main = new Intent(MainActivity.this, editItems2.class);
                main.putExtra("loadFromFavorite", temp);
                startActivity(main);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                showDeleteBox(names.get(i), i);
                return true;
            }
        });
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

        if( id == R.id.action_add){
            Intent addList = new Intent(MainActivity.this, editItems2.class);
            startActivity(addList);
        }

        return super.onOptionsItemSelected(item);
    }
    public void saveRouletteList()
    {
        FileOutputStream output = null;
        FileOutputStream output2 = null;
        String fileName = roulette.getListName();
        final String fileNameList = "roulette_list_names";

        String content = fileName + "@@";
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
    public void getFileNames()
    {
        final String fileNameList = "roulette_list_names";
        FileInputStream input = null;
        ArrayList<String> tempList = new ArrayList<String>();
        try {
            input = openFileInput(fileNameList);
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader buffer = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String text;

            while((text = buffer.readLine()) != null) {
                builder.append(text);
            }
            String[] files = builder.toString().split("@@");

            if(files[0].compareTo("") == 0)
            {
                return;
            }
            for(String s: files)
            {
                tempList.add(s);
            }

            for(String s: tempList)
            {
                if(!names.contains(s))
                {
                    names.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> loadList(String fileName)
    {
        ArrayList<String> itemList = new ArrayList<String>();

        FileInputStream input = null;
        ArrayList<String> tempList = new ArrayList<String>();
        try {
            input = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader buffer = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String text;

            while((text = buffer.readLine()) != null) {
                builder.append(text);
            }
            String[] files = builder.toString().split("@!!!@");
            for(String s: files)
            {
                tempList.add(s);
            }

            for(String s: tempList)
            {
                if(!itemList.contains(s))
                {
                    itemList.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemList;
    }
    public void showDeleteBox(String old, final int index)
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Delete");
        dialog.setContentView(R.layout.delete_box);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Do you wish to Delete list?");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        final TextView txtold = (TextView) dialog.findViewById(R.id.txtold);
        txtold.setText(old);
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile(names.get(index));
                names.remove(index);
                updateFileNames();
                adapter.notifyDataSetChanged();
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
    public void updateFileNames()
    {
        FileOutputStream output = null;
        final String fileName = "roulette_list_names";
        String content = "";

        for(String s: names)
        {
            content += s + " ";
        }
        try {
            output = openFileOutput(fileName, Context.MODE_PRIVATE);
            output.write(content.getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + fileName, Toast.LENGTH_LONG).show();
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
        }
    }
}
