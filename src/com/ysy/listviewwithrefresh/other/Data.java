package com.ysy.listviewwithrefresh.other;

public class Data
{
	private String changed;
	private String cover;
	private String format;
	private String pic;
	private String subject;
	private String summary;

	public Data()
	{
		super();
	}

	public Data(String changed, String cover, String format, String pic, String subject, String summary)
	{
		super();
		this.changed = changed;
		this.cover = cover;
		this.format = format;
		this.pic = pic;
		this.subject = subject;
		this.summary = summary;
	}

	public String getChanged()
	{
		return changed;
	}

	public void setChanged(String changed)
	{
		this.changed = changed;
	}

	public String getCover()
	{
		return cover;
	}

	public void setCover(String cover)
	{
		this.cover = cover;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

}
