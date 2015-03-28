package com.smikhlevskiy.formuladraw.model;

import java.util.ArrayList;

import com.smikhlevskiy.formuladraw.util.FDConstants;

enum TypeLex {
	OPERATOR, OPEN_BRACE, CLOSE_BRACE, DIGITAL, FUNCTION
};

public class ReversePolishNotation {
	private String textFormula;
	private char[] charFormula;
	private static char[] operators = { '.', '+', '-', '*', '/', '(', ')' };

	private boolean isOperator(char symbol) {
		for (int i = 0; i < operators.length; i++) {
			if (operators[i] == symbol)
				return true;
		}
		return false;
	}

	private boolean isDigital(char symbol) {

		if (((symbol >= '1') && (symbol <= '9')) || (symbol == '.') || (symbol == '0'))
			return true;
		else
			return false;
	}

	private boolean isFunction(char symbol) {
		if (((symbol >= 'a') && (symbol <= 'z')) || (symbol == '_'))
			return true;
		else
			return false;
	}

	private ArrayList<Lexschema> lex;
	private ArrayList<Lexschema> lexPolish;

	private class Lexschema {
		public TypeLex typ;
		public FDConstants.TypeFunction typeFunction;
		public char operator;
		double value;

		public Lexschema(TypeLex typ) {
			super();
			this.typ = typ;
			this.operator = ' ';
		}

		public Lexschema(TypeLex typ, double value) {
			super();
			this.typ = typ;
			this.value = value;
			this.operator = ' ';
		}

		public Lexschema(TypeLex typ, FDConstants.TypeFunction typeFunction) {
			super();
			this.typ = typ;
			this.typeFunction = typeFunction;
			this.operator = ' ';
		}

		public Lexschema(TypeLex typ, char operator) {
			super();
			this.typ = typ;
			this.operator = operator;
		}

	}

	public FDConstants.TypeFunction textToFunction(String text) {

		for (int i = 0; i < FDConstants.AVAILABLE_FUNCTIONS.length; i++)
			if (text.equals(FDConstants.AVAILABLE_FUNCTIONS[i][0]))
				return FDConstants.TypeFunction.values()[i];

		return FDConstants.TypeFunction.ERROR;		

	}

	// --------------------------Проверка текста формулы---------------------
	private void controlTextFormula() {

	}

	// ------------------------------------Разбиваем текст формулы на Лексхемы--
	private void divideLexschemas() {
		lex = new ArrayList();
		lex.add(new Lexschema(TypeLex.OPEN_BRACE));
		int i = 0;
		while (i < charFormula.length) {

			String digital = "";
			String function;
			// --------------Открывающеяся скобка--------
			if (charFormula[i] == '(') {
				lex.add(new Lexschema(TypeLex.OPEN_BRACE));
				i++;
				continue;
			}
			// --------------Закрывающеяся скобка--------
			if (charFormula[i] == ')') {
				lex.add(new Lexschema(TypeLex.CLOSE_BRACE));
				i++;
				continue;
			}
			// --------------Оператор--------
			if (isOperator(charFormula[i])) {
				lex.add(new Lexschema(TypeLex.OPERATOR, charFormula[i]));
				i++;
				continue;
			}

			// --------------Число--------------
			digital = "";
			while ((i < charFormula.length) && isDigital(charFormula[i])) {
				digital = digital + charFormula[i];
				i++;
			}

			if (digital.length() > 0) {

				lex.add(new Lexschema(TypeLex.DIGITAL, new Double(digital)));

				continue;

			}

			// ------------Функция--------
			function = "";
			while ((i < charFormula.length) && isFunction(charFormula[i])) {
				function = function + charFormula[i];
				i++;
			}
			if (function.length() > 0) {
				lex.add(new Lexschema(TypeLex.FUNCTION, textToFunction(function)));
				continue;
			}

			return;// Error недопустимый символ

		}

		lex.add(new Lexschema(TypeLex.CLOSE_BRACE));

	}

	// --------------------------Контроль совместимости лексхем----------------
	private void controlCopabilityLexschemas() {

	}

	// -------------------Компиляция лексхем в обратно-польскую запись----------
	public void makePolish() {

		int i = 0;

		ArrayList<Lexschema> lexStack = new ArrayList();
		lexPolish = new ArrayList();

		while (i < lex.size()) {
			if ((lex.get(i).typ == TypeLex.OPEN_BRACE) || (lex.get(i).typ == TypeLex.FUNCTION)) {
				lexStack.add(lex.get(i));
				i++;
				continue;
			}
			if ((lex.get(i).typ == TypeLex.OPERATOR) && ((lex.get(i).operator == '+') || (lex.get(i).operator == '-'))) {

				while (lexStack.get(lexStack.size() - 1).typ != TypeLex.OPEN_BRACE) {
					lexPolish.add(lexStack.get(lexStack.size() - 1));
					lexStack.remove(lexStack.size() - 1);
				}
				lexStack.add(lex.get(i));
				i++;
				continue;

			}
			if ((lex.get(i).typ == TypeLex.OPERATOR) && ((lex.get(i).operator == '*') || (lex.get(i).operator == '/'))) {

				while ((lexStack.get(lexStack.size() - 1).typ != TypeLex.OPEN_BRACE)

				&& (lexStack.get(lexStack.size() - 1).operator != '+')

				&& (lexStack.get(lexStack.size() - 1).operator != '-')) {
					lexPolish.add(lexStack.get(lexStack.size() - 1));
					lexStack.remove(lexStack.size() - 1);
				}
				lexStack.add(lex.get(i));
				i++;
				continue;

			}
			if (lex.get(i).typ == TypeLex.CLOSE_BRACE) {

				while (lexStack.get(lexStack.size() - 1).typ != TypeLex.OPEN_BRACE) {
					lexPolish.add(lexStack.get(lexStack.size() - 1));
					lexStack.remove(lexStack.size() - 1);
				}
				lexStack.remove(lexStack.size() - 1);
				i++;
				continue;
			}

			lexPolish.add(lex.get(i));
			i++;

		}

	}

