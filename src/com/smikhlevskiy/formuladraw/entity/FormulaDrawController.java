package com.smikhlevskiy.formuladraw.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.smikhlevskiy.formuladraw.R;
import com.smikhlevskiy.formuladraw.model.FormulaDrawBaseData;
import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.ui.GraphicView;
import com.smikhlevskiy.formuladraw.util.FDConstants;

public class FormulaDrawController {

	private String[] formulas = new String[FDConstants.colorSpinnerLines.length];
	private Context context;
	private Handler outHandler;
	private SharedPreferences formulaDrawPreferences;
	private int currentLine = 0;

	private FormulaDrawBaseData formulaDrawBaseData;

	public FormulaDrawController(Context context, Handler outHandler) {
		super();
		this.context = context;
		this.outHandler = outHandler;
		FormulaDrawBaseData formulaDrawBaseData = new FormulaDrawBaseData(context);// To-DO

		// TODO Auto-generated constructor stub
	}

	public void drawGraphic(GraphicView graphicView, String textFormula, double xStart, double xEnd) {

		if (xStart >= xEnd) {
			Message message = outHandler.obtainMessage();
			message.what = FDConstants.OUT_TEXT_ERROR_MESSAGE;
			message.obj = context.getString(R.string.errorDiapazon);
			outHandler.sendMessage(message);
			return;
		}

		ReversePolishNotation reversePolishNotation[] = new ReversePolishNotation[FDConstants.colorSpinnerLines.length];
		for (int i = 0; i < FDConstants.colorSpinnerLines.length; i++) {
			reversePolishNotation[i] = new ReversePolishNotation();
			reversePolishNotation[i].setFormula(getFormulas(i));
			reversePolishNotation[i].compile();
		}

		graphicView.setReversePolishNotation(reversePolishNotation);
		graphicView.setMinMax(xStart, xEnd);
		graphicView.setDrawCustomCanvas(true);
		graphicView.invalidate();
	}

	public void cakulator(String textFormula, double x) {
		ReversePolishNotation reversePolishNotation = new ReversePolishNotation();
		reversePolishNotation.setFormula(textFormula);
		reversePolishNotation.compile();

		Message message = outHandler.obtainMessage();

		try {
			double val = reversePolishNotation.calculation(x);

			message.obj = new String("f(" + x + ")=" + val);
			message.what = FDConstants.OUT_TEXT_INFO_MESSAGE;
			outHandler.sendMessage(message);

		} catch (ArithmeticException e) {
			message.obj = new String(context.getString(R.string.mathError));
			message.what = FDConstants.OUT_TEXT_ERROR_MESSAGE;
			outHandler.sendMessage(message);

		}

	}

	/**
	 * Save state Workspace in Preferces: formulas and min/max
	 */
	public void saveWorkspace() {
		SharedPreferences.Editor editor = formulaDrawPreferences.edit();
		for (int i = 0; i < FDConstants.colorSpinnerLines.length; i++)
			editor.putString(FDConstants.APP_PREFERENCES_TEXTFormula + i, getFormulas(i));
		editor.commit();
	}

	/**
	 * Read Prefereces
	 */
	public void readPreferences() {
		formulaDrawPreferences = context.getSharedPreferences(FDConstants.APP_PREFERENCES, context.MODE_PRIVATE);
		for (int i = 0; i < FDConstants.colorSpinnerLines.length; i++) {
			String s = "";
			if (i == 0)
				s = "x*x+x";

			setFormulas(formulaDrawPreferences.getString(FDConstants.APP_PREFERENCES_TEXTFormula + i, s), i);

			// formulaDrawController.setFormulas("",i);
		}
	}

	/**
	 * 
	 * 
	 */
	public void saveUserFunction(String name, String textFunction) {
		ParseUser user = ParseUser.getCurrentUser();
		ParseObject post = new ParseObject("UserDatas");
		post.put("formula", textFunction);
		post.put("name", name);
		post.put("user", user);
		post.saveInBackground();
	}

	public int getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(int currentLine) {
		this.currentLine = currentLine;
	}

	public String getFormulas(int i) {
		return formulas[i];
	}

	public void setFormulas(String formula, int i) {
		this.formulas[i] = formula;
	}

}
