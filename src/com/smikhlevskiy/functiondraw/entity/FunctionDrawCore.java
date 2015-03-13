package com.smikhlevskiy.functiondraw.entity;

import com.smikhlevskiy.functiondraw.model.ReversePolishNotation;

public class FunctionDrawCore {
 
	
 private ReversePolishNotation formula;
 
 
public void compileFormula(String textFormula){	
	formula=new ReversePolishNotation();
	formula.setFormula(textFormula);
	formula.compile();
	
}

}
