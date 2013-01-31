package com.kushal.amv1;


import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        SetTodaysDate();
       
        int installationState = 0; // 0= no subs .. 1 =subs but no tt .. 2=installed
        SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
        try{
        	
	        try{
	        	Cursor c = db.rawQuery("SELECT * FROM subjects", null);
	        	
	        	try{
	        			c = db.rawQuery("SELECT * FROM timetable", null);
	        		
	        			try{
	        				 	c = db.rawQuery("SELECT * FROM attendance", null);
	        				 	installationState = 3;
	        				
	        			}catch(Exception e){
	        				installationState=2;
	        			}
	        			
	        			
	        			
	        	}catch(Exception e){
	        		installationState=1;
	        		
	        	}
	        	
	        	
	        	
	        }catch(Exception e){
	        	installationState=0;
	        }
        }catch(Exception e){}
        
        switch(installationState){
        case 0:
	        //NOT INSTALLED
        	
			Intent intentNotInstalled = new Intent(Main.this,NewOrImport.class);
			startActivity(intentNotInstalled);
		break;
        case 1:
			//Subjects INSTALLED But no Timetable
			Intent intentPartiallyInstalled = new Intent(Main.this,FeedTimetable.class);
			startActivity(intentPartiallyInstalled);
		break;
        case 2:
			//Subjects INSTALLED But no Timetable
			Intent intentAlmostInstalled = new Intent(Main.this,StartDate.class);
			startActivity(intentAlmostInstalled);
		break;
        case 3:
			//ALREADY INSTALLED
	        Intent intentInstalled = new Intent(Main.this,DaysTab.class);
			Calendar c= Calendar.getInstance();
			int today = c.get(Calendar.DAY_OF_WEEK);
			intentInstalled.putExtra("day", intDaytoStringDay(today));
			intentInstalled.putExtra("dayint",today);
			startActivity(intentInstalled);
		break;
        }
        db.close();
        Main.this.finish();
    }

    private void SetTodaysDate() {

        Calendar todayDate = Calendar.getInstance();
		int daynumber = todayDate.get(Calendar.DAY_OF_WEEK);
		String datestr= todayDate.get(Calendar.YEAR)+"-"+todayDate.get(Calendar.MONTH)+"-"+todayDate.get(Calendar.DAY_OF_MONTH);
		Toast.makeText(Main.this, datestr, 10000).show();
		Log.d("AM", "Main prefdate "+datestr);
		SharedPreferences prefs = getSharedPreferences(
			      "com.kushal.amv1", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("DATE", datestr).commit();
		
	}

	private String intDaytoStringDay(int n){
		String day;
		switch (n){
		case 2:
			day="monday";
			break;
		case 3:
			day="tuesday";
			break;
		case 4:
			day="wednesday";
			break;
		case 5:
			day="thursday";
			break;
		case 6:
			day="friday";
			break;
		case 7:
			day="saturday";
			break;
		case 1:
			day="sunday";
			break;
		default:
			day="defaultday";
		}
		return day;
	}
	
}