package com.smikhlevskiy.formuladraw.ui;

import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.util.ScaleСoordinates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GraphicView extends View {

	private ReversePolishNotation reversePolishNotation = null;
	private Double xMin = 0.0;
	private Double xMax = 0.0;
	private Double yMin = 0.0;
	private Double yMax = 0.0;
	private float xTouchOld = 0;
	private float yTouchOld = 0;
	private int lastPointerCount = 1;
	private float lastDtX = 0;
	private float lastDtY = 0;
	private boolean drawCustomCanvas = false;
	private ScaleСoordinates sc = new ScaleСoordinates();
	

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

	public void setReversePolishNotation(ReversePolishNotation reversePolishNotation) {
		this.reversePolishNotation = reversePolishNotation;
	}

	public Double getxMin() {
		return xMin;
	}

	public Double getxMax() {
		return xMax;
	}

	/**
	 * set xMin,xMin and calckulate yMin&yMax
	 * 
	 */
	public void setMinMax(Double xMin, Double xMax) {

		this.xMin = xMin;
		this.xMax = xMax;
		yMin = Double.MAX_VALUE;
		yMax = Double.MIN_VALUE;
		for (int xi = 0; xi <= this.getWidth(); xi++) {
			double x = xMin + 1.0 * xi * (xMax - xMin) / this.getWidth();
			double y = 0;
			try {
				y = reversePolishNotation.cackulation(x);

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
		
		sc.setScreenArea(1, 1, getWidth()-1, getHeight()-1);
		sc.setFunctionArea(xMin, yMin, xMax-xMin, yMax-yMin);
		
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
		/*
		 * mPaint.setColor(Color.rgb(150, 150, 150));
		 * canvas.drawRect(1,1,getWidth()-1,getHeight()-1,mPaint);
		 */
		mPaint.setColor(Color.rgb(255, 255, 255));
		canvas.drawRect(new Rect(0, 0,getWidth(),getHeight()),mPaint);
		
		mPaint.setColor(Color.rgb(0, 0, 0));
		canvas.drawLine(1, 1, getWidth() - 1, 1, mPaint);
		canvas.drawLine(getWidth() - 1, getHeight() - 1, getWidth() - 1, 1, mPaint);
		canvas.drawLine(getWidth() - 1, getHeight() - 1, 1, getHeight() - 1, mPaint);
		canvas.drawLine(0, 0, 0, getHeight() - 1, mPaint);

    	mPaint.setColor(Color.rgb(100, 100, 100));
		mPaint.setStrokeWidth(3);
		
		canvas.drawLine(0, sc.getDpY(0), getWidth(), sc.getDpY(0), mPaint);



		canvas.drawLine(sc.getDpX(0), 0, sc.getDpX(0), getHeight(), mPaint);

		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(3);

		for (double fx = 0; fx < this.getWidth(); fx++) {

			double x1 = sc.getFX(fx); //xMin + 1.0 * xi * (xMax - xMin) / this.getWidth();
			double y1 = 0;
			try {
				y1 = reversePolishNotation.cackulation(x1);
			} catch (ArithmeticException e) {
				continue;
			}
			double x2 = sc.getFX(fx+1);//xMin + 1.0 * (xi + 1.0) * (xMax - xMin) / this.getWidth();
			double y2 = 0;
			try {
				y2 = reversePolishNotation.cackulation(x2);
			} catch (ArithmeticException e) {
				continue;
			}

			
			canvas.drawLine((float) fx, sc.getDpY(y1), (float) (fx + 1.0), sc.getDpY(y2), mPaint);
			

		}

	}
/**
 * 
 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		// событие
		int actionMask = event.getActionMasked();
		// индекс касания
		int pointerIndex = event.getActionIndex();
		// число касаний
		int pointerCount = event.getPointerCount();

		switch (actionMask) {
		case MotionEvent.ACTION_DOWN: // первое касание
			// inTouch = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
			// downPI = pointerIndex;
			break;

		case MotionEvent.ACTION_UP: // прерывание последнего касания
			xTouchOld = 0;
			yTouchOld = 0;

			// inTouch = false;
			// sb.setLength(0);
		case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
			// upPI = pointerIndex;
			break;

		case MotionEvent.ACTION_MOVE: // движение

			float x = event.getX();
			float y = event.getY();
			// -----------scale ----------------
			if ((pointerCount >= 2) && (lastPointerCount > 1)) {
				float dtY = Math.abs(event.getY(0) - event.getY(1));
				float dtX = Math.abs(event.getX(0) - event.getX(1));

				if (dtX - lastDtX != 0) {

					double step = 1.0 * (lastDtX - dtX) * (xMax - xMin) / getWidth();
					xMin = xMin - step / 2;
					xMax = xMax + step / 2;
					sc.setFunctionArea(xMin, yMin, xMax-xMin, yMax-yMin);
					invalidate();
					

				}
				if (dtY - lastDtY != 0) {

					double step = 1.0 * (lastDtY - dtY) * (yMax - yMin) / getHeight();
					yMin = yMin - step / 2;
					yMax = yMax + step / 2;
					sc.setFunctionArea(xMin, yMin, xMax-xMin, yMax-yMin);
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
				sc.setFunctionArea(xMin, yMin, xMax-xMin, yMax-yMin);
				invalidate();

			}
			if ((Math.abs(yTouchOld - y) > 0) && (yTouchOld > 0)) {
				step = 1.0 * (yTouchOld - y) * (yMax - yMin) / getHeight();
				yMin = yMin - step;
				yMax = yMax - step;
				sc.setFunctionArea(xMin, yMin, xMax-xMin, yMax-yMin);
				invalidate();

			}
			xTouchOld = x;
			yTouchOld = y;
			break;
		}

		return true;
	}

}
