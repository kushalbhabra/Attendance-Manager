package com.kushal.amv1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.about);
		setTitle("About us");
		TextView tv_about  = (TextView) findViewById(R.id.tv_about);
		
		String toast = "Feb 2012 \n\n" +
	       "Developers: " +
	       "\nKushal Bhabra" +
	       "\n(kushalbhabra@gmail.com)";
		toast += " \nManoj Gudi\n(manoj.p.gudi@gmail.com)";
		toast += " \nSAurabha Joglekar\n(sau6402@gmail.com)\n";
		toast += "\n\n Dedicated to MU soldiers.\n\nBugs/suggestions welcomed.";
		tv_about.setText(toast);
	}
}
