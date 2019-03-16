package com.example.andrew.roulette;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class editItems extends AppCompatActivity {

    RouletteList temp = new RouletteList();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);

        configureSaveButton();
        temp = getIntent().getParcelableExtra("items");
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, temp.getItemList());
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showInputBox(temp.getItemList().get(i), i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                showDeleteBox(temp.getItemList().get(i), i);
                return true;
            }
        });

    }

    public void showInputBox(String old, final int index){
        final Dialog dialog = new Dialog(editItems.this);
        dialog.setTitle("Input Box");
        dialog.setContentView(R.layout.input_box);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Update Item");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        final EditText editText = (EditText) dialog.findViewById(R.id.txtinput);
        editText.setText(old);
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp.getItemList().set(index, editText.getText().toString());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showDeleteBox(String old, final int index)
    {
        final Dialog dialog = new Dialog(editItems.this);
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
                ArrayList<String> tmp = temp.getItemList();
                tmp.remove(index);
                temp.setItemList(tmp);
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
    public void configureSaveButton()
    {
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent();
                main.putExtra("editedList", temp);
                setResult(3, main);
                setResult(5, main);
                finish();
            }
        });
    }

}
