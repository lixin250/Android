package com.example.lixin.gfbsurfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import static android.view.View.*;


/**
 * SurfaceView 是 View的子类，常用于更新较快的界面
 * SurfaceHolder.Callback SurfaceView的生命周期的回调接口
 * View.OnTouchListener  触摸事件
 * SensorEventListener  传感时间
 * ，重力加速度
 */

class GameView extends SurfaceView implements SurfaceHolder.Callback,OnTouchListener,SensorEventListener {

    private SurfaceHolder holder;   //SurfaceView的托管类
    private Canvas canvas;  //画布
    private Paint paint;    //画笔

    private int screenWidth,screenHeight;   //屏幕相关尺寸
    private float x,y,radius = 100;  //圆相关参数
    private int direction =1;

    private boolean threadFlag; //线程管理标志

    private float speed = 1.0f; //每秒25帧

    private SensorManager sm;   //传感器管理类
    private float[] accelerometerValues = new float[3];  //加速度传感器
    private float[] mangeticFieldValues = new float[3];  // 地刺传感数据

    public GameView(Context context){
        super(context);
        holder = getHolder();   //  获取当前SurfaceView的托管对象
        holder.addCallback(this);   //加载生命周期回调接口

        paint = new Paint();

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        //圆坐标
        x = screenWidth/2;
        y = screenHeight/2;

        //设置触摸监听
        setOnTouchListener(this);

        //注册传感器
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener((SensorEventListener) this,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener((SensorEventListener) this,
                sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);

    }




    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadFlag = true;  //设置启动标志
        new DrawThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        threadFlag = false; //设置县城停止标志
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getRawX();
        y = event.getRawY();
        return false;
    }
    @Override
    public void onSensorChanged(SensorEvent event){
        //获取对应的传感数据
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            mangeticFieldValues = event.values;
        }

        //数据处理：矩阵变换
        float[] values = new float[3];
        float[] r = new float[9];
        SensorManager.getRotationMatrix(r,null,accelerometerValues,mangeticFieldValues);
        SensorManager.getOrientation(r,values);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);
        if (values[2] > 0 && x < screenWidth) x += 5;   //右移
        if (values[2] < 0 && x >0 ) x -= 5; //左移
        if (values[1] > 0 && y >0)  y -= 5; //上移
        if (values[1] < 0 && y < screenHeight)  y += 5; //下移



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private class DrawThread extends Thread{

        @Override
        public void run() {
            while (threadFlag){
                long _startTime = System.currentTimeMillis();

                canvas = holder.lockCanvas();   //获取当前画布并锁定
                logic();    //逻辑控制
                draw();     //绘图见面控制
                holder.unlockCanvasAndPost(canvas); //是防滑布并提交刷新界面

                long _endTime = System.currentTimeMillis();
                long _sleepTime = (long)(speed * 40) - (_endTime - _startTime);
                if (_sleepTime >0){
                    try {
                        Thread.sleep(_sleepTime);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }

        }
    }

    private void draw() {
        //设置画笔清楚与重绘的模式
        paint.setXfermode( new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        //绘制矩形背景
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,0,screenWidth,screenHeight,paint);
        //绘制图形
        paint.setColor(Color.BLUE);
        canvas.drawCircle(x,y,radius,paint);


    }

    private void logic() {
        radius += 5* direction;
        if (radius >= 200 || radius <= 50){
            direction *= -1;
        }


    }
}

