package com.smikhlevskiy.formuladraw.entity;

import android.widget.Toast;

import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.ui.GraphicView;

public class FormulaDrawController {
 
	
public void drawGraphic(GraphicView graphicView,String textFormula,double xStart,double xEnd){
	ReversePolishNotation reversePolishNotation=new ReversePolishNotation();
	reversePolishNotation.setFormula(textFormula);
	
	
	
	reversePolishNotation.compile();
	
	
	
	graphicView.setReversePolishNotation(reversePolishNotation);
	graphicView.setxMinMax(xStart,xEnd);	
	graphicView.setDrawCustomCanvas(true);
	graphicView.invalidate(); 
}
 
 

public double cakulator(String textFormula,double x){
	ReversePolishNotation reversePolishNotation=new ReversePolishNotation();
	reversePolishNotation.setFormula(textFormula);
	reversePolishNotation.compile();
	try {
	return reversePolishNotation.cackulation(x);
	}
	catch (ArithmeticException e) {
	   throw e;
	   
    
	     
   }
	
}


}
