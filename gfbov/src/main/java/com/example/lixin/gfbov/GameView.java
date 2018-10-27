package com.example.lixin.gfbov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

class GameView extends View {

    //Canvas 画布
    //Graphics 图形
    //Paint 画笔
    //Bitmap 位图

    //Thread 线程

    private Paint paint ;
    private float screenWidth,screenHeight; //屏幕尺寸
    private float x,y;  //矩形的顶点
    private float lineX,lineY;  //直线端点
    private float radius;   //圆的半径

    private boolean flag = true ;   //线程控制标志    在true下动态改变-绘制
    private long speed =1;  //每秒帧数
    private int cDir = 1;   //圆的变化方向
    private int walk = 1;


    /*   主函数直接由View修补生成，为了更加安全的构造，此处有删除，并且智能修补下程序 或修改super();。
            public GameView(MainActivity mainActivity) {
               super();
              }
           */
    public GameView(Context context) {
        super(context);
        //获取屏幕尺寸
        this.screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = this .getResources().getDisplayMetrics().heightPixels;
        //直线起始位置
        this.lineX = 0.0f;
        this.lineY = 0.0f;
        //矩形起始点
        this.x = screenWidth/2-50;
        this.y = screenWidth/2-50;
        //圆半径
        this.radius =10;
        //画笔
        this.paint = new Paint();
        this.paint.setAntiAlias(true);


        //启动界面动态绘制线程
        new DrawThread().start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制矩形
        paint.setColor(Color.BLUE);
        canvas.drawRect(x,y,x+100,y+100, paint);
        //绘制圆形
        paint.setColor(Color.RED);
        canvas.drawCircle(screenWidth/2,screenHeight/2, radius, paint);
        //绘制直线
        paint.setColor(Color.BLACK);
        canvas.drawLine(lineX,lineY,screenWidth,screenHeight, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //屏幕触摸事件处理
        //获取触摸的坐标   getX/Y(相对坐标)    getRawX/getRawY(屏幕坐标)
        x=event.getRawX();
        y=event.getRawY();
        //更新视图：不能直接访问试图，通常不能直接调用Draw方法，需要采用异步
        postInvalidate();

        // return super.onTouchEvent(event);
        return  true;
    }

    private class DrawThread extends Thread {
        @Override
        public void run(){
            while (flag){
                long _startTime = System.currentTimeMillis();
                //游戏逻辑
                logic();

                long _endTime = System.currentTimeMillis();
                //定义每帧
                long _sleepTime = speed*40 -(_endTime - _startTime);
                if (_sleepTime > 0){
                    try {
                        Thread.sleep(_sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                postInvalidate();   //每执行一次逻辑，刷新一次界面，诸如改变形状大小，让图形闪动
            }
        }
    }

    private void logic() {
        //改变圆的大小
        if (radius > 200){
            cDir = -1;
        }
        if (radius < 0){
            cDir = 1;
        }
        radius += 5 * cDir;

        //改变直线
        float step = 10;
        switch (walk){
            case 0:     //上
                lineY -= step;
                break;
            case 1:     //右
                lineX += step;
                break;
            case 2:     //下
                lineY += step;
                break;
            case 3:     //左
                lineX -= step;
                break;
        }
        //改变方向
        if (lineX <0 && walk == 3) walk = 2;
        if (lineX >= screenWidth && walk == 1) walk = 3;
        if (lineY >screenHeight && walk ==2) walk =0;
        if (lineY <0 && walk == 0) walk = 1;



    }
}
