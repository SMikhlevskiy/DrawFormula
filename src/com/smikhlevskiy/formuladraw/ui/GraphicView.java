package com.smikhlevskiy.formuladraw.ui;

import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

	public void setxMinMax(Double xMin, Double xMax) {

		this.xMin = xMin;
		this.xMax = xMax;
		yMin = Double.MAX_VALUE;
		yMax = Double.MIN_VALUE;
		for (int xi = 0; xi <= this.getWidth(); xi++) {
			double x = xMin + 1.0 * xi * (xMax - xMin) / this.getWidth();
			double y=0;
			try{
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
		mPaint.setColor(Color.rgb(0, 0, 0));
		canvas.drawLine(1, 1, getWidth() - 1, 1, mPaint);
		canvas.drawLine(getWidth() - 1, getHeight() - 1, getWidth() - 1, 1, mPaint);
		canvas.drawLine(getWidth() - 1, getHeight() - 1, 1, getHeight() - 1, mPaint);
		canvas.drawLine(0, 0, 0, getHeight() - 1, mPaint);

		mPaint.setColor(Color.rgb(100, 100, 100));
		mPaint.setStrokeWidth(3);

		double yi0 = 1.0 * this.getHeight() - 1.0 * (0 - yMin) * this.getHeight() / (yMax - yMin);
		canvas.drawLine(0, (float) yi0, getWidth(), (float) yi0, mPaint);

		double xi0 = (0 - xMin) * this.getWidth() / (xMax - xMin);
		canvas.drawLine((float) xi0, 0, (float) xi0, getHeight(), mPaint);

		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(3);

		for (double xi = 0; xi < this.getWidth(); xi++) {

			double x1 = xMin + 1.0 * xi * (xMax - xMin) / this.getWidth();
			double y1 = 0;
			try {
				y1 = reversePolishNotation.cackulation(x1);
			} catch (ArithmeticException e) {
				continue;
			}
			double x2 = xMin + 1.0 * (xi + 1.0) * (xMax - xMin) / this.getWidth();
			double y2 = 0;
			try {
				y2 = reversePolishNotation.cackulation(x2);
			} catch (ArithmeticException e) {
				continue;
			}

			double yi1 = 1.0 * this.getHeight() - 1.0 * (y1 - yMin) * this.getHeight() / (yMax - yMin);
			double yi2 = 1.0 * this.getHeight() - 1.0 * (y2 - yMin) * this.getHeight() / (yMax - yMin);

			canvas.drawLine((float) xi, (float) yi1, (float) (xi + 1.0), (float) yi2, mPaint);

		}

	}

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
			/*
			 * Toast toast = Toast.makeText(this.getContext(),
			 * "Пора покормить кота!", Toast.LENGTH_SHORT); toast.show();
			 */

			float x = event.getX();
			float y = event.getY();
			// -----------scale----------------
			if ((pointerCount >= 2) && (lastPointerCount > 1)) {
				float dtY = Math.abs(event.getY(0) - event.getY(1));
				float dtX = Math.abs(event.getX(0) - event.getX(1));

				if (dtX - lastDtX != 0) {

					double step = 1.0 * (lastDtX - dtX) * (xMax - xMin) / getWidth();
					xMin = xMin - step / 2;
					xMax = xMax + step / 2;
					invalidate();

				}
				if (dtY - lastDtY != 0) {

					double step = 1.0 * (lastDtY - dtY) * (yMax - yMin) / getHeight();
					yMin = yMin - step / 2;
					yMax = yMax + step / 2;
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
				invalidate();

			}
			if ((Math.abs(yTouchOld - y) > 0) && (yTouchOld > 0)) {
				step = 1.0 * (yTouchOld - y) * (yMax - yMin) / getHeight();
				yMin = yMin - step;
				yMax = yMax - step;

				invalidate();

			}
			xTouchOld = x;
			yTouchOld = y;
			// sb.setLength(0);
			/*
			 * for (int i = 0; i < 10; i++) { sb.append("Index = " + i); if (i <
			 * pointerCount) { sb.append(", ID = " + event.getPointerId(i));
			 * sb.append(", X = " + event.getX(i)); sb.append(", Y = " +
			 * event.getY(i)); } else { sb.append(", ID = ");
			 * sb.append(", X = "); sb.append(", Y = "); } sb.append("\r\n"); }
			 */
			break;
		}

		/*
		 * result = "down: " + downPI + "\n" + "up: " + upPI + "\n";
		 * 
		 * if (inTouch) { result += "pointerCount = " + pointerCount + "\n" +
		 * sb.toString(); } tv.setText(result);
		 */
		return true;// super.onTouchEvent(event);
	}

}
