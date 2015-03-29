package com.smikhlevskiy.formuladraw.util;

/**
 * converts screen coordinates to function coordinates && 
 * converts function coordinates to screen  coordinates
 */
public class ScaleCoordinates {
	// ----------Screen area---
	private float dpXStart = 0;
	private float dpYStart = 0;
	private float dpWidth = 0;
	private float dpHeight = 0;
	// ----------f(x) area---
	private double fXStart = 0;
	private double fYStart = 0;
	private double fWidth = 0;
	private double fHeight = 0;
	// -------Attribut of out function from Down to Up, or from Up to down
	private boolean fromDownToUp = true;

	public float getDpX(double fX) {
		return (float) (dpXStart + dpWidth * (fX - fXStart) / fWidth);
	}

	public float getDpY(double fY) {
	
		if (fromDownToUp)
			return (float) (dpYStart + dpHeight - dpHeight * (fY - fYStart) / fHeight);
		else
			return (float) (dpYStart + dpHeight * (fY - fYStart) / fHeight);
	}

	public double getFX(double dpX) {
		return fXStart + fWidth * (dpX - dpXStart) / dpWidth;
	}

	public double getFY(double dpY) {
		if (fromDownToUp)

			return fYStart - (dpY - dpYStart - dpHeight) * fHeight / dpHeight;
		else
			return fYStart + fHeight * (dpY - dpYStart) / dpHeight;
	}

	/*
	 * Set Coordinates of screen area
	 */
	public void setScreenArea(float dpXStart, float dpYStart, float dpWidth, float dpHeight) {
		this.dpXStart = dpXStart;
		this.dpYStart = dpYStart;
		this.dpWidth = dpWidth;
		this.dpHeight = dpHeight;
	}

	/*
	 * Set Coordinates of function area
	 */
	public void setFunctionArea(double afXStart, double afYStart, double afWidth, double afHeight) {
		this.fXStart = afXStart;
		this.fYStart = afYStart;
		this.fWidth = afWidth;
		this.fHeight = afHeight;

	}

	/**
	 * @return the fromDownToUp
	 */
	public boolean isFromDownToUp() {
		return fromDownToUp;
	}

	/**
	 * the fromDownToUp to set
	 */
	public void setFromDownToUp(boolean fromDownToUp) {
		this.fromDownToUp = fromDownToUp;
	}

}
