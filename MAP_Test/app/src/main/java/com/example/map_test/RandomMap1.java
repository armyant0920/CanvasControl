package com.example.map_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class RandomMap1 extends AppCompatActivity {
    int WALL,FLOOR,BLOCK;
    Random rnd=new Random();
    Button btn_map,btn_move;
    EditText C_Min,C_Max,Col_Min,Col_Max,Row_Min,Row_Max;
    EditText editTexts[];

    int colors[];
    int mapSet[][];
    int X,Y,Min_X,Min_Y,Max_X,Max_Y;//用來存row&col的數量,最小值及最大值
    int CN,CN_Min=2,CN_Max=10;//用來存要產生幾個顏色,最少幾個最大幾個,後續再用變數控制上下限
    int CS=100;//設定格子大小
    DrawView mapView;
    LinearLayout mapLayout;
    Paint p = new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(R.string.random1);
        actionBar.setLogo(R.drawable.logo_design);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        init();
    }

    private void init(){
        btn_map=(Button)findViewById(R.id.btn_map);
        btn_move=(Button) findViewById(R.id.move);
        btn_map.setOnClickListener(btnListener);
        btn_move.setOnClickListener(btnListener);
        mapLayout = (LinearLayout) findViewById(R.id.Map);
        //編輯欄位設定
        C_Min=(EditText) findViewById(R.id.colorMin);
        C_Max=(EditText) findViewById(R.id.colorMax);
        Col_Min=(EditText) findViewById(R.id.colMin);
        Col_Max=(EditText) findViewById(R.id.colMax);
        Row_Max=(EditText) findViewById(R.id.rowMax);
        Row_Min=(EditText) findViewById(R.id.rowMin);

        editTexts=new EditText[]{C_Min,C_Max,Col_Min,Col_Max,Row_Min,Row_Max};

        mapView=new RandomMap1.DrawView(RandomMap1.this);
        //先隨便設寬高 確定塞得下就好 有特殊需求可以自己配合
        mapView.setAlpha(0.5f);

        mapView.setMinimumHeight(1000);//設定最小高度
        mapView.setMinimumWidth(1000);//設定最小寬度
        mapLayout.addView(mapView);

        //通知view组件重绘
        mapView.setBackgroundColor(Color.BLACK);
        mapView.invalidate();

    }

    public class DrawView extends View {

        public DrawView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mapSet!=null) {

                for (int i = 0; i <= mapSet.length - 1; i++) {
                    for (int j = 0; j <= mapSet[i].length - 1; j++) {
                    p.setColor(colors[mapSet[i][j]]);
//                        switch (mapSet[i][j]){
//                            case 0:
//                                p.setColor(FLOOR);
//                                break;
//                            case 1:
//                                p.setColor(WALL);
//                                break;
//                            case 2:
//                                p.setColor(BLOCK);
//                                break;
//                        }
                        canvas.drawRect(i * CS, j * CS, (i + 1) * CS, (j + 1) * CS, p);// 正方形

//                        if (mapSet[i][j] == 0) {
//                            p.setColor(FLOOR);
//
//                        } else {
//                            p.setColor(WALL);
//                        }
//                        canvas.drawRect(i * CS, j * CS, (i + 1) * CS, (j + 1) * CS, p);// 正方形
                    }
                }
            }

        }
    }

    private View.OnClickListener btnListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.btn_map:
                    boolean CheckOK=true;
                    for(EditText et:editTexts){
                        Log.d("et",et.getText().toString());
                        if(et.getText().toString().equals("")){
                            et.setText("1");
                           //CheckOK=false;
                           Toast.makeText(v.getContext(),"空白自動設為1",Toast.LENGTH_SHORT).show();
                          // break;
                        }

                    }

                    if(CheckOK==true) {

                        int min=1,max=1;
                        min=Integer.parseInt(Col_Min.getText().toString());
                        max=Integer.parseInt(Col_Max.getText().toString());
                        if(max<min) {
                            max = min;
                        }
                        X =min+(rnd.nextInt(max-min+1));
                        min=Integer.parseInt(Row_Min.getText().toString());
                        max=Integer.parseInt(Row_Max.getText().toString());
                        if(max<min) {
                            max = min;
                        }
                        Y = min+(rnd.nextInt(max-min+1));
                        min=Integer.parseInt(C_Min.getText().toString());
                        max=Integer.parseInt(C_Max.getText().toString());
                        if(max<min) {
                            max = min;
                        }
                        CN =  min+(rnd.nextInt(max-min+1));
                        Log.d("X=", String.valueOf(X));
                        Log.d("Y=", String.valueOf(Y));
                        Log.d("CN=", String.valueOf(CN));
//                    WALL= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                    FLOOR=Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                    BLOCK=Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                    Log.d("WALL=",String.valueOf(WALL));
//                    Log.d("FLOOR=",String.valueOf(FLOOR));
                        mapSet = new int[X][Y];


                        for (int i = 0; i <= mapSet.length - 1; i++) {
                            for (int j = 0; j <= mapSet[i].length - 1; j++) {
                                mapSet[i][j] = rnd.nextInt(1 + CN);
                                Log.d("mapValue:", "i[" + i + "]" + "j[" + j + "]:" + String.valueOf(mapSet[i][j]));

                            }
                        }
                        colors=new int[CS];
                        for(int k=0;k<=CN;k++){
                            colors[k]=Color.argb(255,rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));}


                        mapView.invalidate();
                    }else{
                        Toast.makeText(v.getContext(),"不能有空白",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.move:
                    Intent intent=new Intent(RandomMap1.this,RandomMap2.class);
                    startActivity(intent);
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        switch (item.getItemId()){
            case R.id.menu1:
                Intent i=new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
