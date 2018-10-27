package com.example.lixin.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //获得意图
        Intent intent = this.getIntent();
        //获得数据
        String message = intent.getStringExtra("Message");
        //显示数据
        TextView textView = findViewById(R.id.textView2);
        textView.setText(message);

    }
}
