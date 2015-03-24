package com.smikhlevskiy.formuladraw.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.smikhlevskiy.formuladraw.R;
import com.smikhlevskiy.formuladraw.adapters.SelectFormulaAdapter;
import com.smikhlevskiy.formuladraw.model.SelectFormulaItem;

public class SelectFormulaActivity extends Activity {
	static final private int CHOOSE_THIEF = 0;
	SelectFormulaAdapter itemAdapter;
	ListView lstView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formula_viewlist);
		

		lstView = (ListView) findViewById(R.id.lv_list);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDatas");
		query.whereExists("user");
		query.orderByAscending("user");
		query.setLimit(1000);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					ArrayList<SelectFormulaItem> aList = new ArrayList<SelectFormulaItem>();
					for (int i = objects.size() - 1; i >= 0; i--) {
						aList.add(new SelectFormulaItem("Title " + i, objects.get(i).getString("formula")));
					}
					itemAdapter = new SelectFormulaAdapter(SelectFormulaActivity.this, R.layout.lstview_itemrow_load, aList);
					lstView.setAdapter(itemAdapter);

				} else {
					
					Toast.makeText(SelectFormulaActivity.this,
							"Чтение списка формул невозможно. Нет связи с Облачным хранилищем Parse.com",
							Toast.LENGTH_LONG).show();
					finish();
				}

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
