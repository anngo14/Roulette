package com.example.andrew.roulette;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Favorites extends AppCompatActivity {
    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        configureBackButton();
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

                Intent main = new Intent();
                main.putExtra("loadFromFavorite", temp);
                setResult(6, main);
                finish();
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

    public void showDeleteBox(String old, final int index)
    {
        final Dialog dialog = new Dialog(Favorites.this);
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

    public void configureBackButton()
    {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouletteList temp = new RouletteList();
                Intent main = new Intent();
                main.putExtra("loadFromFavorite", temp);
                setResult(6, main);
                finish();
            }
        });
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
            String[] files = builder.toString().split("\\\\");

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
            String[] files = builder.toString().split("\\s+");

            if(files[0].compareTo("") == 0)
            {
                return itemList;
            }
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
