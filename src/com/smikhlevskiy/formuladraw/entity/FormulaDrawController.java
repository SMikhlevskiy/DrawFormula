package com.smikhlevskiy.formuladraw.entity;

import android.content.Context;
import android.widget.Toast;

import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.ui.FormulaDrawBaseData;
import com.smikhlevskiy.formuladraw.ui.GraphicView;

public class FormulaDrawController {
	private FormulaDrawBaseData formulaDrawBaseData;

	public FormulaDrawController(Context context) {
		super();
		FormulaDrawBaseData formulaDrawBaseData = new FormulaDrawBaseData(context);

		// TODO Auto-generated constructor stub
	}

	public void drawGraphic(GraphicView graphicView, String textFormula, double xStart, double xEnd) {
		ReversePolishNotation reversePolishNotation = new ReversePolishNotation();
		reversePolishNotation.setFormula(textFormula);

		reversePolishNotation.compile();

		graphicView.setReversePolishNotation(reversePolishNotation);
		graphicView.setxMinMax(xStart, xEnd);
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

	public void saveFormula(String formula) {
		//formulaDrawBaseData.saveFormula(formula);
	}

	public String getFormula() {
		//return formulaDrawBaseData.getFormula();
		return "1/x";
	}

}
