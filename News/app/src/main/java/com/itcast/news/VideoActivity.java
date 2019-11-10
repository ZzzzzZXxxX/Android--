package com.itcast.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private TextView mTextView;
    private Intent intent;
    private String url;
    private String content;
    VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        intent=getIntent();
        url=intent.getStringExtra("url");
        content=intent.getStringExtra("content");
        mTextView=(TextView)findViewById(R.id.tv_video);
        Uri uri = Uri.parse(url);
        mVideoView=(VideoView)findViewById(R.id.Vi_video);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoURI(uri);
        mVideoView.start();
        mVideoView.requestFocus();
        mTextView.setText(content);

    }
}
