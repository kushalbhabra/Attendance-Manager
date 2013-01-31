package com.kushal.amv1;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.googlecode.android.widgets.DateSlider.AlternativeDateSlider;
import com.googlecode.android.widgets.DateSlider.DateSlider;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainTimeTable extends ListActivity implements OnClickListener,OnItemSelectedListener{
	
	TextView tv_today;
	String date=null;
	String today=null;
	String selecteddate = null;
	int dayintent=1;
	String iniDate=null;
	String fullDateatn="na";
	String DayOfWeek=null;
	
	//Spinner spinner=null;
	ArrayAdapter<String> adapterForSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.maintimetable);
		
		//GETTING SELF DAY AND DATE
		today=getIntent().getExtras().getString("date");
		DayOfWeek=getIntent().getExtras().getString("day");
		
		Log.d("AM", "MTT today="+today);
		Log.d("AM", "MTT DayOfWeek="+DayOfWeek);
		
		//SETTING UP CALENDAR BUTTON
		Button btn_calendar = (Button) findViewById(R.id.btn_calendar);
		btn_calendar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(1);//SHOWS DATE SELECTION DIALOG	
			}
		});		
		
		tv_today = (TextView) findViewById(R.id.sp_Day);
		Calendar cal = Calendar.getInstance();
		String date[] = today.split("-");
		int year=Integer.parseInt(date[0]);
		int month=Integer.parseInt(date[1]);
		int day_of_month=Integer.parseInt(date[2]);
		//cal.set(year,month,day_of_month);
		//cal.get(Calendar.)
		Date Ddate = new Date(year,month,day_of_month); // is same as date = new

		SimpleDateFormat dformat = new SimpleDateFormat("dd-MMM-yy");
		String formattedDate = dformat.format(Ddate);
		Log.d("AM", "SET TEXTVIEW:"+formattedDate);
		tv_today.setText(formattedDate);
		
		
		
		Button markFull = (Button) findViewById(R.id.btn_markFull);
		
		markFull.setOnClickListener(this);
		
		setListData();
		
	}
	
	
    private void setListData() {
    	
    	Log.d("AM", "MMT setlistdata( called for "+today);
    	ArrayList<Lecture> lectures = populateList(today);
    	
    	for(Lecture lec: lectures){
    		Log.d("AM", lec.getSubject()+" "+lec.getTime()+" "+lec.getAtn());
    	}
    	
		if (lectures!=null)
		{
			LectureArrayAdapter LAA = new LectureArrayAdapter(this, R.layout.lectureinlist, lectures);
	        setListAdapter(LAA);
		}
		else
			Log.d("AM", "LIST EMPTY");
	}
    
    
	// define the listener which is called once a user selected the date.
    private DateSlider.OnDateSetListener mDateSetListener =
        new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                // update the dateText view with the corresponding date
                //dateText.setText(String.format("The chosen date:%n%te. %tB %tY", selectedDate, selectedDate, selectedDate));
            	
            	tv_today.setText(String.format("%n%te. %tB %tY", selectedDate, selectedDate, selectedDate));
            	
            	String datestr = getDateFromCal(selectedDate);
            	SharedPreferences prefs = getSharedPreferences(
      			      "com.kushal.amv1", Context.MODE_PRIVATE);
            	SharedPreferences.Editor editor = prefs.edit();
      			editor.putString("DATE", datestr).commit();
            	startActivity(new Intent(MainTimeTable.this,DaysTab.class));
            	MainTimeTable.this.finish();
            }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        // this method is called after invoking 'showDialog' for the first time
        // here we initiate the corresponding DateSlideSelector and return the dialog to its caller
    	
    	// get today's date and time
        final Calendar c = Calendar.getInstance();
        
        switch (id) {
        case 1:
            return new AlternativeDateSlider(this,mDateSetListener,c);
        }
        return null;
    }


	private ArrayList<Lecture> populateList(String date) {
			ArrayList<Lecture> lectures = null;
			Lecture lecture = null;
			if(date!=null){
			try{
				SQLiteDatabase db =openOrCreateDatabase("am", MODE_PRIVATE, null);
				Cursor c = db.rawQuery("SELECT * FROM attendance WHERE thedate='"+date+"' ORDER BY cellNo", null);
				
				lectures = new ArrayList<Lecture>();
				/* LOGGING FULL ATTENDANCE DATABASE
				Cursor c = db.rawQuery("SELECT * FROM attendance", null);
				if(c.moveToFirst())
				{
					//cellNo varchar,thedate date,day varchar,subject varchar,atn varchar
					do{
					String dbCellno = c.getString(c.getColumnIndex("cellNo"))+"|";
					String dbthedate = c.getString(c.getColumnIndex("thedate"))+"|";
					String dbday = c.getString(c.getColumnIndex("day"))+"|";
					String dbsubject = c.getString(c.getColumnIndex("subject"))+"|";
					String dbatn = c.getString(c.getColumnIndex("atn"));
					Log.d("AM", dbCellno+dbthedate+dbday+dbsubject+dbatn);
					}while(c.moveToNext());
					
				}
				*/
			
				int count = c.getCount();
				Log.d("AM", "COUNT="+count);
				
				int i=0;
				
				Cursor c_cell =null;
				
				if(c.moveToFirst()){
					do{
						lecture = new Lecture();
						
						//Set Subject
						String subject = c.getString(c.getColumnIndex("subject"));
						lecture.setSubject(subject);
						
						//Set Time
						c_cell = db.rawQuery("SELECT * FROM timetable where cellNo='"+c.getString(c.getColumnIndex("cellNo"))+"'", null);
						c_cell.moveToFirst();
						String time = c_cell.getString(c_cell.getColumnIndex("st_hour"))+":"+c_cell.getString(c_cell.getColumnIndex("st_min"));
						lecture.setTime(time);
						
						//Set Attendance
						String atn = c.getString(c.getColumnIndex("atn"));
						lecture.setAtn(atn);

						c_cell.close();
						i++;
						if(lecture!=null)
							lectures.add(lecture);
					}while(c.moveToNext());
				}
				//Log.d("WHERE", "after setting adapter");
				c.close();
				db.close();
			}
			catch(Exception e){}
		}
			else{
				Log.d("AM", "MTT date is null");
			}
			return lectures;
	}


	private String getSelectedDate() {
		return today;
	}

	
	@Override
	public void onClick(View v) {
		
		if(v.getId()==R.id.btn_markFull){
			//String cellno =getcellno(position+1);
			String date = getSelectedDate();
			SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
			Cursor c = db.rawQuery("SELECT * FROM attendance WHERE thedate ='"+date+"'", null);
			if(c.moveToFirst()){
				
				fullDateatn = roll(fullDateatn);
				db.execSQL("UPDATE attendance SET atn='"+fullDateatn+"' WHERE thedate ='"+date+"'");
				//Log.d("ATN", "attendance update on cellno="+cellno+"   date="+date+"   atn="+atn);
				setListData();
			}
			c.close();
			db.close();
			
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		setListData();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		setListData();
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//Toast t = Toast.makeText(MainTimeTable.this, "LIST item clicked", 2000);
		//t.show();
		String cellno =getcellno(position+1);
		String date = getSelectedDate();
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM attendance WHERE cellNo='"+cellno+"' AND thedate ='"+date+"'", null);
		if(c.moveToFirst()){
			String atn = c.getString(c.getColumnIndex("atn"));
			atn = roll(atn);
			db.execSQL("UPDATE attendance SET atn='"+atn+"' WHERE cellNo='"+cellno+"' AND thedate ='"+date+"'");
			//Log.d("ATN", "attendance update on cellno="+cellno+"   date="+date+"   atn="+atn);
			setListData();
		}
		c.close();
		db.close();
		
	}
	private String roll(String atn) {
		if (atn.equalsIgnoreCase("na"))
			atn = "y";
		else if (atn.equalsIgnoreCase("y"))
			atn = "n";
		else if (atn.equalsIgnoreCase("n"))
			atn = "na";
		return atn;
	}

	private String getcellno(int position){
		int n = 0;
		if (DayOfWeek.equalsIgnoreCase("monday")){
			n = 2;
		}if(DayOfWeek.equalsIgnoreCase("tuesday")){
			n = 3;
		}if(DayOfWeek.equalsIgnoreCase("wednesday")){
			n = 4;
		}if(DayOfWeek.equalsIgnoreCase("thursday")){
			n = 5;
		}if(DayOfWeek.equalsIgnoreCase("friday")){
			n = 6;
		}if(DayOfWeek.equalsIgnoreCase("saturday")){
			n = 7;
		}if(DayOfWeek.equalsIgnoreCase("sunday")){
			n = 1;
		}
		return n+" "+position;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mymenu,menu);
    	return true;
    }
	
	
	
	//OPTION KEY MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()== R.id.stats_menu){
    		Intent intent = new Intent(MainTimeTable.this,Stats.class);
    		startActivity(intent);
    	}
    	if(item.getItemId()== R.id.resetAll_menu){
    		//-----------------------------------------------------------------
    		AlertDialog.Builder builder=new AlertDialog.Builder(MainTimeTable.this);
			builder.setMessage("Are you sure you want to reset everything?");
			builder.setCancelable(true); //User can enter yes or no.. he can get out of it
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
		    		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE,null);
		    		db.execSQL("DROP TABLE IF EXISTS subjects");
		    		db.execSQL("DROP TABLE IF EXISTS timetable");
		    		db.execSQL("DROP TABLE IF EXISTS attendance");
		    		db.execSQL("DROP TABLE IF EXISTS trivial");
		    		db.execSQL("DROP TABLE IF EXISTS attendanceBackUp");
		    		
		    		Intent intent = new Intent(MainTimeTable.this,Main.class);
		    		startActivity(intent);
		    		MainTimeTable.this.finish();
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
    	
    		//-----------------------------------------------------------------
    	}
    	if(item.getItemId()== R.id.resetAttendance_menu){
    		//---------------------------------------------------------------------
    		AlertDialog.Builder builder=new AlertDialog.Builder(MainTimeTable.this);
			builder.setMessage("Are you sure you want to clear attendance data?");
			builder.setCancelable(true); //User can enter yes or no.. he can get out of it
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE,null);
		    		db.execSQL("DROP TABLE IF EXISTS attendance");
		    		
		    		Intent intent = new Intent(MainTimeTable.this,Main.class);
		    		startActivity(intent);
		    		MainTimeTable.this.finish();
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
    		
    		//--------------------------------------------------------------------------
    	}
    	if(item.getItemId()== R.id.resetTT_menu){
    		//-------------------------------------------------------------------------
    		Intent intent = new Intent(MainTimeTable.this,TT_options.class);
    		startActivity(intent);
    	}
    	if(item.getItemId()== R.id.about_menu){
    		Intent about = new Intent(MainTimeTable.this,About.class);
    		startActivity(about);
    		//MainTimeTable.this.finish();
    	}
    	if(item.getItemId()== R.id.fb_menu){
    		String url = "http://www.facebook.com/pages/Attendance-Manager-for-Android/172865499484854"; 
            Intent i = new Intent(Intent.ACTION_VIEW); // Create a new intent - stating you want to 'view something'
            i.setData(Uri.parse(url));  // Add the url data (allowing android to realise you want to open the browser)
            startActivity(i); // Go go go!
    	}
    	
    	return true;
    }
    public static final void setAppFont(ViewGroup mContainer, Typeface mFont)
    {
        if (mContainer == null || mFont == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i)
        {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView)
            {
                // Set the font if it is a TextView.
                ((TextView) mChild).setTypeface(mFont);
            }
            else if (mChild instanceof ViewGroup)
            {
                // Recursively attempt another ViewGroup.
                setAppFont((ViewGroup) mChild, mFont);
            }
        }
    }
    private String getDateFromCal(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
		
		String Date = year+"-"+month+"-"+day_of_month;
		return Date;
	}
}
