package smikhlevskiy.formuladraw.model;
/*
 * Utility for work with formulas
 * Calculate Integral & Find Root
 */
import java.util.concurrent.TimeUnit;

import smikhlevskiy.formuladraw.R;
import smikhlevskiy.formuladraw.util.FDConstants;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MathUtility {
	private Context context;
	private String textFormula;
	private double xStart;
	private double xEnd;
	private double eps;
	private int numParse;
	private Handler outHandler;
	private MyAsynkTask mt;
	/*
	 * 
	 */
	public MathUtility(Context context,Handler outHandler,String textFormula){
		this.context=context;
		this.outHandler=outHandler;
		this.textFormula=textFormula;
		
	}

	/*
	 * calculate Integral and send message to MainAcivity
	 */
	public void calcIntegral(double xStart, double xEnd, int numParse) {
		if (xStart >= xEnd) {
			Message message = outHandler.obtainMessage();
			message.what = FDConstants.OUT_TEXT_ERROR_MESSAGE;
			message.obj = context.getString(R.string.errorDiapazon);
			outHandler.sendMessage(message);
			return;
		}

		this.xStart = xStart;
		this.xEnd = xEnd;
		this.numParse = numParse;
		double step = 1.0 * (xEnd - xStart) / numParse;

		ReversePolishNotation rpn = new ReversePolishNotation();
		rpn.setFormula(textFormula);
		rpn.compile();

		double sum = 0;
		for (int i = 0; i < numParse; i++) {
			double x = xStart + i * step;
			try {
				sum = sum + step * (rpn.calculation(x) + rpn.calculation(x + step)) / 2;
			} catch (ArithmeticException e) {
				Message message = outHandler.obtainMessage();
				message.obj = new String(context.getString(R.string.mathError));
				message.what = FDConstants.OUT_TEXT_ERROR_MESSAGE;
				outHandler.sendMessage(message);
				return;
			}

		}

		Message message = outHandler.obtainMessage();
		message.what = FDConstants.OUT_TEXT_INFO_MESSAGE;
		message.obj = new String(context.getString(R.string.integral) + sum);
		outHandler.sendMessage(message);

	}
	/*
	 * find Root in AsyncTask and send message to MainAcivity 
	 *  
	 */

	public void findRoot(double xStart, double xEnd, double eps) {
		if (xStart >= xEnd) {
			Message message = outHandler.obtainMessage();
			message.what = FDConstants.OUT_TEXT_ERROR_MESSAGE;
			message.obj = context.getString(R.string.errorDiapazon);
			outHandler.sendMessage(message);
			return;
		}
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
				try {
					y1 = rpn.calculation(x1);
					y2 = rpn.calculation(x2);
				} catch (ArithmeticException e) {
					return null;
				}

				if (Math.abs(y1) < eps)
					return y1;
				if (Math.abs(y2) < eps)
					return y2;

				double x = x1 + (x2 - x1) / 2;
				double y;
				try {
					y = rpn.calculation(x);
				} catch (ArithmeticException e) {
					return null;
				}

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
			Message message = outHandler.obtainMessage();
			if (result == null) {
				message.what = FDConstants.OUT_TEXT_ERROR_MESSAGE;
				message.obj = context.getString(R.string.rootBad);
			} else {
				message.what = FDConstants.OUT_TEXT_INFO_MESSAGE;
				message.obj = context.getString(R.string.rootOk) + result;
			}

			outHandler.sendMessage(message);
		}
	}

	
}
