package com.example.map_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.Random;

public class RandomMap2 extends AppCompatActivity {
    int WALL,FLOOR,BLOCK;

    Random rnd=new Random();
    Button btn_submit;
    int mapSet[][]=new int[10][4];
    int X=10,Y=4;
    int CS=100;//設定格子大小
    int BG_Colors[]={0,0,0};

    LinearLayout layout1,layout2,layout3,layout4,BG_Change;
    LinearLayout[] layouts;

    Paint p1 = new Paint();
    Paint p2 = new Paint();
    Paint p3 = new Paint();
    Paint p4 = new Paint();
    Paint paints[];
    BitmapTool bitmapTool;
    Bitmap bitmap,Wall;
    Bitmap TreesA[],Walls[];
    Resources res;
    ImageButton ICON;
    TypedArray TA,TB,TW;//三個Array,分別用來存取3種資源

    RandomMap2.DrawView mapView[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backgrounds);
        bitmapTool=new BitmapTool();
        res=getResources();
        TA=getResources().obtainTypedArray(R.array.Tree_type_b_Pic);
        TW=getResources().obtainTypedArray(R.array.Wall_Pic);

        TreesA=new Bitmap[TA.length()];
        for(int i=0;i<TA.length();i++){
            TreesA[i]=bitmapTool.NewImage(this,CS,TA.getResourceId(i,0));
        }
        Walls=new Bitmap[TW.length()];
        for(int i=0;i<TW.length();i++){
            Walls[i]=bitmapTool.NewImage(this,CS,TW.getResourceId(i,0));
        }

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.random2);
        actionBar.setLogo(R.drawable.map);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        init();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode==RESULT_OK){
            Log.d("result",String.valueOf(requestCode));
            Uri uri = data.getData();
            //寫log
            Log.e("uri", uri.toString());
            //抽象資料的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //由抽象資料接口轉換圖檔路徑為Bitmap
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                bitmap=bitmapTool.NewImage(getApplicationContext(),400,bitmap);
                // 將Bitmap設定到ImageView
                ICON.setImageBitmap(bitmap);

                bitmap=bitmapTool.NewImage(getApplicationContext(),CS,bitmap);
                Log.d("bitmap",String.valueOf(bitmap.getWidth())+"/"+String.valueOf(bitmap.getHeight()));

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        else{Log.d("error","error");Log.d("result",String.valueOf(resultCode));}
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        layout1=(LinearLayout) findViewById(R.id.layout1);
        layout2=(LinearLayout) findViewById(R.id.layout2);
        layout3=(LinearLayout) findViewById(R.id.layout3);
        layout4=(LinearLayout) findViewById(R.id.layout4);
        layouts=new LinearLayout[]{layout1,layout2,layout3,layout4};
        for(LinearLayout layout:layouts){
            layout.setOnLongClickListener(longClickListener);
        }
        ICON=(ImageButton) findViewById(R.id.ICON);
        ICON.setOnLongClickListener(longClickListener);
        /*ICON.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 999);
                return true;//return ture觸發長按事件, return false則觸發點擊
            }
        });*/

        btn_submit=(Button)findViewById(R.id.btn_subimt);
        btn_submit.setOnClickListener(btnListener);

        mapView=new DrawView[layouts.length];
        for (int i=0;i<layouts.length;i++){
            mapView[i]=new RandomMap2.DrawView(RandomMap2.this);
            //先隨便設寬高 確定塞得下就好 有特殊需求可以自己配合
            mapView[i].setAlpha(1.0f);//遮色片效果
            mapView[i].setMinimumHeight(400);//設定最小高度
            mapView[i].setMinimumWidth(1000);//設定最小寬度
            layouts[i].addView(mapView[i]);
        }
        paints=new Paint[]{p1,p2,p3,p4};//多重畫筆是否有利用空間還沒嘗試
        bitmapTool=new BitmapTool();
        // bitmap=bitmapTool.NewImage(this,R.drawable.a_tree1);
    }

    public class DrawView extends View {

        public DrawView(Context context) {
            super(context);}

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for(LinearLayout layout:layouts) {
                for (int i = 0; i < mapSet.length; i++) {
                    for (int j = 0; j < mapSet[i].length; j++) {
                        switch (mapSet[i][j]) {
                            case 0:
                                p1.setColor(FLOOR);
                                canvas.drawRect(i * CS, j * CS, (i + 1) * CS, (j + 1) * CS, p1);// 正方形
                                p1.setColor(Color.argb(255,0,0,0));
                                break;
                            case 1://牆壁

                                canvas.drawBitmap(Wall,i*CS,j*CS,p1);
                                break;
                            case 2:
                                if (bitmap != null) {
                                    canvas.drawBitmap(bitmap,i*CS,j*CS,p1);
                                }
//                                p1.setColor(BLOCK);
//                                canvas.drawRect(i * CS, j * CS, (i + 1) * CS, (j + 1) * CS, p1);// 正方形
                                break;
                            case 3:
                                canvas.drawBitmap(TreesA[rnd.nextInt(TA.length())],i*CS,j*CS,p1);
                                break;
                        }
                    }
                }
            }
        }
    }
    private View.OnLongClickListener longClickListener= new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch(v.getId()){
                case R.id.ICON:
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 999);
                    //return true;//return ture觸發長按事件, return false則觸發點擊
                    break;
                case R.id.layout1:
                    BG_Change=layout1;
                    PickColor();
                    break;
                case R.id.layout2:
                    BG_Change=layout2;
                    PickColor();
                    break;
                case R.id.layout3:
                    BG_Change=layout3;
                    PickColor();
                    break;
                case R.id.layout4:
                    BG_Change=layout4;
                    PickColor();
                    break;
            }
            return true;
        }
    };

    private View.OnClickListener btnListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.btn_subimt:
                    if(bitmap!=null) {
                        FLOOR = Color.argb(23, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                        Wall = Walls[rnd.nextInt(Walls.length)];
                        //WALL= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                        // BLOCK=Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    /*
                    int x=0;
                    x+=rnd.nextInt(TA.length());
                    Log.d("Tree",String.valueOf(x));
                    Tree=bitmapTool.NewImage(v.getContext(),CS,TA.getResourceId(x,0));
                    */
                        for (int i = 0; i <= mapSet.length - 1; i++) {
                            for (int j = 0; j <= mapSet[i].length - 1; j++) {
                                mapSet[i][j] = rnd.nextInt(4);
                                Log.d("mapValue:", "i[" + i + "]" + "j[" + j + "]:" + String.valueOf(mapSet[i][j]));
                            }
                        }
                        for (int k = 0; k < layouts.length; k++) {
                            mapView[k].invalidate();
                        }
                    }
                    else {
                        Toast.makeText(v.getContext(),"必須匯入角色圖片",Toast.LENGTH_SHORT).show();}
                    break;

            }
        }
    };

    private void PickColor(){

                final View Picker= LayoutInflater.from(RandomMap2.this).inflate(R.layout.pick_color,null);
//                LinearLayout linearLayout=(LinearLayout) findViewById(R.id.BackGround_Layout);
//                linearLayout.addView(Picker);
                final EditText TextR=Picker.findViewById(R.id.Text_R);
                final LinearLayout PreView=Picker.findViewById(R.id.PreView);
                final EditText TextG=Picker.findViewById(R.id.Text_G);
                final EditText TextB=Picker.findViewById(R.id.Text_B);
                TextR.setOnClickListener(btnListener);
                TextG.setOnClickListener(btnListener);
                TextB.setOnClickListener(btnListener);
                SeekBar  barR=Picker.findViewById(R.id.seekBarR);
                SeekBar  barG=Picker.findViewById(R.id.seekBarG);
                SeekBar  barB=Picker.findViewById(R.id.seekBarB);
                barR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        TextR.setText(String.valueOf(progress));
                       BG_Colors[0]=progress;
                       PreView.setBackgroundColor(Color.rgb(BG_Colors[0],BG_Colors[1],BG_Colors[2]));

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                barG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    TextG.setText(String.valueOf(progress));
                    BG_Colors[1]=progress;
                    PreView.setBackgroundColor(Color.rgb(BG_Colors[0],BG_Colors[1],BG_Colors[2]));

                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                barB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        TextB.setText(String.valueOf(progress));
                        BG_Colors[2]=progress;
                        PreView.setBackgroundColor(Color.rgb(BG_Colors[0],BG_Colors[1],BG_Colors[2]));

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
//                SeekBar.OnSeekBarChangeListener seekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                        switch(Picker.getId()){
//                            case R.id.seekBarR:
//                                TextR.setText(String.valueOf(getText))
//                                break;
//                            case R.id.seekBarG:
//                                break;
//                            case R.id.seekBarB:
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                    }
//                };

                new AlertDialog.Builder(RandomMap2.this)
                .setTitle("選擇背景顏色")
                .setView(Picker)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText Text_R = (EditText) Picker.findViewById(R.id.Text_R);
                        EditText Text_G = (EditText) Picker.findViewById(R.id.Text_G);
                        EditText Text_B = (EditText) Picker.findViewById(R.id.Text_B);
                        BG_Colors[0]=Integer.parseInt(Text_R.getText().toString());
                        BG_Colors[1]=Integer.parseInt(Text_G.getText().toString());
                        BG_Colors[2]=Integer.parseInt(Text_B.getText().toString());
                        BG_Change.setBackgroundColor(Color.rgb(BG_Colors[0],BG_Colors[1],BG_Colors[2]));
                        for(int i=0;i<BG_Colors.length;i++){
                            BG_Colors[i]=0;
                        }
                    }
                })
                .show();
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
