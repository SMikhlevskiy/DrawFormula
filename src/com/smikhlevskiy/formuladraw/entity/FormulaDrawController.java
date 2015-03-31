package com.smikhlevskiy.formuladraw.entity;

import android.content.Context;
import android.widget.Toast;

import com.smikhlevskiy.formuladraw.model.FormulaDrawBaseData;
import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.ui.GraphicView;
import com.smikhlevskiy.formuladraw.util.FDConstants;

public class FormulaDrawController {

	private String[] formulas = new String[FDConstants.colorSpinnerLines.length];
	private int currentLine = 0;

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

	private FormulaDrawBaseData formulaDrawBaseData;

	public FormulaDrawController(Context context) {
		super();
		FormulaDrawBaseData formulaDrawBaseData = new FormulaDrawBaseData(context);

		// TODO Auto-generated constructor stub
	}

	public void drawGraphic(GraphicView graphicView, String textFormula, double xStart, double xEnd) {
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

	public double cakulator(String textFormula, double x) {
		ReversePolishNotation reversePolishNotation = new ReversePolishNotation();
		reversePolishNotation.setFormula(textFormula);
		reversePolishNotation.compile();
		try {
			return reversePolishNotation.cackulation(x);
		} catch (ArithmeticException e) {
			throw e;

		}

	}
	/*
	 * public void saveFormula(String formula) {
	 * //formulaDrawBaseData.saveFormula(formula); }
	 * 
	 * public String getFormula() { //return formulaDrawBaseData.getFormula();
	 * return "1/x"; }
	 */
}
