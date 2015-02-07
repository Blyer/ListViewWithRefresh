package com.ysy.listviewwithrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListViewWithRefresh extends ListView
{
	private Context mContext;
	private View headerView;
	private int headerViewHeight;
	private TextView textView_hint;
	private TextView textView_time;
	private ImageView imageView_arrow;
	private ProgressBar progressBar;

	private final int NONE = 0; // 正常状态
	private final int PULL = 1; // 提示下拉刷新状态
	private final int RELESE = 2; // 提示释放刷新状态
	private final int REFRESHING = 3; // 正在刷新状态
	private int state = NONE;

	private String lastUpdateTime = "上次更新于1-19 20:34";
	private boolean isPullDown;
	private int topPadding;
	private float startY = 0;

	public ListViewWithRefresh(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public ListViewWithRefresh(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		init();
	}

	public ListViewWithRefresh(Context context)
	{
		super(context);
		mContext = context;
		init();
	}

	private void init()
	{
		headerView = LayoutInflater.from(mContext).inflate(R.layout.listview_header_layout, null);
		textView_hint = (TextView)headerView.findViewById(R.id.listview_dropstate_textView_hint);
		textView_time = (TextView)headerView.findViewById(R.id.listview_dropstate_textView_time);
		imageView_arrow = (ImageView)headerView.findViewById(R.id.listview_dropstate_imageView);
		progressBar = (ProgressBar)headerView.findViewById(R.id.listview_dropstate_progressBar);
		addHeaderView(headerView);
		headerView.measure(0, 0);
		headerViewHeight = headerView.getMeasuredHeight();
		topPadding = headerView.getPaddingTop();
		setTopPadding(-headerViewHeight);
	}

	private void setTopPadding(int topPadding)
	{
		headerView.setPadding(headerView.getPaddingLeft(), topPadding, headerView.getPaddingRight(), headerView.getPaddingBottom());
		headerView.invalidate();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if (this.getFirstVisiblePosition() == 0)
				{
					startY = ev.getY();
					isPullDown = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				move(ev, startY);
				break;
			case MotionEvent.ACTION_UP:
				if (state == RELESE)
				{
					state = REFRESHING;
					refreshHeaderViewByState();
				}
				else if (state == PULL)
				{
					state = NONE;
					isPullDown = false;
					refreshHeaderViewByState();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	private void move(MotionEvent ev, float startY)
	{
		if (!isPullDown)
		{
			return;
		}
		float movingY = ev.getY();
		float space = movingY - startY;
		int topPadding = (int)(space - headerViewHeight);

		switch (state)
		{
			case NONE:
				if (space > 0)
				{
					state = PULL;
					refreshHeaderViewByState();
				}
				break;
			case PULL:
				setTopPadding(topPadding);
				if (space > headerViewHeight)
				{
					state = RELESE;
					refreshHeaderViewByState();
				}
				break;
			case RELESE:
				setTopPadding(topPadding);
				if (space < headerViewHeight)
				{
					state = PULL;
					refreshHeaderViewByState();
				}
				else if (space <= 0)
				{
					state = NONE;
					refreshHeaderViewByState();
				}
				break;
		}
	}

	private void refreshHeaderViewByState()
	{
		RotateAnimation anim1 = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		RotateAnimation anim2 = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim2.setDuration(500);
		anim2.setFillAfter(true);
		switch (state)
		{
			case NONE:
				imageView_arrow.clearAnimation();
				setTopPadding(-headerViewHeight);
				break;
			case PULL:
				imageView_arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				textView_hint.setText("下拉可以刷新！");
				textView_time.setText(lastUpdateTime);
				imageView_arrow.clearAnimation();
				imageView_arrow.setAnimation(anim2);
				break;
			case RELESE:
				imageView_arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				textView_hint.setText("松开可以刷新！");
				textView_time.setText(lastUpdateTime);
				imageView_arrow.clearAnimation();
				imageView_arrow.setAnimation(anim1);
				break;
			case REFRESHING:
				setTopPadding(topPadding);
				imageView_arrow.clearAnimation();
				imageView_arrow.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				textView_hint.setText("正在刷新！");
				textView_time.setText(lastUpdateTime);
				break;
		}
	}

}
