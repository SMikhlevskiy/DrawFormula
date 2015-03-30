package com.smikhlevskiy.formuladraw.model;

public class SpinnerItemLines {
	String text;
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	int color;

	public SpinnerItemLines(String text, int color) {
		this.text = text;
		this.color = color;
	}
}
