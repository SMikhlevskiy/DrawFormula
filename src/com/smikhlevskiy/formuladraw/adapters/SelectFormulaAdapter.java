package com.smikhlevskiy.formuladraw.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.smikhlevskiy.formuladraw.R;
import com.smikhlevskiy.formuladraw.model.SelectFormulaItem;

public class SelectFormulaAdapter extends ArrayAdapter<SelectFormulaItem> {
	final Context context;
	final int layoutResourceId;
	private ArrayList<SelectFormulaItem> data = null;

	public SelectFormulaAdapter(final Context context, final int layoutResourceId, final ArrayList<SelectFormulaItem> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View row = convertView;
		LoadHolder holder = null;

		if (row == null) {
			final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new LoadHolder();
			holder.txtMainTitle = (TextView) row.findViewById(R.id.txtMainTitle);
			holder.txtSubTitle = (TextView) row.findViewById(R.id.txtSubTitle);

			row.setTag(holder);
		} else {
			holder = (LoadHolder) row.getTag();
		}

		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadHolder gholder = (LoadHolder) v.getTag();

				Toast.makeText(v.getContext(), gholder.txtSubTitle.getText().toString(), Toast.LENGTH_LONG).show();
				
				Intent answerInent = new Intent();
				answerInent.putExtra("formula",gholder.txtSubTitle.getText().toString());
				((Activity)context).setResult(1,answerInent);
				((Activity)context).finish();
			}

		});

		/*final FormulaItemModel jsonLoadObj = data.get(position);*/
		holder.txtMainTitle.setText(/*jsonLoadObj.getMainTitle().toString().toUpperCase()*/data.get(position).getMainTitle().toUpperCase());
		holder.txtSubTitle.setText(/*jsonLoadObj.getSubTitle()*/data.get(position).getSubTitle());

		return row;
	}

	private class LoadHolder {
		TextView txtMainTitle;
		TextView txtSubTitle;
	}
}