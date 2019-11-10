package com.itcast.music;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private List<String> lstFile = new ArrayList<String>(); //结果 List
    private List<String> lstFilename = new ArrayList<String>(); //名字
    MusicService.MyBinder binder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstFile.clear();
        GetFiles("/data/data/com.itcast.music/musicfile/");
        toName(lstFile, lstFilename);
        //初始化ListView控件
        mListView = (ListView) findViewById(R.id.lv);
        //创建一个Adapter的实例
        MyBaseAdapter mAdapter = new MyBaseAdapter();
        //设置Adapter
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                passDate(i);
            }
        });


    }

    public void GetFiles(String Path) //搜索目录
    {
        File[] files = new File(Path).listFiles();

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            lstFile.add(f.getPath());
        }
    }

    public void toName(List<String> list, List<String> name) {
        for (int i = 0; i < list.size(); i++) {
            String fileName = list.get(i);
            String fileNameNow = fileName.substring(fileName.lastIndexOf("/") + 1);
            name.add(fileNameNow);

        }
    }

    public Bitmap loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        return bitmap;
    }

    public void passDate(int i) {
        Intent intent = new Intent(this, MusicActivity.class);
        intent.putExtra("path",lstFile.get(i));

        startActivity(intent);
    }

    class MyBaseAdapter extends BaseAdapter {
        //得到item的总数
        @Override
        public int getCount() {
            //返回ListView Item条目的总数
            return lstFilename.size();
        }

        //得到Item代表的对象
        @Override
        public Object getItem(int position) {
            //返回ListView Item条目代表的对象
            return lstFilename.get(position);
        }

        //得到Item的id
        @Override
        public long getItemId(int position) {
            //返回ListView Item的id
            return position;
        }

        //得到Item的View视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).
                        inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById
                        (R.id.item_tv);
                holder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTextView.setText(lstFilename.get(position));
            holder.imageView.setImageBitmap(loadingCover(lstFile.get(position)));

            return convertView;
        }

        class ViewHolder {
            TextView mTextView;
            ImageView imageView;
        }


    }

}