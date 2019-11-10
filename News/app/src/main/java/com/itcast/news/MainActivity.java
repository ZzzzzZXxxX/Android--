package com.itcast.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.vorlonsoft.android.http.AsyncHttpClient;
import com.vorlonsoft.android.http.JsonHttpResponseHandler;

import com.loopj.android.image.SmartImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private LinearLayout loading;
    private ListView mListView;
    private List<String> title = new ArrayList<String>();
    private List<String> content = new ArrayList<String>();
    private List<String> type = new ArrayList<String>();
    private List<String> imgurl = new ArrayList<String>();
    private List<String> comment = new ArrayList<String>();
    private HashMap<Integer,String> mp4url=new HashMap<Integer, String>();
    private JSONArray array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        loading = (LinearLayout) findViewById(R.id.loading);
        mListView = (ListView) findViewById(R.id.lv_news);
        getdata();

    }

    protected void getdata() {
        AsyncHttpClient client = new AsyncHttpClient(); //AsyncHttpClient

        client.get("http://172.23.160.75:8080/NewsInfo.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    array = response.getJSONArray("News");
                    if (array == null) {
                        Toast.makeText(MainActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                    } else {
                        //读json，存入list
                        for (int i = 0; i < array.length(); i++) {
                            title.add(array.getJSONObject(i).get("title").toString());
                            content.add(array.getJSONObject(i).get("content").toString());
                            type.add(array.getJSONObject(i).get("type").toString());
                            imgurl.add("http://172.23.160.75:8080/" + array.getJSONObject(i).get("imgurl").toString());
                            comment.add(array.getJSONObject(i).get("comment").toString());
                            if(array.getJSONObject(i).get("type").toString().equals("3")){
                                mp4url.put(i,"http://172.23.160.75:8080/"+array.getJSONObject(i).get("mp4url").toString());

                            }


                        }
                        //更新界面
                        loading.setVisibility(View.INVISIBLE);
                        //创建Adapter的实例
                        //设置Adapter
                        mListView.setAdapter(new MyBaseAdapter());
                        //点击Item
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                passDate(i);
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MainActivity.this, "请求失败！！！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void passDate(int i) {
        if(type.get(i).equals("1")||type.get(i).equals("2")){
            Intent intent = new Intent(this, ContentActivity.class);
            intent.putExtra("content", content.get(i));
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("content", content.get(i));
            intent.putExtra("url", mp4url.get(i));
            startActivity(intent);
        }

    }

    class MyBaseAdapter extends BaseAdapter {
        //得到item的总数
        @Override
        public int getCount() {
            //返回ListView Item条目的总数
            return title.size();
        }

        //得到Item代表的对象
        @Override
        public Object getItem(int position) {
            //返回ListView Item条目代表的对象
            return title.get(position);
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
                        (R.id.tv_title);
                holder.siv = (SmartImageView) convertView.findViewById(R.id.siv_icon);
                holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTextView.setText(title.get(position));
            holder.siv.setImageUrl(imgurl.get(position), R.mipmap.ic_launcher);
            holder.tv_description.setText(content.get(position));
            switch (type.get(position)) {
                //不同新闻类型设置不同的颜色和不同的内容
                case "1":
                    holder.tv_type.setText("评论:" + comment.get(position));
                    break;
                case "2":
                    holder.tv_type.setTextColor(Color.RED);
                    holder.tv_type.setText("专题");
                    break;
                case "3":
                    holder.tv_type.setTextColor(Color.BLUE);
                    holder.tv_type.setText("LIVE");
                    break;
            }


            return convertView;
        }

        class ViewHolder {
            TextView mTextView;
            private TextView tv_description;
            private TextView tv_type;
            SmartImageView siv;  //SmartImageView
        }


    }
}
