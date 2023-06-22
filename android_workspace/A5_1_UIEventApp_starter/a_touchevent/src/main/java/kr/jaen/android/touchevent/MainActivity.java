package kr.jaen.android.touchevent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class Point{
        float x;
        float y;
        boolean isContinue;
        Point(float x, float y, boolean isContinue){
            this.isContinue = isContinue;
            this.x = x;
            this.y = y;
            }
       }


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_SCSA";

    private static class MyPaintView extends View{
        public MyPaintView(Context context){
            super(context);
        }
        List<Point> list = new ArrayList<>(); //좌표정보

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();//붓
            paint.setColor(Color.RED);//색깔
            paint.setStrokeWidth(10f);//두께

            for (int i = 0; i < list.size(); i++) {
                Point point = list.get(i);
                if(point.isContinue){
                    canvas.drawLine(list.get(i-1).x, list.get(i-1).y, point.x, point.y, paint);
                }

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    list.add(new Point(event.getX(), event.getY(), false));
                    break;
                case MotionEvent.ACTION_MOVE:
                    list.add(new Point(event.getX(), event.getY(), true));
                    Log.d(TAG, "onTouchEvent: "+event.getX());
                    break;
                case MotionEvent.ACTION_UP:
                    list.add(new Point(event.getX(), event.getY(), true));
                    break;

            }
            invalidate(); //ondraw 부름
            return true; //true: 동작 끝 /  false:뒤 이벤트를 수행하겠다
        }
    }

//    private static class MyTouchListener implements View.OnTouchListener {

        // Event Handler 메서드 작성
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    Log.d(TAG, "화면 누름: " + event.getX() + ", " + event.getY());
//                    return true;
//
//                case MotionEvent.ACTION_MOVE:
//                    Log.d(TAG, "누른채로 움직임: " + event.getX() + ", " + event.getY());
//                    return true;
//
//                case MotionEvent.ACTION_UP:
//                    Log.d(TAG, "화면에서 뗌: " + event.getX() + ", " + event.getY());
//                    return true;
//            }
//            return false;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activity 화면 전체를 View 객체로 가져옴
        View view = new MyPaintView(this);
        setContentView(view);

        // View에 Event Listener를 등록함
//        view.setOnTouchListener(new MyTouchListener());
    }
}