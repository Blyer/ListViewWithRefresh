package com.ysy.listviewwithrefresh;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysy.listviewwithrefresh.other.MyAdapter;
import com.ysy.listviewwithrefresh.other.News;

public class MainActivity extends Activity
{
	private ListViewWithRefresh listView_main;
	private MyAdapter adapter;
	
	private static final String urlAddr = "http://litchiapi.jstv.com/api/GetFeeds?column=0&PageSize=20&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView_main = (ListViewWithRefresh)findViewById(R.id.listView_main);
		adapter = new MyAdapter(this);
		listView_main.setAdapter(adapter);
		
		new Thread()
		{
			public void run()
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(urlAddr);
				try
				{
					HttpResponse response = client.execute(get);
					if (response.getStatusLine().getStatusCode() == 200)
					{
						String result = EntityUtils.toString(response.getEntity());
						final List<News> data = parse(result);
						listView_main.post(new Runnable()
						{
							
							@Override
							public void run()
							{
								adapter.setData(data);
							}
						});
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			private List<News> parse(String result)
			{
				if (result != null)
				{
					JSONObject root = JSONObject.parseObject(result);
					JSONObject obj = root.getJSONObject("paramz");
					String str = obj.getString("feeds");
					List<News> data = JSONArray.parseArray(str, News.class);
					return data;
				}
				return null;
			}
		}.start();
	}

}
