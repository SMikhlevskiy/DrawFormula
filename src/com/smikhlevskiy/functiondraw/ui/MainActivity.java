package com.smikhlevskiy.functiondraw.ui;


import com.smikhlevskiy.functiondraw.R;
import com.smikhlevskiy.functiondraw.entity.FunctionDrawCore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	private Button buttonCalck;
	private EditText editTextFunction;
	
	private FunctionDrawCore functionDrawCore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		functionDrawCore=new FunctionDrawCore(); 

		buttonCalck = (Button) findViewById(R.id.buttonCalck);
		editTextFunction=(EditText) findViewById(R.id.editTextFunction);
		
		
		buttonCalck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				functionDrawCore.compileFormula("10.5+20*(10+3*(4/2))");
				//functionDrawCore.compileFormula(editTextFunction.getText().toString());
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
