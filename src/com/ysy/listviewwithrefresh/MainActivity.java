package com.ysy.listviewwithrefresh;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysy.listviewwithrefresh.ListViewWithRefresh.OnPullDownGetDataListener;
import com.ysy.listviewwithrefresh.ListViewWithRefresh.OnPullUpGetDataListener;
import com.ysy.listviewwithrefresh.other.MyAdapter;
import com.ysy.listviewwithrefresh.other.News;

public class MainActivity extends Activity
{
	private ListViewWithRefresh listView_main;
	private MyAdapter adapter;
	
	private int pageIndex;
	private String urlAddr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView_main = (ListViewWithRefresh)findViewById(R.id.listView_main);
		adapter = new MyAdapter(this);
		listView_main.setAdapter(adapter);
		listView_main.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(MainActivity.this, "Hello " + position, Toast.LENGTH_SHORT).show();
			}
		});
		listView_main.setOnScrollListener(new OnScrollListener()
		{
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				if (firstVisibleItem + visibleItemCount == totalItemCount)
				{
					//Toast.makeText(MainActivity.this, "下拉到底了！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		listView_main.setOnPullDownGetDataListener(new OnPullDownGetDataListener()
		{
			
			@Override
			public void loadingData()
			{
				new Thread()
				{
					public void run()
					{
						pageIndex = 1;
						buildURL();
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
										listView_main.pullDownRefreshComplete();
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
		});
		listView_main.setOnPullUpGetDataListener(new OnPullUpGetDataListener()
		{
			
			@Override
			public void loadingData()
			{
				listView_main.setFooterLayoutVisible();
				new Thread()
				{
					public void run()
					{
						pageIndex++;
						buildURL();
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
										listView_main.pullUpRefreshComplete();
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
		});
		init();
	}

	private void buildURL()
	{
		urlAddr = "http://litchiapi.jstv.com/api/GetFeeds?column=0&PageSize=10&pageIndex="+pageIndex+"&val=100511D3BE5301280E0992C73A9DEC41";
	}
	
	private void init()
	{
		pageIndex = 1;
		buildURL();
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