	/*
	 * Compile textFormula to Reverse Polish Notation & Find Error
	 */
	public int compile() {
		controlTextFormula();
		divideLexschemas();
		controlCopabilityLexschemas();
		makePolish();

		return 0;

	}

	// ---------------Calckulation f(x)------------------------
	public double cackulation(double x) {
		try {

			if (lexPolish == null)
				return 0;

			ArrayList<Double> valueStack = new ArrayList();

			for (int i = 0; i < lexPolish.size(); i++) {

				if (lexPolish.get(i).typ == TypeLex.DIGITAL) {
					valueStack.add(new Double(lexPolish.get(i).value));
					continue;
				}
				if (lexPolish.get(i).typ == TypeLex.FUNCTION) {


					switch (lexPolish.get(i).typeFunction) {
					case XVALUE:
						valueStack.add(new Double(x));
						continue;
					case Pi:
						valueStack.add(Math.PI);
						continue;
					case E:
						valueStack.add(Math.E);
						continue;

					case abs:
						valueStack.set(valueStack.size() - 1, Math.abs(valueStack.get(valueStack.size() - 1)));
						break;
					case asin:
						valueStack.set(valueStack.size() - 1, Math.asin(valueStack.get(valueStack.size() - 1)));
						break;
					case acos:
						valueStack.set(valueStack.size() - 1, Math.acos(valueStack.get(valueStack.size() - 1)));
						break;
					case atan:
						valueStack.set(valueStack.size() - 1, Math.atan(valueStack.get(valueStack.size() - 1)));
						break;
					case cbrt:
						valueStack.set(valueStack.size() - 1, Math.cbrt(valueStack.get(valueStack.size() - 1)));
						break;						
					case cos:
						valueStack.set(valueStack.size() - 1, Math.cos(valueStack.get(valueStack.size() - 1)));
						break;						
					case exp:
						valueStack.set(valueStack.size() - 1, Math.exp(valueStack.get(valueStack.size() - 1)));
						break;
					case log:
						valueStack.set(valueStack.size() - 1, Math.log(valueStack.get(valueStack.size() - 1)));
						break;						
					case log10:
						valueStack.set(valueStack.size() - 1, Math.atan(valueStack.get(valueStack.size() - 1)));
						break;
					case pow:
						valueStack.set(valueStack.size() - 1, Math.pow(valueStack.get(valueStack.size() - 1),2));
						break;						
					case random:
						valueStack.set(valueStack.size() - 1, valueStack.get(valueStack.size() - 1)*Math.random());
						break;
					case sin:
						valueStack.set(valueStack.size() - 1, Math.sin(valueStack.get(valueStack.size() - 1)));
						break;
					case sqrt:
						valueStack.set(valueStack.size() - 1, Math.sqrt(valueStack.get(valueStack.size() - 1)));
						break;
					case tan:
						valueStack.set(valueStack.size() - 1, Math.tan(valueStack.get(valueStack.size() - 1)));
						break;						
						
						
						

					

					default:
						throw (new ArithmeticException());// unknown function
					}
				}

				if (lexPolish.get(i).typ == TypeLex.OPERATOR) {
					switch (lexPolish.get(i).operator) {
					case '/':
						if (valueStack.get(valueStack.size() - 1) == 0.0)
							throw new ArithmeticException();

						valueStack.set(valueStack.size() - 2, new Double(valueStack.get(valueStack.size() - 2)
								/ valueStack.get(valueStack.size() - 1)));

						break;
					case '*':
						valueStack.set(valueStack.size() - 2, new Double(valueStack.get(valueStack.size() - 2)
								* valueStack.get(valueStack.size() - 1)));
						break;
					case '+':
						valueStack.set(valueStack.size() - 2, new Double(valueStack.get(valueStack.size() - 2)
								+ valueStack.get(valueStack.size() - 1)));
						break;
					case '-':
						valueStack.set(valueStack.size() - 2, new Double(valueStack.get(valueStack.size() - 2)
								- valueStack.get(valueStack.size() - 1)));
						break;

					default:
						break;

					}
					valueStack.remove(valueStack.size() - 1);
					continue;
				}

			}
			if (valueStack.size() > 0)
				return valueStack.get(valueStack.size() - 1);

			return 0;// error
		} catch (ArithmeticException e) {

			throw e;
		}

	}

	public String getFormula() {
		return textFormula;
	}

	public void setFormula(String textFormula) {
		this.textFormula = textFormula.replace(" ", "").toLowerCase();
		charFormula = this.textFormula.toCharArray();
	}

}
