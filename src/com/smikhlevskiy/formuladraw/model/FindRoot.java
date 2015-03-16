package com.smikhlevskiy.formuladraw.model;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FindRoot {
Context context;
MyAsynkTask mt;
	public FindRoot(Context context,String textFormula, double xStart, double xEnd, double eps) {
		this.context=context;
		
		mt = new MyAsynkTask();
	    mt.execute();
		/*
		ReversePolishNotation reversePolishNotation = new ReversePolishNotation();
		reversePolishNotation.setFormula(textFormula);
		reversePolishNotation.compile();
		while (true) {
			Double yStart = reversePolishNotation.cackulation(xStart);
			Double yEnd = reversePolishNotation.cackulation(xEnd);

			if (Math.abs(yStart) < eps) {
				return;
			}
		}
		*/

	}
	
	
	  class MyAsynkTask extends AsyncTask<Void, Void, Void> {

		    @Override
		    protected void onPreExecute() {
		      super.onPreExecute();
		      
		    }

		    @Override
		    protected Void doInBackground(Void... params) {
		      try {
		        TimeUnit.SECONDS.sleep(2);
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      }
		      return null;
		    }

		    @Override
		    protected void onPostExecute(Void result) {
		      super.onPostExecute(result);
		      Toast.makeText(context, "Корень не найден",
		    		   Toast.LENGTH_LONG).show();
		    }
		  }

}
