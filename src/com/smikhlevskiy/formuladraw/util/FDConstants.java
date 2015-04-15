package com.smikhlevskiy.formuladraw.util;

import android.graphics.Color;

public abstract class FDConstants {

	// ------------Main Handler messages-----
	public static final int OUT_TEXT_INFO_MESSAGE = 1;
	public static final int OUT_TEXT_ERROR_MESSAGE = 2;
	public static final int CHANGE_XLIMITS = 3;
	// ------------type SelectFormula--------

	public static final int LOAD_FORMULA = 1;
	public static final int ADD_FUNCTION = 2;
	public static final int ADD_USER_FUNCTION = 3;

	// -------------save Workspace------------
	//
	public static final String APP_PREFERENCES = "fdpreferences";
	public static final String APP_PREFERENCES_TEXTFormula = "formula";
	public static final String APP_PREFERENCES_dYdT = "dYdT";

	// ---------------functions--------------------------
	public static final String[][] AVAILABLE_FUNCTIONS = { { "abs", "модуль" }, { "asin", "арксинус" },
			{ "acos", "аркосинус" }, { "atan", "арктангенс" }, { "cbrt", "кубический корень" }, { "cos", "косинус" },
			{ "exp", "експонента в степени Х" }, { "log", "логорифм" }, { "log10", "10 тичный логорифм" },
			{ "pow", "возьведение в квадрат" }, { "random", "случайное значение от 0 до X" }, { "sin", "синус" },
			{ "sqrt", "корень" }, { "tan", "тангенс" }, { "pi", "число пи" }, { "e", "экспонента" },
			{ "x", "аргумент" } };

	public enum TypeFunction {

		abs, asin, acos, atan, cbrt, cos, exp, log, log10, pow, random, sin, sqrt, tan, Pi, E,

		XVALUE, ERROR
	};

	public static int[] colorSpinnerLines = { Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA,
			Color.rgb(255, 128, 0), Color.BLACK };
}
