package com.kushal.amv1;

import com.kushal.amv1.R.layout;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Stats extends Activity implements OnItemSelectedListener,OnFocusChangeListener,OnClickListener,OnCheckedChangeListener{
	ArrayAdapter<String> adapterForSpinnerFrom;
	ArrayAdapter<String> adapterForSpinnerTo;
	ArrayAdapter<String> adapterForSpinnerSubject;
	Spinner fromDate=null;
	Spinner toDate=null;
	Spinner sp_subject=null;
	CheckBox cb = null;
	EditText et_minPerc = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		setTitle("Statistics");
		et_minPerc = (EditText) findViewById(R.id.editText1);
		et_minPerc.setText("75");
		cb = (CheckBox) findViewById(R.id.checkBox1);
		cb.setChecked(false);
		SQLiteDatabase db =  openOrCreateDatabase("am", MODE_PRIVATE, null);
		try{
			Cursor c = db.rawQuery("SELECT * FROM attendanceBackup", null);
			if(!c.moveToFirst()){
				cb.setVisibility(View.GONE);
				et_minPerc.setGravity(Gravity.CENTER_HORIZONTAL);
			}
		}catch(Exception e){}
		
		cb.setOnCheckedChangeListener(this);
		populateDateSpinners();
		populateSubjectSpinner();
		setListeners();
		CalculateStats();
	}

	

	private void CalculateStats() {
		int y=0,n=0;
		if(cb.isChecked()){
			y=getBackupY();
			n=getBackupN();
		}
		int th=75;
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		try{
			String msg=null;
			Cursor c=null;
			String fd = fromDate.getSelectedItem().toString();
			String td = toDate.getSelectedItem().toString();
			String sub = sp_subject.getSelectedItem().toString();
			String minPerc = et_minPerc.getText().toString();
			msg = "fd="+fd+"\ntd="+td+"\nsub="+sub+"\nminPerc="+minPerc;
			th=Integer.parseInt(minPerc);
			
			//Log.d("SUB","'"+sub+"'");
			if(sub.equalsIgnoreCase("All Subjects")){
				
				c=db.rawQuery("SELECT count(*)\"y\" FROM attendance WHERE (thedate >='"+fd+"' AND thedate<='"+td+"') AND atn='y'", null);
				if(c.moveToFirst()){
					y+=Integer.parseInt(c.getString(c.getColumnIndex("y")));
				}
				c=db.rawQuery("SELECT count(*)\"n\" FROM attendance WHERE (thedate >='"+fd+"' AND thedate<='"+td+"') AND atn='n'", null);
				if(c.moveToFirst()){
				n+=Integer.parseInt(c.getString(c.getColumnIndex("n")));
				}
				//Log.d("WHERE","IF");
				
			}
			else{
				c=db.rawQuery("SELECT count(*)\"y\" FROM attendance WHERE (thedate >='"+fd+"' AND thedate<='"+td+"') AND (subject ='"+sub+"' AND atn='y')", null);
				if(c.moveToFirst()){
				y+=Integer.parseInt(c.getString(c.getColumnIndex("y")));
				}
				c=db.rawQuery("SELECT count(*)\"n\" FROM attendance WHERE (thedate >='"+fd+"' AND thedate<='"+td+"') AND (subject ='"+sub+"' AND atn='n')", null);
				if(c.moveToFirst()){
				n+=Integer.parseInt(c.getString(c.getColumnIndex("n")));
				}
				//Log.d("WHERE","ELSE");
				
			}
			c.close();
			
		}catch(Exception e){
			//Log.d("WHERE","CATCH");
		}
		finally{
			TextView p = (TextView) findViewById(R.id.textView3);
			TextView a = (TextView) findViewById(R.id.textView4);
			TextView t = (TextView) findViewById(R.id.textView5);
			TextView per = (TextView) findViewById(R.id.textView6);
			TextView summary = (TextView) findViewById(R.id.textView7);
			
			if(y!=0 || n!=0){
				float perc = (y*100)/(y+n);
				
				p.setText("Present ="+y);
				a.setText("Absent  ="+n);
				t.setText("Total   ="+(y+n));
				per.setText("%age   ="+perc);
				
					   
				//Log.d("TH","TH="+th+"\nTH+1="+(th+1));
				//int canBunk = (int) Math.floor((100*y/th) - (y+n));
				//int haveToSit = (int) Math.ceil(((th/100)*(y+n)-y)/(1-th/100));
				
					//TH range check
					th=(th==100)?99:th;
					th = (int)Math.abs(th)%100 ;
					th=(th==0)?1:th;
					
					EditText et = (EditText)findViewById(R.id.editText1);
					et.setText(""+th);
					
					//Log.d("TH","TH="+th+"\nTH+1="+(th+1));
				String sumry="";
				if(perc<th && th!=0){
					//int haveToSit = (int) Math.ceil(((th/100)*(y+n)-y)/(1-th/100));
					
					double thby100 = th/100.000;
					//Log.d("TH","THby100="+thby100);
					double totlecs = y+n;
					double denom = 1-thby100;
					
					double haveToSitnew = Math.ceil((thby100*totlecs-y)/denom); 
					//haveToSit = 
					sumry+="You have to sit "+(int)haveToSitnew+" lecture(s).";
				}
				else if(perc>=th){
					int canBunk = (int) Math.floor((100*y/th) - (y+n));
					sumry+="You can bunk "+canBunk+" lecture(s).";
				}
				
				summary.setText(sumry);
			}
			else{
				p.setText("");
				a.setText("");
				t.setText("");
				per.setText("");
				summary.setText("No Lectures Conducted.");
			}

			db.close();
			//Log.d("WHERE","FINALLY");
		}
		
	}

	private int getBackupN() {
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		int n=0;
		try{
			Cursor c=null;
			//String fd = fromDate.getSelectedItem().toString();
			//String td = toDate.getSelectedItem().toString();
			String sub = sp_subject.getSelectedItem().toString();

			//Log.d("SUB","'"+sub+"'");
			if(sub.equalsIgnoreCase("All Subjects")){
				c=db.rawQuery("SELECT count(*)\"n\" FROM attendanceBackUp WHERE atn='n'", null);
				if(c.moveToFirst()){
				n=Integer.parseInt(c.getString(c.getColumnIndex("n")));
				//Log.d("BACKN","n="+n);
				}
				//Log.d("WHERE","IF");
			}
			else{
				c=db.rawQuery("SELECT count(*)\"n\" FROM attendanceBackUp WHERE subject='"+sub+"' AND atn='n'", null);
				if(c.moveToFirst()){
				n=Integer.parseInt(c.getString(c.getColumnIndex("n")));
				//Log.d("BACKN","n="+n);
				}
				//Log.d("WHERE","ELSE");
				
			}
			c.close();
			db.close();
		}catch(Exception e){
			//Log.d("STATBACKUPEXC",e.toString());
			db.close();
		}
		return n;
	}



	private int getBackupY() {
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		int y=0;
		try{
			Cursor c=null;
			//String fd = fromDate.getSelectedItem().toString();
			//String td = toDate.getSelectedItem().toString();
			String sub = sp_subject.getSelectedItem().toString();

			//Log.d("SUB","'"+sub+"'");
			if(sub.equalsIgnoreCase("All Subjects")){
				c=db.rawQuery("SELECT count(*)\"y\" FROM attendanceBackUp WHERE atn='y'", null);
				if(c.moveToFirst()){
				y=Integer.parseInt(c.getString(c.getColumnIndex("y")));
				//Log.d("BACKY","y="+y);
				}
				//Log.d("WHERE","IF");
			}
			else{
				c=db.rawQuery("SELECT count(*)\"y\" FROM attendanceBackUp WHERE subject='"+sub+"' AND atn='y'", null);
				if(c.moveToFirst()){
				y=Integer.parseInt(c.getString(c.getColumnIndex("y")));
				//Log.d("BACKY","y="+y);
				}
				//Log.d("WHERE","ELSE");
				
			}
			//c.close();
			db.close();
		}catch(Exception e){
			//Log.d("STATBACKUPEXC",e.toString());
			db.close();
		}
		return y;
	}



	private void populateSubjectSpinner() {
		//POPULATION OF Subject SPINNER
		Cursor c = null;
		SQLiteDatabase db = null;
		try{
			db = openOrCreateDatabase("am", MODE_PRIVATE, null);
			sp_subject= (Spinner) findViewById(R.id.spinner3);
			adapterForSpinnerSubject = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item); 
			adapterForSpinnerSubject.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item); 
			sp_subject.setAdapter(adapterForSpinnerSubject);
			c=db.rawQuery("SELECT * FROM subjects", null);
			
			int i=0;
			if (c.moveToFirst()) {
					 adapterForSpinnerSubject.add("All Subjects");
			         do {
			        	 //if(i==0)
			        		// selecteddate = c.getString(c.getColumnIndex("thedate"));
			             adapterForSpinnerSubject.add(c.getString(c.getColumnIndex("subject_name")));
			             i++;
			         } while (c.moveToNext());      
			 }
		}catch(Exception e){}
		finally{
			c.close();
			db.close();
		}

	}

	private void populateDateSpinners() {
		
			//POPULATION OF DATE SPINNERs
			Cursor c = null;
			SQLiteDatabase db = null;
			try{
				db = openOrCreateDatabase("am", MODE_PRIVATE, null);
				fromDate = (Spinner) findViewById(R.id.spinner1);
				toDate =  (Spinner) findViewById(R.id.spinner2);
				adapterForSpinnerFrom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item); 
				adapterForSpinnerFrom.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
				adapterForSpinnerTo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item); 
				adapterForSpinnerTo.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
				
				fromDate.setAdapter(adapterForSpinnerFrom);
				toDate.setAdapter(adapterForSpinnerTo);
				
				c=db.rawQuery("SELECT DISTINCT(thedate) FROM attendance ORDER BY thedate ASC", null);
				//int i=0;
				if (c.moveToFirst()) { 
				         do {
				        	 //if(i==0)
				        		 //selecteddate = c.getString(c.getColumnIndex("thedate"));
				             adapterForSpinnerFrom.add(c.getString(c.getColumnIndex("thedate")));
				             //i++;
				         } while (c.moveToNext());      
			    }
				
				c=db.rawQuery("SELECT DISTINCT(thedate) FROM attendance ORDER BY thedate DESC", null);
				//int i=0;
				if (c.moveToFirst()) { 
				         do {
				        	 //if(i==0)
				        		 //selecteddate = c.getString(c.getColumnIndex("thedate"));
				             adapterForSpinnerTo.add(c.getString(c.getColumnIndex("thedate")));
				             //i++;
				         } while (c.moveToNext());      
				 }
				
				
			}catch(Exception e){}
			finally{
				c.close();
				db.close();
			}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		CalculateStats();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//CalculateStats();
	}
	public void setListeners() {
		fromDate.setOnItemSelectedListener(this);
		toDate.setOnItemSelectedListener(this);
		sp_subject.setOnItemSelectedListener(this);
		et_minPerc.setOnClickListener(this);
	}


	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
	}


	@Override
	public void onClick(View v) {
		if(v.getId()==et_minPerc.getId()){
			CalculateStats();
		}
	}



	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		CalculateStats();
	}
}
