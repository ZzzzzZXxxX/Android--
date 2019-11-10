package com.itcast.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity {
    private TextView mTextView;
    private Intent intent;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mTextView=(TextView)findViewById(R.id.content_tv);
        intent=getIntent();
        content=intent.getStringExtra("content");
        mTextView.setText(content);



    }
}
