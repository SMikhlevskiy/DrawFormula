package com.smikhlevskiy.formuladraw.model;
/*
 * To do
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;

public class FormulaDrawBaseData {
	private SharedPreferences formulaDrawPreferences;
	public static final String APP_PREFERENCES = "formuladrawpreferences";
	public static final String APP_PREFERENCES_TEXTFormula = "formula";
	Context context;

	public FormulaDrawBaseData(Context context) {
		super();
		this.context=context;
		/*
		Parse.enableLocalDatastore(context);
	    Parse.initialize(context, "49TMtDqnUx9cWNssiQfrK8SZS1dkQIR2ai3iH4vZ", "eQ9MvhA8hMcORp4dceFRJkNtHXGK7PYCDq7DjflY");
	    */
	    
	    //formulaDrawPreferences = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);

	}
	public String getFormula(){
		/*
		formulaDrawPreferences = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
		return formulaDrawPreferences.getString(APP_PREFERENCES_TEXTFormula, "x*x+x");
		*/
		return "";
		
	}
		
	public void saveFormula(String formula){
		/*
		SharedPreferences.Editor editor = formulaDrawPreferences.edit();
		editor.putString(APP_PREFERENCES_TEXTFormula, formula);
		editor.commit();
		*/
	}


}
