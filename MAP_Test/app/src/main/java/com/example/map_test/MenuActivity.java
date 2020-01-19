package com.example.map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MenuActivity extends AppCompatActivity {
    private Button btn_map,btn_circle,btn_Puzzle,btn_scratch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btn_map=(Button)findViewById(R.id.btn_map);
        btn_circle=(Button)findViewById(R.id.btn_circle);
        btn_Puzzle=(Button)findViewById(R.id.btn_Puzzle);
        btn_map.setOnClickListener(jumpListener);
        btn_circle.setOnClickListener(jumpListener);
        btn_Puzzle.setOnClickListener(jumpListener);
        btn_scratch=(Button)findViewById(R.id.btn_scratch);
        btn_scratch.setOnClickListener(jumpListener);

    }

    View.OnClickListener jumpListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent();
            switch (v.getId()){
                case R.id.btn_map:
                    i.setClass(getApplicationContext(),RandomMap1.class);
                    break;
                case R.id.btn_circle:
                    i.setClass(getApplicationContext(),CircleTest.class);
                    break;
                case R.id.btn_Puzzle:
                    i.setClass(getApplicationContext(),Puzzle.class);
                    break;
                case R.id.btn_scratch:
                    i.setClass(getApplicationContext(),Scratch.class);
                    break;
            }
            startActivity(i);

        }
    };
}
