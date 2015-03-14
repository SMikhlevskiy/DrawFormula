package com.smikhlevskiy.formuladraw.ui;

import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class GraphicView extends View {
	private ReversePolishNotation reversePolishNotation = null;
	private Double xMin = 0.0;
	private Double xMax = 0.0;
	private Double yMin = 0.0;
	private Double yMax = 0.0;

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

	public void setFormula(String text) {
		reversePolishNotation = new ReversePolishNotation();
		reversePolishNotation.setFormula(text);
		reversePolishNotation.compile();

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
			double y = reversePolishNotation.cackulation(x);

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
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(3);
		
		for (int xi = 0; xi < this.getWidth(); xi++) {

			double x1 = xMin + 1.0 * xi * (xMax - xMin) / this.getWidth();
			double y1 = reversePolishNotation.cackulation(x1);
			double x2 = xMin + 1.0 * (xi+1) * (xMax - xMin) / this.getWidth();
			double y2 = reversePolishNotation.cackulation(x2);
			
			double yi1=this.getHeight()-y1*this.getHeight()/(yMax-yMin);
			double yi2=this.getHeight()-y2*this.getHeight()/(yMax-yMin);
			
			canvas.drawLine(xi,Math.round(yi1),xi+1,Math.round(yi2),mPaint);

			
		}

	}

}
