package com.example.map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class Test extends AppCompatActivity {
    private ImageView imgafter;
    private ImageView imgbefore;
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private Bitmap before;
    private Bitmap after;
    private float pointX,pointY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        init();
    }

    private void init(){
    imgafter=(ImageView)findViewById(R.id.after) ;
    imgbefore=(ImageView)findViewById(R.id.before) ;
        after = BitmapFactory.decodeResource(getResources(), R.drawable.girl);
        before = BitmapFactory.decodeResource(getResources(), R.drawable.guys);
        imgafter.setImageBitmap(after);
        imgbefore.setImageBitmap(before);
        // 建立可以修改的空白的bitmap
        bitmap = Bitmap.createBitmap(before.getWidth(), before.getHeight(),
                before.getConfig());
        Log.d("size",String.valueOf(before.getWidth())+"/"+String.valueOf(before.getHeight()));
        imgbefore.setOnTouchListener(new View.OnTouchListener() {
                                         @Override
                                         public boolean onTouch(View v, MotionEvent event) {

                                             pointX = event.getX();
                                             pointY = event.getY();
                                             Log.d("position", String.valueOf(event.getX() + "/") + String.valueOf(event.getY()));
                                             switch (event.getAction()) {
                                                 case MotionEvent.ACTION_MOVE:
                                                     Log.d("draw","draw");
                                                     paint.setColor(Color.GRAY);
                                                     paint.setAlpha(127);
                                                     canvas.drawCircle(pointX,pointY,200,paint);
                                                     //bitmap.setPixel((int) pointX, (int) pointY, 0);
                                                     imgbefore.setImageBitmap(bitmap);
                                                     //imgbefore.setImageAlpha(0);

                                                     break;
                                             }
                                             return true;
                                         }
                                     });

        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
// 建立畫布
        canvas = new Canvas(bitmap);
        canvas.drawBitmap(before, new Matrix(), paint);



    }

    private class OnTouchListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            pointX=e.getX();
            pointY=e.getY();
            Log.d("position",String.valueOf(e.getX()+"/")+String.valueOf(e.getY()));

            return true;
        }
    }
}
