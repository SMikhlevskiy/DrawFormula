package com.smikhlevskiy.formuladraw.util;

/**
 * converts screen coordinates to function coordinates && 
 * converts function coordinates to screen  coordinates
 */
public class ScaleСoordinates {
	// ----------Screen area---
	private double dpXStart = 0;
	private double dpYStart = 0;
	private double dpWidth = 0;
	private double dpHeight = 0;
	// ----------f(x) area---
	private double fXStart = 0;
	private double fYStart = 0;
	private double fWidth = 0;
	private double fHeight = 0;
	// -------Attribut of out function from Down to Up, or from Up to down
	private boolean fromDownToUp = true;

	public double getDpX(double fX) {
		return (1.0*dpXStart + 1.0*dpWidth * (fX - fXStart)/ fWidth );
	}

	public double getDpY(double fY) {
		// double yi1 = 1.0 * this.getHeight() - 1.0 * (y1 - yMin) *
		// this.getHeight() / (yMax - yMin);

		if (fromDownToUp)
			return (double) (dpYStart + dpHeight - dpHeight * (fY - fYStart) / fHeight);
		else
			return (double) (dpYStart + dpHeight * (fY - fYStart) / fHeight);
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
	public void setScreenArea(double dpXStart, double dpYStart, double dpWidth, double dpHeight) {
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
