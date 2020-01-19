package com.example.map_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class CircleTest extends AppCompatActivity {
    boolean run=false;
    Paint P=new Paint();
    Button btn_Roll;
    LinearLayout Circlelayout;
    Float angle=0f,radius=500f,speed=10f;
    int items[]={1,1,1,1};//先預設機率完全一致,所以會是90度的角
    int sum;
    int Colors[][]=new int[4][3];
    BigDecimal bigDecimal;//用來計算精確小數
    Random rnd =new Random();
    Thread thread;
    RectF rectF=new RectF(0,0,radius*2,radius*2);;
    DrawView circleView;
    float swapAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_test);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.circle);
        actionBar.setLogo(R.drawable.blueprint);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        init();
    }
    public class DrawView extends View {
        public DrawView(Context context) {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            P.setColor(Color.WHITE);
            canvas.drawRect(0, 0, radius*2, radius*2, P);// 正方形
            float swapPoint=angle;
            Log.d("swapStart",String.valueOf(swapPoint));
            for(int i=0;i<items.length;i++){
                Log.d("items",String.valueOf(i));
                P.setColor(Color.argb(127,Colors[i][0],Colors[i][1],Colors[i][2]));
                float f=swapAngle*items[i];
                float TextRadians=(float)Math.toRadians((swapPoint+f/2));
                canvas.drawArc(rectF, swapPoint, f, true, P);
                P.setColor(Color.RED);
                canvas.drawText("這是第"+(i+1) +"個數,機率是"+items[i]*100/sum+"%",
                        radius+(float)(radius*Math.cos(TextRadians)),radius+(float)(radius*Math.sin(TextRadians)),P);
                Log.d("position:",String.valueOf(radius+(float)(radius*Math.cos(TextRadians))+"/"+String.valueOf(radius+(float)(radius*Math.sin(TextRadians)))));
                swapPoint+=f;
                swapPoint%=360;
                Log.d("swapPoint",String.valueOf(swapPoint));
                }
            }
        }
    public void init(){
            angle=0f;
            P.setTextSize(50.0f);
            btn_Roll=(Button)findViewById(R.id.btn_Roll);
            btn_Roll.setOnClickListener(listener);
            Circlelayout=(LinearLayout)findViewById(R.id.CircleLayout);
            circleView =new DrawView(this);
            circleView.setMinimumHeight(2000);
            circleView.setMinimumWidth(2000);
            Circlelayout.addView(circleView);
            //隨機設定4個常數
            for(int i=0;i<items.length;i++){
                String s="";
                items[i]=3+rnd.nextInt(4);
                sum+=items[i];
                Log.d("sum","SumNow:"+String.valueOf(sum));
                for (int j=0;j<Colors[i].length;j++){
                Colors[i][j]=rnd.nextInt(256);
                s+=String.valueOf(Colors[i][j])+"\t"; }
                Log.d("color",s); }
            bigDecimal=new BigDecimal((float)360/sum);
            //將平均角度四捨五入計算到(10)個位數(注意,包含整數)
            swapAngle=bigDecimal.setScale(10, RoundingMode.HALF_UP).floatValue();
            Log.d("bigDecimal",String.valueOf(bigDecimal));
            Log.d("swapAngle",String.valueOf(swapAngle));
            circleView.invalidate();
            }
    View.OnClickListener listener=new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             switch(v.getId()){
                 case R.id.btn_Roll:
                     if(run!=true){
                         run=true;
                         btn_Roll.setText("stop");
                         RollThread();
                     }else{
                         run=false;
                         btn_Roll.setText("Roll");
                     }
                     break;
             }
                    }
            } ;
    private void RollThread(){
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(run==true){
                    angle+=speed;
                    angle%=360;
                    Log.d("angle",String.valueOf(angle));
                    circleView.invalidate();
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally { }
                }
            }
        });
        thread.start();
     }
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