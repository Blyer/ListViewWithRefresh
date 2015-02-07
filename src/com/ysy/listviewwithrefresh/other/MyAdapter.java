package com.ysy.listviewwithrefresh.other;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysy.bitmapcache.ImageLoader;
import com.ysy.bitmapcache.ImageLoader.OnLoadingFinishedListener;
import com.ysy.listviewwithrefresh.R;

public class MyAdapter extends BaseAdapter
{
	private Context mContext;
	private List<News> mData;

	public MyAdapter(Context context)
	{
		mContext = context;
		mData = new ArrayList<News>();
	}

	public void setData(List<News> data)
	{
		if (data == null)
		{
			return;
		}
		boolean flag = false;
		for (int temp = data.size(), i = 0; i < temp; ++i)
		{
			News news = data.get(i);
			if (!mData.contains(news))
			{
				mData.add(news);
				flag = true;
			}
		}
		if (flag)
			this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return mData.size();
	}

	@Override
	public Object getItem(int pos)
	{
		return mData.get(pos);
	}

	@Override
	public long getItemId(int pos)
	{
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup)
	{
		ViewHolder holder = null;
		if (view == null)
		{
			view = View.inflate(mContext, R.layout.news_item, null);
			holder = new ViewHolder();
			holder.imageView_cover = (ImageView)view.findViewById(R.id.imageView_cover);
			holder.textView_subject = (TextView)view.findViewById(R.id.textView_subject);
			holder.textView_summary = (TextView)view.findViewById(R.id.textView_summary);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)view.getTag();
		}
		Data data = mData.get(pos).getData();
		holder.textView_subject.setText(data.getSubject());
		holder.textView_summary.setText(data.getSummary());
		String urlAddr = "http://litchiapi.jstv.com"+data.getCover();
		holder.imageView_cover.setTag(urlAddr);
		final ImageView imageView = holder.imageView_cover;
		ImageLoader.getInstance().loadImage(urlAddr, new OnLoadingFinishedListener()
		{
			
			@Override
			public void loadingFinished(String urlAddr, Bitmap bm)
			{
				if (urlAddr.equals(imageView.getTag()))
				{
					imageView.setImageBitmap(bm);
				}
			}
		});
		return view;
	}
	
	private class ViewHolder
	{
		ImageView imageView_cover;
		TextView textView_subject;
		TextView textView_summary;
	}

}
