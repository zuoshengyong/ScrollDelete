package com.example.scrolldelete;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity{

	private TextView textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		textview = (TextView)findViewById(R.id.textview);
		String text = getIntent().getStringExtra("text");
		textview.setText(text);
	}
}
