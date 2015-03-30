package com.smikhlevskiy.formuladraw.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.smikhlevskiy.formuladraw.R;
import com.smikhlevskiy.formuladraw.model.SpinnerItemLines;

public class SpinnerLinesAdapter  extends ArrayAdapter<SpinnerItemLines> {
	final Context context;
	private final int layoutResourceId;
	private LayoutInflater inflater;
	private ArrayList<SpinnerItemLines> data = null;
	
	public SpinnerLinesAdapter(final Context context, final int layoutResourceId, final ArrayList<SpinnerItemLines> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		inflater = ((Activity) context).getLayoutInflater();
		
	}
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position, convertView, parent);
    }
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View row = inflater.inflate(layoutResourceId, parent, false);
		TextView label = (TextView)row.findViewById(R.id.spinnerText);
		label.setText(data.get(position).getText());
		label.setTextColor(data.get(position).getColor());
		return row;
	}

	
	
}
	