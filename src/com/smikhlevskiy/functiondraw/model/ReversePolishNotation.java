package com.smikhlevskiy.functiondraw.model;

import java.util.ArrayList;

enum TypeLex {
	OPERATOR, OPEN_BRACE, CLOSE_BRACE, DIGITAL, FUNCTION
};

enum TypeFunction {
	SIN, COS, XVALUE, ERROR
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
		public TypeFunction typeFunction;
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

		public Lexschema(TypeLex typ, TypeFunction typeFunction) {
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

	private TypeFunction textToFunction(String text) {
		if (text.equals("sin"))
			return TypeFunction.SIN;
		if (text.equals("cos"))
			return TypeFunction.COS;
		if (text.equals("x"))
			return TypeFunction.XVALUE;

		return TypeFunction.ERROR;
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

	// ------------------------------------------------------------------------
	public int compile() {
		controlTextFormula();
		divideLexschemas();
		controlCopabilityLexschemas();
		makePolish();

		return 0;

	}

	// ---------------Вычисление значения------------------------
	public double cackulation(double x) {
		if (lexPolish == null)
			return 0;

		ArrayList<Double> valStack = new ArrayList();

		for (int i = 0; i < lexPolish.size(); i++) {

			if (lexPolish.get(i).typ == TypeLex.DIGITAL) {
				valStack.add(new Double(lexPolish.get(i).value));
				continue;
			}
			if (lexPolish.get(i).typ == TypeLex.FUNCTION) {

				switch (lexPolish.get(i).typeFunction) {
				case SIN:
					valStack.set(valStack.size() - 1, Math.sin(valStack.get(valStack.size() - 1)));
					break;
				case COS:
					valStack.set(valStack.size() - 1, Math.cos(valStack.get(valStack.size() - 1)));

					break;
				case XVALUE:
					valStack.add(new Double(x));
					continue;				
					

				default:					
					break;// eeror
				}
			}

			if (lexPolish.get(i).typ == TypeLex.OPERATOR) {
				switch (lexPolish.get(i).operator) {
				case '/':
					valStack.set(valStack.size() - 2,
							new Double(valStack.get(valStack.size() - 2) / valStack.get(valStack.size() - 1)));

					break;
				case '*':
					valStack.set(valStack.size() - 2,
							new Double(valStack.get(valStack.size() - 2) * valStack.get(valStack.size() - 1)));
					break;
				case '+':
					valStack.set(valStack.size() - 2,
							new Double(valStack.get(valStack.size() - 2) + valStack.get(valStack.size() - 1)));
					break;
				case '-':
					valStack.set(valStack.size() - 2,
							new Double(valStack.get(valStack.size() - 2) - valStack.get(valStack.size() - 1)));
					break;

				default:
					break;

				}
				valStack.remove(valStack.size() - 1);
				continue;
			}

		}
		if (valStack.size() > 0)
			return valStack.get(valStack.size() - 1);

		return 0;// error

	}

	public String getFormula() {
		return textFormula;
	}

	public void setFormula(String textFormula) {
		this.textFormula = textFormula;
		charFormula = textFormula.toCharArray();
	}

}
