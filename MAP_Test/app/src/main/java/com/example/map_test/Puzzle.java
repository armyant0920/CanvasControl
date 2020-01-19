package com.example.map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class Puzzle extends AppCompatActivity {
    //先做成4*4的圖\

    int squareValue[];
    private Button btn_puzzle,btn_save;
    LinearLayout PZ;
    Paint P=new Paint();
    BitmapTool bt;
    Bitmap bitmap,bitmap2;
    ImageButton btn_Img;
    ArrayList<Bitmap>image_squares;
    Puzzle.DrawView PV;
    int GS=200;
    Random rnd=new Random();
    int MaxX=4,MaxY=4;//以後再開放自訂格子數
    boolean play=false;
    String sdCardDir;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        sdCardDir= Environment.getExternalStorageDirectory()+"/";// + "/PuzzleImages/";

        Log.d("Environment2",Environment.getExternalStorageDirectory() + "/PuzzleImages/");
        //sdCardDir="/sdcard/DCIM/Camera/";
        init();

    }

    private void init(){
        btn_puzzle=(Button)findViewById(R.id.btn_puzzle);
        btn_puzzle.setOnClickListener(listener);
        btn_Img=(ImageButton)findViewById(R.id.Img);
        btn_Img.setOnClickListener(listener);
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(listener);
        PZ=(LinearLayout)findViewById(R.id.layout_puzzle);
        PV=new DrawView(Puzzle.this);
        PV.setAlpha(1.0f);//遮色片效果
        PV.setMinimumHeight(1000);//設定最小高度
        PV.setMinimumWidth(1000);//設定最小寬度
        PZ.addView(PV);
        bt=new BitmapTool();
        P.setColor(Color.WHITE);
        P.setStrokeWidth(5.0f);

        image_squares=new ArrayList<>();

    }
    View.OnClickListener listener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.Img:
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 998);
                    PV.invalidate();
                    break;
                case R.id.btn_puzzle:
                    if(bitmap!=null)
                    {play=true;
                    PV.invalidate();}
                    break;
                case R.id.btn_save:
                    if(bitmap2!=null){
                        saveBitmap(bitmap2);
                    }


                    break;

            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode==RESULT_OK){
            Log.d("result",String.valueOf(requestCode));
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                bitmap=bt.NewImage(getApplicationContext(),800,bitmap);
                btn_Img.setImageBitmap(bitmap);
                image_squares.clear();
                for(int x=0;x<MaxX;x++){
                    for(int y=0;y<MaxY;y++){
                        image_squares.add(bitmap.createBitmap(bitmap,x*GS,y*GS,GS,GS));
                        Log.d("SquSize",String.valueOf(image_squares.size()));
                    }
                }
                PV.invalidate();

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        else{Log.d("error","error");Log.d("result",String.valueOf(resultCode));}
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class DrawView extends View {

        public DrawView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if(play==true && bitmap!=null){
                play=false;
                List<Integer> Puzzles=new ArrayList<>();
                for(int i=0;i<image_squares.size();i++){
                Puzzles.add(i);
                    Log.d("Pizzles",String.valueOf(Puzzles.indexOf(i)));
                }
                for (int x=0;x<MaxX;x++){
                        for(int y=0;y<MaxY;y++){
                            int index=rnd.nextInt(Puzzles.size());
                            Log.d("index",String.valueOf(index));
                            canvas.drawBitmap( image_squares.get(Puzzles.get(index)),x*GS,y*GS,P);
                            canvas.drawLine(x*GS, y*GS, (x+1)*GS, (y)*GS, P);//劃出方格線-橫線
                            canvas.drawLine(x*GS, y*GS, (x)*GS, (y+1)*GS, P);//劃出方格線-直線
                            Puzzles.remove(index);
                        }
                }
                if(bitmap!=null) {
                    bitmap2=Bitmap.createBitmap(PV.getWidth(),PV.getHeight(), Bitmap.Config.ARGB_8888);}
                } else{
                if(bitmap2!=null)
                canvas.drawBitmap(bitmap2,0,0,P);

            }
        }
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
    private void saveBitmap(Bitmap bitmap){
        try{
            File dirFile=new File(sdCardDir);
            if (!dirFile.exists()) {              //如果不存在，那就建立這個資料夾
                dirFile.mkdirs();}
            File file=new File(sdCardDir,String.valueOf(rnd.nextInt())+".png");

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Log.d("file",String.valueOf(file.toURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

