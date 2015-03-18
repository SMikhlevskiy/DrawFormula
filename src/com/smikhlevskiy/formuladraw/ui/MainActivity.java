package com.smikhlevskiy.formuladraw.ui;


import com.smikhlevskiy.formuladraw.ui.UserRegActivity;
import com.parse.ParseUser;
import com.smikhlevskiy.formuladraw.entity.FormulaDrawController;
import com.smikhlevskiy.formuladraw.model.FindRoot;
import com.smikhlevskiy.formuladraw.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private Button buttonCalck;
	private Button buttonDraw;
	private Button buttonRoot;
	public Button buttonUserReg;
	private EditText editTextFunction;
	// private EditText editTextX;
	private EditText editTextXStart;
	private EditText editTextXEnd;
	private TextView textViewResult;
	private TextView textViewRegUser;
	public GraphicView graphicView;

	private void drawUserInfo() {

		if (ParseUser.getCurrentUser() == null) {
			buttonUserReg.setText(getString(R.string.registration));
			textViewRegUser.setText(getString(R.string.notRegistredUser));
		} else {
			textViewRegUser.setText(getString(R.string.user)
					+ ParseUser.getCurrentUser().getUsername());
			buttonUserReg.setText(getString(R.string.logOut));
		}
		;

	}

	private FormulaDrawController formulaDrawController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		formulaDrawController = new FormulaDrawController();

		buttonCalck = (Button) findViewById(R.id.buttonCalck);
		buttonDraw = (Button) findViewById(R.id.buttonDraw);
		buttonRoot = (Button) findViewById(R.id.buttonRoot);
		buttonUserReg = (Button) findViewById(R.id.buttonRegistration);

		editTextFunction = (EditText) findViewById(R.id.editTextFormula);
		textViewResult = (TextView) findViewById(R.id.textViewResult);
		textViewRegUser =(TextView) findViewById(R.id.textViewRegUser);
		editTextXStart = (EditText) findViewById(R.id.editTextXstart);
		editTextXEnd = (EditText) findViewById(R.id.editTextXend);
		graphicView = (GraphicView) findViewById(R.id.graphicView);

		buttonRoot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FindRoot findRoot = new FindRoot(getApplicationContext(), editTextFunction.getText().toString(),
						new Double(editTextXStart.getText().toString()), new Double(editTextXEnd.getText().toString()),
						(double) 0.01);

			}
		});

		/* -----Button User Registration form Set On ClickListener---------- */
		buttonUserReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ParseUser.getCurrentUser() == null) {
					startActivity(new Intent(MainActivity.this, UserRegActivity.class));
				} else {
					ParseUser.logOut();
					drawUserInfo();
				}

			}

		});

		// --------------------------------------------
		buttonDraw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				formulaDrawController.drawGraphic(graphicView, editTextFunction.getText().toString(), new Double(
						editTextXStart.getText().toString()), new Double(editTextXEnd.getText().toString()));

			}

		});

		buttonCalck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					textViewResult.setText(new Double(formulaDrawController.cakulator(editTextFunction.getText().toString(), /* xVal */
							0.0)).toString());
				} catch (ArithmeticException e) {
					Toast.makeText(getApplicationContext(), getString(R.string.mathError), Toast.LENGTH_LONG).show();
					textViewResult.setText("");
				}

			}

		});
		drawUserInfo();

		formulaDrawController.drawGraphic(graphicView, /*editTextFunction.getText().toString()*/"X", -10,10);

	}
	@Override
	protected void onResume() {
		drawUserInfo();// Draw user info after closing Registration form
		super.onResume();
	}

}
