package com.smikhlevskiy.formuladraw.ui;

import java.util.List;

import com.smikhlevskiy.formuladraw.ui.UserRegActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
	static final private int CHOOSE_THIEF = 0;
	private Button buttonCalck;
	private Button buttonDraw;
	private Button buttonRoot;
	private Button buttonSelectFormula;
	public Button buttonUserReg;
	private EditText editTextFunction;	
	private EditText editTextXStart;
	private EditText editTextXEnd;
	private TextView textViewResult;
	private TextView textViewRegUser;
	private GraphicView graphicView;
	private boolean prFirstFocus = true;
	private SharedPreferences formulaDrawPreferences;

	private static final String APP_PREFERENCES = "formuladrawpreferences";
	private static final String APP_PREFERENCES_TEXTFormula = "formula";
	private FormulaDrawController formulaDrawController;
	MenuItem FDMenuItem[] = new MenuItem[5];
	/**		Out information about current user
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
	/**		Get user's old formulas and add they to History menu
	 * 
	 */
	public void GetOldFormulas() {
		ParseUser user = ParseUser.getCurrentUser();
		if (user == null)
			return;

		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDatas");
		query.whereEqualTo("user", user);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {

					for (int i = 0; i < 5; i++)
						if (objects.size() - i - 1 >= 0) {
							FDMenuItem[i].setTitle(objects.get(objects.size() - i - 1).getString("formula"));

						}
				} else {

				}
			}
		});

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
		buttonSelectFormula = (Button) findViewById(R.id.buttonSelectFormula);

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
		
		/* -----Button User Registration form Set On ClickListener---------- */
		buttonSelectFormula.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (ParseUser.getCurrentUser() != null) {
					startActivityForResult(new Intent(MainActivity.this, SelectFormulaActivity.class),CHOOSE_THIEF);				
				} else Toast.makeText(MainActivity.this, "Зарегистрируйтись пожайлуста", Toast.LENGTH_LONG).show();

			}

		});


		// --------------------------Draw Graphic-----------------
		buttonDraw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				formulaDrawController.drawGraphic(graphicView, editTextFunction.getText().toString(), new Double(
						editTextXStart.getText().toString()), new Double(editTextXEnd.getText().toString()));

				ParseUser user = ParseUser.getCurrentUser();
				if (user != null) {
					// Make a new post
					ParseObject post = new ParseObject("UserDatas");
					post.put("formula", editTextFunction.getText().toString());
					post.put("user", user);
					post.saveInBackground();

					GetOldFormulas();

				}

			}

		});
		//-------------------------Calckulator-----------------
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
		GetOldFormulas();
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		FDMenuItem[0] = menu.findItem(R.id.item1);

		FDMenuItem[1] = menu.findItem(R.id.item2);
		FDMenuItem[2] = menu.findItem(R.id.item3);
		FDMenuItem[3] = menu.findItem(R.id.item4);
		FDMenuItem[4] = menu.findItem(R.id.item5);
		GetOldFormulas();


		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() != R.id.history)
			editTextFunction.setText(item.getTitle());
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  editTextFunction.setText(data.getStringExtra("formula"));
	}	
}
