package com.example.map_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class Scratch extends AppCompatActivity {
    private LinearLayout scratch_layout;
    private GestureDetector mDetector;
    private Scratch.DrawView SV;
    private Paint P1,P2;
    private float pointX,pointY,lastX,lastY;
    private Button btn_reset;
    private boolean reset=false;
    private Bitmap bitmap,cover,bg,fg;
    private Canvas mCanvas;
    private float CS=100f;
    private Path mPath;
    BitmapTool bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        btn_reset=findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(listener);
        //mDetector=new GestureDetector(getApplicationContext(), new OnTouchListener());
        scratch_layout=(LinearLayout)findViewById(R.id.scratch_layout);
        SV=new DrawView(this);
        SV.setAlpha(1.0f);
        SV.setMinimumHeight(1000);//設定最小高度
        SV.setMinimumWidth(1000);//設定最小寬度
        scratch_layout.addView(SV);
        init();




    }
    private void init(){
        bt=new BitmapTool();
        bg=bt.NewImage(this,1000,R.drawable.girl);

        //bg= BitmapFactory.decodeResource(getResources(),R.drawable.girl);
        fg=Bitmap.createBitmap(bg.getWidth(),bg.getHeight(), Bitmap.Config.ARGB_8888);
//        bitmap=Bitmap.createBitmap(SV.getWidth(),SV.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(fg);
        mCanvas.drawColor(Color.GRAY);
        P1=new Paint();
        P1.setAlpha(0);
        P1.setAntiAlias(true);
        P1.setStrokeJoin(Paint.Join.ROUND);
        P1.setStrokeCap(Paint.Cap.ROUND);
        P1.setStrokeWidth(50);
        P1.setStyle(Paint.Style.STROKE);
        P1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//por這兩個是啥?->兩張圖層疊加時的覆蓋模式設定
        //參考:https://blog.csdn.net/hiyohu/article/details/12509731

        //P2本來是拿來寫字的,暫時不用
        P2=new Paint();

        P2.setAlpha(0);
        P2.setAntiAlias(true);
        P2.setStrokeJoin(Paint.Join.ROUND);
        P2.setStrokeCap(Paint.Cap.ROUND);
        P2.setStrokeWidth(2);
        P2.setTextSize(60);
        P2.setStyle(Paint.Style.STROKE);
        P2.setTextAlign(Paint.Align.CENTER);

        //
        mPath = new Path();

    }
    View.OnClickListener listener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_reset:
                    reset=true;
                    mCanvas.drawColor(Color.GRAY);
                    SV.invalidate();

                    break;
            }

        }
    };

//    private class OnTouchListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            pointX=e.getX();
//            pointY=e.getY();
//           Log.d("position",String.valueOf(e.getX()+"/")+String.valueOf(e.getY()));
//            SV.invalidate();
//            return true;
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getActionMasked();
        Log.d("XY","X:"+String.valueOf(event.getX())+"Y:"+String.valueOf(event.getY()));
        SV.invalidate();
        switch (action) {
            case ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(event.getX(), event.getY());
                break;
            case ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                break;
            case ACTION_UP:
                break;
            case ACTION_CANCEL:
                break;
        }
        mCanvas.drawPath(mPath, P1);
        SV.invalidate();
        return true;
       // mDetector.onTouchEvent(event);
    }


    public class DrawView extends View {

        public DrawView(Context context) {
            super(context);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(bg,0,0,null);//畫筆可以Null??
            canvas.drawBitmap(fg,0,0,null);



//            if(reset==true){
//                reset=false;
//            P.setColor(Color.GRAY);
//            canvas.drawRect(0, 0, scratch_layout.getWidth(),  scratch_layout.getHeight(), P);
//            cover=Bitmap.createBitmap(scratch_layout.getWidth(),scratch_layout.getHeight(), Bitmap.Config.ARGB_8888);
//            }
//
//            //P.setAlpha(0);
//            canvas.drawCircle(pointX,pointX,CS,P);
//            cover=Bitmap.createBitmap(SV.getWidth(),SV.getHeight(), Bitmap.Config.ARGB_8888);
//            canvas.drawBitmap(cover,0,0,P);


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
}
