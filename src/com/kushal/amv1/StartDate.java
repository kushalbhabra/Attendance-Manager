package com.kushal.amv1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
public class StartDate extends Activity implements OnClickListener{
	String[] strDays = new String[]{
            "blank",
            "sunday",//1
            "monday",//2
            "tuesday",//3
            "wednesday",//4
            "thusday",//5
            "friday",//6
            "saturday",//7
            
          };
	DatePicker sd; //Start Date
	Button go;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getstartdate);
		setTitle("Final Step!");
		go=(Button) findViewById(R.id.btn_startAm);
		sd = (DatePicker) findViewById(R.id.dpStartDate); 
		go.setOnClickListener(this);
		//Note: DatePicker Month starts from 0 to 11
		// 		SQLite DateFormat YYYY-MM-DD
		
	}

	@Override
	public void onClick(View v) {
		
		go.setText("Creating db... ");
		createdb();
		go.setText("Done! ");
		
		Intent intent = new Intent(StartDate.this,DaysTab.class);
		Calendar c= Calendar.getInstance();
		int today = c.get(Calendar.DAY_OF_WEEK);
		
		startActivity(intent);
		Toast t = Toast.makeText(StartDate.this, "Tap on the lecture to mark attendance!", 10000);
		t.show();
		StartDate.this.finish();
		
	}
	private void createdb(){
		try{
			SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		    db.execSQL("DROP TABLE IF EXISTS attendance");
		    db.execSQL("CREATE TABLE attendance(cellNo varchar,thedate varchar,day varchar,subject varchar,atn varchar)");
		    
		    Calendar cal = Calendar.getInstance();
		    cal.set(sd.getYear(),sd.getMonth(),sd.getDayOfMonth());
		    
		    Log.d("AM", "Inside createdb");
		    
		    
		    int daySpan180=0;
			while(daySpan180!=181){
				
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
				int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
				
				String currentDay = strDays[day_of_week];
				String currentDate = year+"-"+month+"-"+day_of_month;
				
				Cursor c = db.rawQuery("SELECT * FROM timetable WHERE day='"+currentDay+"' ORDER BY st_hour", null);
				
				if(c.moveToFirst()){
					do{		
						String cellNo = c.getString(c.getColumnIndex("cellno"));
						String subject = c.getString(c.getColumnIndex("subject"));
						Log.d("AM","CellNo="+cellNo+"  Date="+currentDate+"   Day="+currentDay);
						String insertquery="INSERT INTO attendance VALUES('"+cellNo+"','"+currentDate+"','"+currentDay+"','"+subject+"','na')";
						db.execSQL(insertquery);
						Log.d("AM",insertquery);
					}while(c.moveToNext());
					
				}
				c.close();
				cal.add(Calendar.DATE, 1);
				daySpan180++;
			}
		    
		    
		}catch(Exception e)
		{
			Log.d("AM",e.toString());
		}
		    	
	}
		@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF EXISTS attendance");
		db.close();
		super.onBackPressed();
	}
}

