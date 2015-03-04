package com.ysy.listviewwithrefresh.other;

public class News
{
	private String category;
	private Data data;
	private int id;
	private int oid;

	public News()
	{
		super();
	}

	public News(String category, Data data, int id, int oid)
	{
		super();
		this.category = category;
		this.data = data;
		this.id = id;
		this.oid = oid;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public Data getData()
	{
		return data;
	}

	public void setData(Data data)
	{
		this.data = data;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getOid()
	{
		return oid;
	}

	public void setOid(int oid)
	{
		this.oid = oid;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		News other = (News)obj;
		if (id != other.id)
			return false;
		return true;
	}

}
