package com.smikhlevskiy.formuladraw.ui;



import com.smikhlevskiy.formuladraw.ui.UserRegActivity;
import com.smikhlevskiy.formuladraw.util.FDConstants;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.smikhlevskiy.formuladraw.entity.FormulaDrawController;
import com.smikhlevskiy.formuladraw.model.FindRoot;
import com.smikhlevskiy.formuladraw.model.ReversePolishNotation;
import com.smikhlevskiy.formuladraw.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
	private Button buttonUserReg;
	private EditText editTextFunction;
	private EditText editTextXStart;
	private EditText editTextXEnd;
	private TextView textViewResult;
	private TextView textViewRegUser;
	private GraphicView graphicView;
	private boolean prFirstFocus = true;
	private SharedPreferences formulaDrawPreferences;

	private FormulaDrawController formulaDrawController;
	MenuItem FDMenuItem[] = new MenuItem[5];

	/**
	 * Save state Workspace in Preferces: formulas and min/max
	 */
	private void saveWorkspace() {
		SharedPreferences.Editor editor = formulaDrawPreferences.edit();
		editor.putString(FDConstants.APP_PREFERENCES_TEXTFormula, editTextFunction.getText().toString());
		editor.commit();
	}

	/**
	 * ------------Start Activity SelectFormula-------------
	 * 
	 * @param typeSelect
	 *            - AddFunction or LoadFormula
	 */
	private void startSelectFormulaActivity(int typeSelect) {
		if (ParseUser.getCurrentUser() != null) {
			Intent intent = new Intent(MainActivity.this, SelectFormulaActivity.class);
			intent.putExtra("typeSelect", typeSelect);
			startActivityForResult(intent, typeSelect);
		} else
			Toast.makeText(MainActivity.this, getString(R.string.pleaseReg), Toast.LENGTH_LONG).show();
	}

	/**
	 * Out information about current user
	 * 
	 */
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

	/**
	 * Draw Graphic On screen
	 */
	private void drawGraphic() {
		formulaDrawController.drawGraphic(graphicView, editTextFunction.getText().toString(), new Double(editTextXStart
				.getText().toString()), new Double(editTextXEnd.getText().toString()));
	}

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

		formulaDrawPreferences = getSharedPreferences(FDConstants.APP_PREFERENCES, getApplicationContext().MODE_PRIVATE);

		editTextFunction.setText(formulaDrawPreferences.getString(FDConstants.APP_PREFERENCES_TEXTFormula, "x*x+x"));

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

		// --------------------------Draw Graphic-----------------
		buttonDraw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				drawGraphic();

			}

		});
		// -------------------------Calckulator-----------------
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
			drawGraphic();

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
		saveWorkspace();

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

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		
		case R.id.loadFormula: {
			startSelectFormulaActivity(FDConstants.LOAD_FORMULA);

			break;
		}
		case R.id.addFunction: {
			startSelectFormulaActivity(FDConstants.ADD_FUNCTION);

			break;
		}
		case R.id.addUserFunction: {
			startSelectFormulaActivity(FDConstants.ADD_USER_FUNCTION);

			break;
		}

		case R.id.saveFormula: {
			ParseUser user = ParseUser.getCurrentUser();
			if (user != null) {

				final EditText editTextAlertDialog = new EditText(this);

				editTextAlertDialog.setText("UserFunc");

				new AlertDialog.Builder(this)

				.setTitle(getString(R.string.saveFormula)).setMessage(getString(R.string.inputNameFormula))
						.setView(editTextAlertDialog)
						.setPositiveButton(getString(R.string.oK), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								ParseUser user = ParseUser.getCurrentUser();
								ParseObject post = new ParseObject("UserDatas");
								post.put("formula", editTextFunction.getText().toString());
								post.put("name", editTextAlertDialog.getText().toString());
								post.put("user", user);
								post.saveInBackground();

							}

						})

						.setNegativeButton(getString(R.string.cansel), new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int whichButton) {

							}

						})

						.show();

			}

			break;
		}
		default:
			break;

		}

		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data != null) {
			if ((requestCode == FDConstants.ADD_FUNCTION) || (requestCode == FDConstants.ADD_USER_FUNCTION)) {
				String textFormula = data.getStringExtra("name").toLowerCase();
				ReversePolishNotation rpn = new ReversePolishNotation();
				rpn.setFormula(textFormula);
				if ((rpn.textToFunction(textFormula) == FDConstants.TypeFunction.XVALUE)
						|| (rpn.textToFunction(textFormula) == FDConstants.TypeFunction.E)
						|| (rpn.textToFunction(textFormula) == FDConstants.TypeFunction.Pi))
					editTextFunction.setText(editTextFunction.getText() + textFormula);
				else
					editTextFunction.setText(editTextFunction.getText() + textFormula + "(x)");
			} else if (requestCode == FDConstants.LOAD_FORMULA)
				editTextFunction.setText(data.getStringExtra("formula"));

			drawGraphic();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
