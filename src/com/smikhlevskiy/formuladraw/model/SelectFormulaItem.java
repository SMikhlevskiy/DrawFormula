package com.smikhlevskiy.formuladraw.model;

public class SelectFormulaItem
{
	private String mainTitle;
	private String subTitle;
	


	public SelectFormulaItem(String mainT, String subT)
	{
		this.mainTitle = mainT;
		this.subTitle = subT;
		
	}

	public String getMainTitle()
	{
		return mainTitle;
	}

	public String getSubTitle()
	{
		return subTitle;
	}


	public void setMainTitle(String mainTitle)
	{
		this.mainTitle = mainTitle;
	}

	public void setSubTitle(String subTitle)
	{
		this.subTitle = subTitle;
	}


}
