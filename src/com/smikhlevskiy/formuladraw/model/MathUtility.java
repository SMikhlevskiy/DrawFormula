package com.smikhlevskiy.formuladraw.model;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class MathUtility {
	private Context context;
	private String textFormula;
	private double xStart;
	private double xEnd;
	private double eps;
	private int numParse;

	private MyAsynkTask mt;

	public void calckIntegral(Context context, String textFormula, double xStart, double xEnd, int numParse) {
		this.context = context;
		this.textFormula = textFormula;
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.numParse = numParse;
		double step = 1.0*(xEnd-xStart) / numParse;
		

		ReversePolishNotation rpn = new ReversePolishNotation();
		rpn.setFormula(textFormula);
		rpn.compile();
		
		double sum = 0;
		for (int i=0;i<numParse;i++){
			double x=xStart+i*step;
			sum = sum + step * (rpn.cackulation(x) + rpn.cackulation(x + step)) / 2;
		}
		Toast.makeText(context, "Интеграл=" + sum, Toast.LENGTH_LONG).show();

	}

	public void findRoot(Context context, String textFormula, double xStart, double xEnd, double eps) {
		this.context = context;
		this.textFormula = textFormula;
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.eps = eps;

		mt = new MyAsynkTask();
		mt.execute();

	}

	class MyAsynkTask extends AsyncTask<Void, Void, Double> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Double doInBackground(Void... params) {
			// try {
			ReversePolishNotation rpn = new ReversePolishNotation();
			rpn.setFormula(textFormula);
			rpn.compile();
			double x1 = xStart;
			double x2 = xEnd;
			double y1;
			double y2;

			for (int i = 0; i <= 10000000; i++) {// Bounce protection
				y1 = rpn.cackulation(x1);
				y2 = rpn.cackulation(x2);
				if (Math.abs(y1) < eps)
					return y1;
				if (Math.abs(y2) < eps)
					return y2;

				double x = x1 + (x2 - x1) / 2;
				double y = rpn.cackulation(x);

				if (Math.abs(y) < eps)
					return x;

				if (Math.signum(y2) != Math.signum(y))
					x1 = x;
				else if (Math.signum(y1) != Math.signum(y))
					x2 = x;
				else
					return null;

			}

			// TimeUnit.SECONDS.sleep(2);
			/*
			 * } catch (InterruptedException e) { e.printStackTrace(); }
			 */
			return null;
		}

		@Override
		protected void onPostExecute(Double result) {
			super.onPostExecute(result);
			if (result == null)
				Toast.makeText(context, "Корень не найден", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context, "Корень найден. X=" + result, Toast.LENGTH_LONG).show();
		}
	}

}
