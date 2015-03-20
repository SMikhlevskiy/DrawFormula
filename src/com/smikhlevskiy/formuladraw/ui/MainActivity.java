package com.smikhlevskiy.formuladraw.ui;

import com.smikhlevskiy.formuladraw.ui.UserRegActivity;
import com.parse.ParseUser;
import com.smikhlevskiy.formuladraw.entity.FormulaDrawController;
import com.smikhlevskiy.formuladraw.model.FindRoot;
import com.smikhlevskiy.formuladraw.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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
	private boolean prFirstFocus = true;
	private SharedPreferences formulaDrawPreferences;

	public static final String APP_PREFERENCES = "formuladrawpreferences";
	public static final String APP_PREFERENCES_TEXTFormula = "formula";

	private void drawUserInfo() {

		if (ParseUser.getCurrentUser() == null) {
			buttonUserReg.setText(getString(R.string.registration));
			textViewRegUser.setText(getString(R.string.notRegistredUser));
		} else {
			textViewRegUser.setText(getString(R.string.user) + ParseUser.getCurrentUser().getUsername());
			buttonUserReg.setText(getString(R.string.logOut));
		}
		;

	}

	private FormulaDrawController formulaDrawController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		formulaDrawController = new FormulaDrawController(this);

		buttonCalck = (Button) findViewById(R.id.buttonCalck);
		buttonDraw = (Button) findViewById(R.id.buttonDraw);
		buttonRoot = (Button) findViewById(R.id.buttonRoot);
		buttonUserReg = (Button) findViewById(R.id.buttonRegistration);

		editTextFunction = (EditText) findViewById(R.id.editTextFormula);
		textViewResult = (TextView) findViewById(R.id.textViewResult);
		textViewRegUser = (TextView) findViewById(R.id.textViewRegUser);
		editTextXStart = (EditText) findViewById(R.id.editTextXstart);
		editTextXEnd = (EditText) findViewById(R.id.editTextXend);
		graphicView = (GraphicView) findViewById(R.id.graphicView);

		ActionBar bar = getSupportActionBar();
		bar.setDisplayShowHomeEnabled(true);
		bar.setIcon(R.drawable.ic_launcher);

		formulaDrawPreferences = getSharedPreferences(APP_PREFERENCES, getApplicationContext().MODE_PRIVATE);
		editTextFunction.setText(formulaDrawPreferences.getString(APP_PREFERENCES_TEXTFormula, "x*x+x"));
		// ---------Set Last Text Formula--------------
		// editTextFunction.setText(formulaDrawController.getFormula());

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
					textViewResult.setText(new Double(formulaDrawController.cakulator(editTextFunction.getText()
							.toString(), /* xVal */
							0.0)).toString());
				} catch (ArithmeticException e) {
					Toast.makeText(getApplicationContext(), getString(R.string.mathError), Toast.LENGTH_LONG).show();
					textViewResult.setText("");
				}

			}

		});

	}

	/*-------Draw Graphic in On FirstFocus. use because do not have size of view in OnCreate--*/
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (prFirstFocus) {
			formulaDrawController.drawGraphic(graphicView, editTextFunction.getText().toString(), new Double(
					editTextXStart.getText().toString()), new Double(editTextXEnd.getText().toString()));

		}
		prFirstFocus = false;
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// Here you can get the size!
	}

	@Override
	protected void onPause() {
		// Save text formula
		// formulaDrawController.saveFormula(editTextFunction.getText().toString());
		SharedPreferences.Editor editor = formulaDrawPreferences.edit();
		editor.putString(APP_PREFERENCES_TEXTFormula, editTextFunction.getText().toString());
		editor.commit();

		super.onPause();

	}

	@Override
	protected void onResume() {
		drawUserInfo();// Draw user info after closing Registration form

		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		// MenuItem bedMenuItem = menu.findItem(R.id.item1);
		// bedMenuItem.setTitle("Коровки");

		// int base = Menu.CATEGORY_SECONDARY;

		// menu.add(base, base + 1, base + 1, "sec. item 1");

		// MenuItem myMenuItem = menu.findItem(R.id.action_settings);

		// Inflating the sub_menu menu this way, will add its menu items
		// to the empty SubMenu you created in the xml
		// getMenuInflater().inflate(R.menu, myMenuItem.getSubMenu());

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * switch(item.getItemId()){ case R.id.item1:
		 * 
		 * break; }
		 */
		if (item.getItemId() != R.id.history)
			editTextFunction.setText(item.getTitle());
		return true;
	}
}
