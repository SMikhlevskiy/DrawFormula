package com.smikhlevskiy.formuladraw.ui;

/**
 * Draw Graphic on View
 * 
 */
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.util.FDConstants;

import com.smikhlevskiy.formuladraw.util.ScaleСoordinates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GraphicView extends View {

	private ReversePolishNotation reversePolishNotation[] = null;
	private boolean[] isdYdT = null;
	private double xMin = 0.0;
	private double xMax = 0.0;
	private double yMin = 0.0;
	private double yMax = 0.0;

	private double gridYStart = 0.0;
	private double gridYStep = 0.0;
	private double gridXStart = 0.0;
	private double gridXStep = 0.0;

	private float xTouchOld = 0;
	private float yTouchOld = 0;
	private int lastPointerCount = 1;
	private float lastDtX = 0;
	private float lastDtY = 0;
	private boolean drawCustomCanvas = false;
	private ScaleСoordinates sc = new ScaleСoordinates();

	private Handler outHandler;

	public void setOutHandler(Handler outHandler) {
		this.outHandler = outHandler;
	}

	private void initImageView() {

	}

	public GraphicView(Context context) {
		super(context);

	}

	public GraphicView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public GraphicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/*
	 * calck optimal Step for scale grid
	 */
	private double calcStepGrid(double interval, int kMin, int kMax, int kOpt, boolean isTime) {

		if (interval <= 0)
			return 0;
		double signinteval = Math.signum(interval);
		interval = Math.abs(interval);
		// array constants for out scale of graphic
		double stData[] = { 1, 2, 2.5, 5 };
		double stTime[] = { 1, 2, 3, 5, 10, 15, 30, 45, 60, 120, 150, 180, 240, 720, 1440, 2880, 7200, 14400, 43200,
				525600 };
		double st[];
		if (isTime)
			st = stTime;
		else
			st = stData;

		if (!isTime) {
			int j = 1;
			while (Math.abs(interval / st[0]) < kMin) {
				for (int i = 0; i < st.length; i++)
					st[i] = st[i] / 10;
				j++;
				if (j > 1000)
					return signinteval * interval / kMin;// Bounce protection
			}

			j = 1;
			while (Math.abs(interval / st[st.length - 1]) > kMax) {
				for (int i = 0; i < st.length; i++)
					st[i] = st[i] * 10;
				j++;
				if (j > 1000)
					return interval / kMin;// Bounce protection
			}

		}

		int iOpt = -1;
		double rOpt = 100000;

		for (int i = 0; i < st.length; i++)
			if (Math.abs(st[i]) > 0)
				if (Math.abs(interval / st[i] - kOpt) < rOpt) {
					rOpt = Math.abs(interval / st[i] - kOpt);
					iOpt = i;

				}
		if (iOpt >= 0)
			return st[iOpt];
		else
			return interval / kOpt;

	}

	/*
	 * find optimal begin point for scale grid
	 */
	private double calckGridBeginPoint(double startValue, double step) {
		if (step <= 0)
			return startValue;
		double x = 0;

		if ((startValue > 0) && (startValue <= step * 1.5))
			x = 0;
		else
			x = 1.0 * step * ((int) ((startValue - 1.5 * step) / step));

		int j = 0;
		while (startValue - x > step) {

			x = x + step;

			if (j > 1000)
				return startValue;
			j++;
		}

		return x;
	}

	/**
	 *   
	 */
	private void prepareGraphicArea() {
		sc.setScreenArea(1, 1, getWidth() - 1, getHeight() - 1);
		sc.setFunctionArea(xMin, yMin, xMax - xMin, yMax - yMin);

		gridXStep = this.calcStepGrid(xMax - xMin, 2, 4, 3, false);
		gridXStart = this.calckGridBeginPoint(xMin, gridXStep);

		gridYStep = this.calcStepGrid(yMax - yMin, 2, 4, 3, false);
		gridYStart = this.calckGridBeginPoint(yMin, gridYStep);
	}

	/**
	 * set xMin,xMin and calculate yMin&yMax
	 * 
	 */
	public void setMinMax(Double xMin, Double xMax) {

		this.xMin = xMin;
		this.xMax = xMax;
		yMin = Double.MAX_VALUE;
		yMax = Double.MIN_VALUE;
		for (int i = 0; i < FDConstants.colorSpinnerLines.length; i++)
			for (int xi = 0; xi <= this.getWidth(); xi++) {
				if (reversePolishNotation[i]==null) continue;
				double x = xMin + 1.0 * xi * (xMax - xMin) / this.getWidth();
				double y = 0;
				try {
					y = reversePolishNotation[i].calculation(x);

				} catch (ArithmeticException e) {

					continue;
				}

				if (y > yMax)
					yMax = y;
				if (y < yMin)
					yMin = y;
			}

		yMax = yMax + 0.1 * (yMax - yMin);
		yMin = yMin - 0.1 * (yMax - yMin);
		if (Math.abs(yMax - yMin) < 0.00001) {
			yMin = yMin - 10;
			yMax = yMax + 10;
		}

		prepareGraphicArea();

	}

	public void setDrawCustomCanvas(boolean drawCustomCanvas) {
		this.drawCustomCanvas = drawCustomCanvas;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		
		if (!drawCustomCanvas) {
			super.onDraw(canvas);
			return;
		}
		
		Paint mPaint = new Paint();
		// -----Draw background
		mPaint.setColor(Color.WHITE);
		canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mPaint);

		// -------Draw limit lines
		mPaint.setColor(Color.BLACK);
		canvas.drawLine(1, 1, getWidth() - 1, 1, mPaint);
		canvas.drawLine(getWidth() - 1, getHeight() - 1, getWidth() - 1, 1, mPaint);
		canvas.drawLine(getWidth() - 1, getHeight() - 1, 1, getHeight() - 1, mPaint);
		canvas.drawLine(0, 0, 0, getHeight() - 1, mPaint);

		mPaint.setColor(Color.rgb(150, 100, 100));
		mPaint.setStrokeWidth(3);

		canvas.drawLine(0, (float) sc.getDpY(0), getWidth(), (float) sc.getDpY(0), mPaint);// Draw
		// Y=0
		// Line

		canvas.drawLine((float) sc.getDpX(0), 0, (float) sc.getDpX(0), getHeight(), mPaint);// Draw
		// X=0
		// Line

		mPaint.setStrokeWidth(1);
		mPaint.setTextSize(20);
		// NumberFormat formatter = new DecimalFormat("#0.000");
		mPaint.setColor(Color.rgb(50, 50, 50));
		double x = gridXStart;

		while (x <= xMax) {

			canvas.drawLine((float) sc.getDpX(x), 0, (float) sc.getDpX(x), getHeight(), mPaint);
			canvas.drawText(x + "", (float) sc.getDpX(x), (float) getHeight() - 10, mPaint);
			x = x + gridXStep;
		}
		double y = gridYStart;
		while (y <= yMax) {

			canvas.drawLine(0, (float) sc.getDpY(y), getWidth(), (float) sc.getDpY(y), mPaint);

			if (getHeight() - sc.getDpY(y) > 25)
				canvas.drawText(y + "", 0, (float) sc.getDpY(y), mPaint);
			y = y + gridYStep;
		}

		mPaint.setStrokeWidth(3);
		for (int i = 0; i < FDConstants.colorSpinnerLines.length; i++)
		{
			if (reversePolishNotation[i]==null) continue;
		
			for (double fx = 0; fx < this.getWidth(); fx++) {
				mPaint.setColor(FDConstants.colorSpinnerLines[i]);
				double x1 = sc.getFX(fx); // xMin + 1.0 * xi * (xMax - xMin) /
											// this.getWidth();
				double y1 = 0;
				try {
					y1 = reversePolishNotation[i].calculation(x1);
				} catch (ArithmeticException e) {
					continue;
				}
				double x2 = sc.getFX(fx + 1);// xMin + 1.0 * (xi + 1.0) * (xMax
												// -
												// xMin) / this.getWidth();
				double y2 = 0;
				try {
					y2 = reversePolishNotation[i].calculation(x2);
				} catch (ArithmeticException e) {
					continue;
				}

				if (isdYdT[i]) {// dY/dT
					double y3 = 0;
					double x3 = sc.getFX(fx + 2);// xMin + 1.0 * (xi + 2.0) *
													// (xMax

					try {
						y3 = reversePolishNotation[i].calculation(x3);
					} catch (ArithmeticException e) {
						continue;
					}
					
					y1=(y2-y1)/(x2-x1);
					y2=(y3-y2)/(x3-x2);
				}

				canvas.drawLine((float) fx, (float) sc.getDpY(y1), (float) (fx + 1.0), (float) sc.getDpY(y2), mPaint);// draw
				// f(x)
				// to
				// f(x+1)

			}
		}
	}

	/**
	 * Out XY in main Activity
	 * 
	 * @param xDown
	 * @param yDown
	 */
	private void outXY(float xDown, float yDown) {
		Message message = outHandler.obtainMessage();
		message.what = FDConstants.OUT_TEXT_INFO_MESSAGE;

		NumberFormat formatter = new DecimalFormat("#0.00");

		message.obj = new String("x=" + formatter.format(sc.getFX(xDown)) + ", y=" + formatter.format(sc.getFY(yDown)));
		outHandler.sendMessage(message);
	}

	/**
 * 
 */
	private void outMessageOnChangeXMinMax() {
		Message message = outHandler.obtainMessage();
		message.what = FDConstants.CHANGE_XLIMITS;
		double xMinMaxa[] = new double[2];
		xMinMaxa[0] = xMin;
		xMinMaxa[1] = xMax;
		message.obj = xMinMaxa;
		outHandler.sendMessage(message);

	}

	/**
 * 
 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		int actionMask = event.getActionMasked();

		int pointerIndex = event.getActionIndex();

		int pointerCount = event.getPointerCount();

		switch (actionMask) {
		case MotionEvent.ACTION_DOWN:

			float xDown = event.getX();
			float yDown = event.getY();

			// Toast.makeText(getContext(), "x="+x+", y="+y,0).show();
			outXY(xDown, yDown);

			// inTouch = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			// downPI = pointerIndex;
			break;

		case MotionEvent.ACTION_UP:
			xTouchOld = 0;
			yTouchOld = 0;

		case MotionEvent.ACTION_POINTER_UP:

			break;

		case MotionEvent.ACTION_MOVE:

			float x = event.getX();
			float y = event.getY();
			outXY(x, y);

			// Toast.makeText(getContext(), "x="+x+", y="+y,0).show();

			// -----------scale ----------------
			if ((pointerCount >= 2) && (lastPointerCount > 1)) {
				float dtY = Math.abs(event.getY(0) - event.getY(1));
				float dtX = Math.abs(event.getX(0) - event.getX(1));

				if (dtX - lastDtX != 0) {

					double step = 1.0 * (lastDtX - dtX) * (xMax - xMin) / getWidth();
					xMin = xMin - step / 2;
					xMax = xMax + step / 2;
					prepareGraphicArea();
					outMessageOnChangeXMinMax();
					invalidate();

				}
				if (dtY - lastDtY != 0) {

					double step = 1.0 * (lastDtY - dtY) * (yMax - yMin) / getHeight();
					yMin = yMin - step / 2;
					yMax = yMax + step / 2;
					prepareGraphicArea();
					outMessageOnChangeXMinMax();
					invalidate();

				}

			}
			lastPointerCount = pointerCount;

			if (pointerCount > 1) {
				lastDtX = Math.abs(event.getX(0) - event.getX(1));
				lastDtY = Math.abs(event.getY(0) - event.getY(1));

				xTouchOld = x;
				yTouchOld = y;

				return true;
			}

			double step;
			// -------Move----
			if ((Math.abs(xTouchOld - x) > 0) && (xTouchOld > 0)) {
				step = 1.0 * (xTouchOld - x) * (xMax - xMin) / getWidth();
				xMin = xMin + step;
				xMax = xMax + step;

				// xMin=xMin+xTouchOld-x;
				// xMax=xMax+xTouchOld-x;
				prepareGraphicArea();
				outMessageOnChangeXMinMax();
				invalidate();

			}
			if ((Math.abs(yTouchOld - y) > 0) && (yTouchOld > 0)) {
				step = 1.0 * (yTouchOld - y) * (yMax - yMin) / getHeight();
				yMin = yMin - step;
				yMax = yMax - step;
				prepareGraphicArea();
				outMessageOnChangeXMinMax();
				invalidate();

			}
			xTouchOld = x;
			yTouchOld = y;
			break;
		}

		return true;
	}

	public void setReversePolishNotation(ReversePolishNotation[] reversePolishNotation) {
		this.reversePolishNotation = reversePolishNotation;
	}

	public void setIsdYdT(boolean[] isdYdT) {
		this.isdYdT = isdYdT;
	}

	public Double getxMin() {
		return xMin;
	}

	public Double getxMax() {
		return xMax;
	}
}
