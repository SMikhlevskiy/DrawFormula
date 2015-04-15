package com.smikhlevskiy.formuladraw.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.smikhlevskiy.formuladraw.ui.UserRegActivity;
import com.smikhlevskiy.formuladraw.util.FDConstants;
import com.parse.ParseUser;
import com.smikhlevskiy.formuladraw.adapters.SpinnerLinesAdapter;
import com.smikhlevskiy.formuladraw.entity.FormulaDrawController;
import com.smikhlevskiy.formuladraw.model.MathUtility;
import com.smikhlevskiy.formuladraw.model.SpinnerItemLines;
import com.smikhlevskiy.formuladraw.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Spinner spinnerLine;
	private Button buttonCalck;
	private Button buttonDraw;
	private Button buttonRoot;
	private Button buttonCalcIntegral;
	private Button buttonUserReg;
	private EditText editTextFunction;
	private EditText editTextXStart;
	private EditText editTextXEnd;
	private CheckBox dYdT;
	private TextView textViewRegUser;
	private TextView textViewInfoBox;
	private GraphicView graphicView;
	private boolean prFirstFocus = true;
	private Handler mainActivityHandler;
	private FormulaDrawController formulaDrawController;

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
		if ((editTextXStart.getText().toString().length() > 0) && (editTextXEnd.getText().toString().length() > 0))
			formulaDrawController.drawGraphic(graphicView, editTextFunction.getText().toString(), new Double(
					editTextXStart.getText().toString()), new Double(editTextXEnd.getText().toString()));
		else
			textViewInfoBox.setText(getString(R.string.errorDiapazon));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("Main activity", "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i("Main activity", "Start FindViews");
		// ---------------------------------------------
		// -------------Activity main Handler----------
		// ---------------------------------------------
		mainActivityHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case FDConstants.OUT_TEXT_INFO_MESSAGE:
					textViewInfoBox.setTextColor(Color.BLACK);
					textViewInfoBox.setBackgroundColor(Color.TRANSPARENT);
					textViewInfoBox.setText((String) msg.obj);
					break;
				case FDConstants.OUT_TEXT_ERROR_MESSAGE:
					textViewInfoBox.setTextColor(Color.WHITE);
					textViewInfoBox.setBackgroundColor(Color.RED);
					textViewInfoBox.setText((String) msg.obj);
					break;
				case FDConstants.CHANGE_XLIMITS:
					double x[] = (double[]) msg.obj;

					NumberFormat formatter = new DecimalFormat("#0.00");

					editTextXStart.setText(formatter.format(x[0]));
					editTextXEnd.setText(formatter.format(x[1]));

					break;

				default:
					break;
				}
			}
		};

		formulaDrawController = new FormulaDrawController(this, mainActivityHandler);

		buttonCalck = (Button) findViewById(R.id.buttonCalck);
		buttonDraw = (Button) findViewById(R.id.buttonDraw);
		buttonRoot = (Button) findViewById(R.id.buttonRoot);
		buttonCalcIntegral = (Button) findViewById(R.id.buttonCalcIntegral);
		buttonUserReg = (Button) findViewById(R.id.buttonRegistration);

		editTextFunction = (EditText) findViewById(R.id.editTextFormula);

		textViewRegUser = (TextView) findViewById(R.id.textViewRegUser);
		editTextXStart = (EditText) findViewById(R.id.editTextXstart);
		editTextXEnd = (EditText) findViewById(R.id.editTextXend);
		graphicView = (GraphicView) findViewById(R.id.graphicView);
		graphicView.setOutHandler(mainActivityHandler);

		textViewInfoBox = (TextView) findViewById(R.id.textViewInfoBox);
		
		dYdT = (CheckBox) findViewById(R.id.dYdT);

		Log.i("Main activity", "Init spinner");
		// ---------------Spinner Lines---

		ArrayList<SpinnerItemLines> aList = new ArrayList<SpinnerItemLines>();
		for (int i = 0; i < FDConstants.colorSpinnerLines.length; i++)
			aList.add(new SpinnerItemLines(getString(R.string.line) + " " + (i + 1), FDConstants.colorSpinnerLines[i]));
		SpinnerLinesAdapter adapter = new SpinnerLinesAdapter(this, R.layout.item_spinner_lines, aList);
		spinnerLine = (Spinner) findViewById(R.id.spinnerLine);
		spinnerLine.setAdapter(adapter);
		spinnerLine.setSelection(0);

		spinnerLine.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				editTextFunction.setText(formulaDrawController.getFormulas(position));
				dYdT.setChecked(formulaDrawController.getdYdT(position));
				formulaDrawController.setCurrentLine(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
		Log.i("Main activity", "Setup Action Bar");
		// --------------ActionBar-----
		ActionBar bar = getSupportActionBar();
		bar.setDisplayShowHomeEnabled(true);
		bar.setIcon(R.drawable.ic_launcher);

		// --------------On change formula's text
		editTextFunction.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				formulaDrawController.setFormulas(s.toString(), spinnerLine.getSelectedItemPosition());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
		
		dYdT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

		       @Override
		       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		    	   formulaDrawController.setdYdT(isChecked, spinnerLine.getSelectedItemPosition());
		       }
		   }
		);     		// ----------read preferencrs--------------
		Log.i("Main activity", "Read preferces");
		formulaDrawController.readPreferences();

		// editTextFunction.setText(formulaDrawController.getFormulas(spinnerLine.getSelectedItemPosition()));
		// --------------- Find Root-------------
		buttonRoot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textViewInfoBox.setText("");
				MathUtility mathUtility = new MathUtility(getApplicationContext(), mainActivityHandler,
						editTextFunction.getText().toString());

				if ((editTextXStart.getText().toString().length() > 0)
						&& (editTextXEnd.getText().toString().length() > 0))
					mathUtility.findRoot(new Double(editTextXStart.getText().toString()), new Double(editTextXEnd
							.getText().toString()), (double) 0.000001);
				else
					textViewInfoBox.setText(getString(R.string.errorDiapazon));
			}
		});
		// --------------- calckIntegral-------------
		buttonCalcIntegral.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textViewInfoBox.setText("");
				MathUtility mathUtility = new MathUtility(getApplicationContext(), mainActivityHandler,
						editTextFunction.getText().toString());

				if ((editTextXStart.getText().toString().length() > 0)
						&& (editTextXEnd.getText().toString().length() > 0))

					mathUtility.calcIntegral(new Double(editTextXStart.getText().toString()), new Double(editTextXEnd
							.getText().toString()), 10000);
				else
					textViewInfoBox.setText(getString(R.string.errorDiapazon));

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
				textViewInfoBox.setText("");
				drawGraphic();

			}

		});
		// -------------------------Calckulator-----------------
		buttonCalck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if ((editTextXStart.getText().toString().length() > 0))

					formulaDrawController.cakulator(editTextFunction.getText().toString(), new Double(editTextXStart
							.getText().toString()));
				else
					textViewInfoBox.setText(getString(R.string.errorDiapazon));

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
		Log.i("Main Activity", "OnPause");
		Log.i("Main Activity", "Save Preferences");
		// Save text formula
		// formulaDrawController.saveFormula(editTextFunction.getText().toString());

		formulaDrawController.saveWorkspace(editTextXStart.getText().toString(), editTextXEnd.getText().toString());

		super.onPause();

	}

	@Override
	protected void onResume() {
		Log.i("Main Activity", "OnResume");
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
			if (user == null){
				Message message = mainActivityHandler.obtainMessage();
				message.what = FDConstants.OUT_TEXT_ERROR_MESSAGE;
				message.obj = this.getString(R.string.notRegistredUser);
				mainActivityHandler.sendMessage(message);
				
				return true;
			}

			final EditText editTextAlertDialog = new EditText(this);

			editTextAlertDialog.setText("UserFunc");

			new AlertDialog.Builder(this)

			.setTitle(getString(R.string.saveFormula)).setMessage(getString(R.string.inputNameFormula))
					.setView(editTextAlertDialog)
					.setPositiveButton(getString(R.string.oK), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							formulaDrawController.saveUserFunction(editTextAlertDialog.getText().toString(),
									editTextFunction.getText().toString());

						}

					})

					.setNegativeButton(getString(R.string.cansel), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int whichButton) {

						}

					})

					.show();

			break;
		}
		default:
			break;

		}

		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("Main Activity", "OnActivityResult");
		// --------Get Function from ActivitySelectFunction--------------
		if (data != null) {

			if ((requestCode == FDConstants.ADD_FUNCTION) || (requestCode == FDConstants.ADD_USER_FUNCTION))
				editTextFunction.setText(editTextFunction.getText()
						+ formulaDrawController.formFunctionInsert(data.getStringExtra("name").toLowerCase()));
			else if (requestCode == FDConstants.LOAD_FORMULA)
				editTextFunction.setText(data.getStringExtra("formula").toLowerCase());

			drawGraphic();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
