package com.example.andrew.roulette;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class editItems2 extends AppCompatActivity {
    RouletteList rouletteList = new RouletteList();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> listTemp = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouletteList fav = getIntent().getParcelableExtra("loadFromFavorite");
        if(fav != null){
            rouletteList = fav;
        }
        rouletteList.getItemList().add("Add Item " + '\uFF0B');
        setContentView(R.layout.activity_edit_items2);
        setTitle(rouletteList.getListName());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        EditText titleTxt = (EditText) findViewById(R.id.rouletteTitleTxt);
        titleTxt.setText(rouletteList.getListName());
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, rouletteList.getItemList());
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Adds new item when Add Item is clicked and keeps Add Item as the last element
                if(i == rouletteList.getItemList().size() - 1){
                    int index = rouletteList.getItemList().size();
                    //Adds a default item name in sequential order
                    rouletteList.getItemList().add(i, "Item " + index);
                    adapter.notifyDataSetChanged();
                }
                else{
                    showInputBox(rouletteList.getItemList().get(i), i);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                if(i != rouletteList.getItemList().size() - 1){
                    showDeleteBox(rouletteList.getItemList().get(i), i);
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_list) {
            showBox();
        }
        if(id == R.id.start_list) {
            Intent spin = new Intent(editItems2.this, Spinner2.class);
            rouletteList.getItemList().remove(rouletteList.getItemList().size()-1);
            //Blocks spin activity if rouletteList has no items
            if(rouletteList.getItemList().size() == 0){
                Toast.makeText(this, "No Items in this Roulette! Please Try Again", Toast.LENGTH_LONG).show();
                rouletteList.getItemList().add("Add Item " + '\uFF0B');
                return true;
            }
            spin.putExtra("items", rouletteList);
            startActivityForResult(spin, 7);
        }

        return super.onOptionsItemSelected(item);
    }
    public void showInputBox(String old, final int index){
        final Dialog dialog = new Dialog(editItems2.this);
        dialog.setTitle("Input Box");
        dialog.setContentView(R.layout.input_box);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Edit Item");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        final EditText editText = (EditText) dialog.findViewById(R.id.txtinput);
        editText.setText(old);
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        Button cnbtn = (Button)dialog.findViewById(R.id.btcancel);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().matches("^\\s*$")){
                    Toast.makeText(editItems2.this, "Invalid Entry! Try Again!", Toast.LENGTH_LONG).show();
                    return;
                }
                rouletteList.getItemList().set(index, editText.getText().toString());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void showDeleteBox(String old, final int index)
    {
        final Dialog dialog = new Dialog(editItems2.this);
        dialog.setTitle("Delete");
        dialog.setContentView(R.layout.delete_box);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Do you wish to Delete Item?");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        final TextView txtold = (TextView) dialog.findViewById(R.id.txtold);
        txtold.setText(old);
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> tmp = rouletteList.getItemList();
                tmp.remove(index);
                rouletteList.setItemList(tmp);
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
    public void showBox()
    {
        final Dialog dialog = new Dialog(editItems2.this);
        dialog.setTitle("Save Roulette");
        dialog.setContentView(R.layout.title_box);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Do you Want to Save this Roulette?");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        final TextView editText = (TextView) dialog.findViewById(R.id.txttitle);
        EditText titleTxt = (EditText) findViewById(R.id.rouletteTitleTxt);
        editText.setText(titleTxt.getText().toString());
        String title = titleTxt.getText().toString();
        //Roulette Title validation
        if(title.contains("/")){
            Toast.makeText(editItems2.this, "Invalid File Name! Try Again!", Toast.LENGTH_LONG).show();
            return;
        }
        if(title.compareTo("") == 0)
        {
            Toast.makeText(editItems2.this, "Invalid File Name! Try Again!", Toast.LENGTH_LONG).show();
            return;
        }
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rouletteList.setListName(title);
                setTitle(rouletteList.getListName());
                rouletteList.getItemList().remove(rouletteList.getItemList().size()-1);
                saveRouletteList();
                rouletteList.getItemList().add("Add Item " + '\uFF0B');
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
    public void saveRouletteList()
    {
        FileOutputStream output = null;
        FileOutputStream output2 = null;
        String fileName = rouletteList.getListName();
        final String fileNameList = "roulette_list_names";

        String content = fileName + "@@";
        try {
            output = openFileOutput(fileName, Context.MODE_PRIVATE);
            output2 = openFileOutput(fileNameList, Context.MODE_APPEND);
            System.out.println(rouletteList.toString());
            output.write(rouletteList.toString().getBytes());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 7)
        {
            rouletteList = data.getParcelableExtra("items");
            rouletteList.getItemList().add("Add Item " + '\uFF0B');
            adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, rouletteList.getItemList());
            ListView listView = (ListView) findViewById(R.id.list);

            listView.setAdapter(adapter);
        }
    }
}
