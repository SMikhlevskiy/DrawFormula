package com.smikhlevskiy.formuladraw.ui;

import com.smikhlevskiy.formuladraw.entity.FormulaDrawCore;
import com.smikhlevskiy.functiondraw.R;

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

public class MainActivity extends ActionBarActivity {
	private Button buttonCalck;
	private Button buttonDraw;
	private EditText editTextFunction;
	private EditText editTextX;
	private EditText editTextXStart;
	private EditText editTextXEnd;
	private TextView textViewResult;
	public GraphicView graphicView;

	private FormulaDrawCore formulaDrawCore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		formulaDrawCore = new FormulaDrawCore();

		buttonCalck = (Button) findViewById(R.id.buttonCalck);
		buttonDraw = (Button) findViewById(R.id.buttonDraw);

		editTextFunction = (EditText) findViewById(R.id.editTextFormula);
		textViewResult = (TextView) findViewById(R.id.textViewResult);
		editTextX = (EditText) findViewById(R.id.editTextX);
		editTextXStart = (EditText) findViewById(R.id.editTextXstart);
		editTextXEnd = (EditText) findViewById(R.id.editTextXend);
		graphicView = (GraphicView) findViewById(R.id.graphicView);
		
		// --------------------------------------------
		buttonDraw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				formulaDrawCore.drawGraphic(graphicView,
				 editTextFunction.getText().toString(), new Double(
						editTextXStart.getText().toString()), new Double(editTextXEnd.getText().toString()));				
				
			}

		});

		buttonCalck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				double xVal = new Double(editTextX.getText().toString());
				textViewResult.setText(new Double(formulaDrawCore.cakulationFormula(editTextFunction.getText()
						.toString(), xVal)).toString());

				// functionDrawCore.compileFormula(editTextFunction.getText().toString());
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
