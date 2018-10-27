package com.example.lixin.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

   /*
    生命周期：参见官方API文档
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i( "**MyDebug**","生命周期onCreate方法");

        //隐式意图
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);  // 隐式意图打开电话拨号
                Uri data = Uri.parse("tel:19979241110");
                intent.setData(data);
                startActivity(intent);
            }
        });

        //隐式意图2，连接互联网
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("http://www.jju.edu.cn");
                intent.setData(data);
                startActivity(intent);
            }
        });


    }

    public void sendMessage(View view){
        // 定义意图：显式意图
        Intent intent = new Intent(this,DisplayMessageActivity.class);
        // 获取界面组件  R类 -- 不用编辑的基类，由系统自动生成，自动对认识目录中的资源进行变量命名
        // 明磊资源应该符合变量名的定义方式
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        //意图绑定数据
        intent.putExtra("Message",message);
        //启动活动
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i( "**MyDebug**","生命周期onCreate方法");

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i( "**MyDebug**","生命周期onResume方法");

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i( "**MyDebug**","生命周期onStop方法");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i( "**MyDebug**","生命周期onPause方法");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i( "**MyDebug**","生命周期onDestroy方法");

    }


}
