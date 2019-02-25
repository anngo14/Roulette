package com.example.andrew.roulette;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity {

    String line;

    EditText userInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        userInput = (EditText) findViewById(R.id.editText);

        configureAddButton();
    }

    public void configureAddButton()
    {
        Button addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line = userInput.getText().toString();
                Intent main = new Intent();
                main.putExtra("newItem", line);
                setResult(2, main);
                finish();
            }
        });
    }
}
