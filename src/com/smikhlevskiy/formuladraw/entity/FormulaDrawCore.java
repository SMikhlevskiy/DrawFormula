package com.smikhlevskiy.formuladraw.entity;

import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.ui.GraphicView;

public class FormulaDrawCore {
 
	
public void drawGraphic(GraphicView graphicView,String textFormula,double xStart,double xEnd){
	graphicView.setFormula(textFormula);
	graphicView.setxMinMax(xStart,xEnd);	
	
	graphicView.setDrawCustomCanvas(true);
	graphicView.invalidate(); 
}
 
 

public double cakulationFormula(String textFormula,double x){
	ReversePolishNotation reversePolishNotation=new ReversePolishNotation();
	reversePolishNotation.setFormula(textFormula);
	reversePolishNotation.compile();
	return reversePolishNotation.cackulation(x);
	
}


}
