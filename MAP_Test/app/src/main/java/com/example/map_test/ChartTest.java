package com.example.map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChartTest extends AppCompatActivity {
    DrawView ChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);
        ChartView=new DrawView(this);

    }
}
