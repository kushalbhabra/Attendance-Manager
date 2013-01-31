package com.kushal.amv1;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class SlotDialog extends Activity implements OnClickListener{
	ArrayAdapter<String> adapterForSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slotdialog);
		final String day = getIntent().getExtras().getString("day");
		setTitle(day.toUpperCase());
		
		//Getting References
		Spinner spinner=(Spinner) findViewById(R.id.spinner1);
		Button saveadd =(Button) findViewById(R.id.btn_saveadd);
		Button saveproceed =(Button) findViewById(R.id.btn_saveproceed);
		
		//Setting Listeners
		saveadd.setOnClickListener((android.view.View.OnClickListener) this);
		saveproceed.setOnClickListener((android.view.View.OnClickListener) this);
		
		//Setting Initial Time
		TimePicker tp1 = (TimePicker) findViewById(R.id.timePicker1);
		tp1.setCurrentHour(9);
		tp1.setCurrentMinute(0);
		
		//Setting Subject Selection Spinner
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		try{
		Cursor c = db.rawQuery("SELECT * FROM subjects",null);
		adapterForSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
		adapterForSpinner.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item); 
		spinner.setAdapter(adapterForSpinner);
		if(c.moveToFirst()){
			do{
				adapterForSpinner.add(c.getString(c.getColumnIndex("subject_name")));
			}while(c.moveToNext());
		}}catch(Exception e){}
		db.close();
		
		
		Button b = (Button) findViewById(R.id.btn_ResetDay);
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new AlertDialog.Builder(SlotDialog.this);
				builder.setMessage("Are you sure you want to reset "+day+"'s timetable?");
				builder.setCancelable(true); //User has to enter yes or no.. he can't get out of it
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
						db.execSQL("DELETE FROM timetable WHERE day='"+day+"'");
						db.execSQL("UPDATE trivial SET last_lec=0 WHERE day='"+day+"' ");
						db.close();
						Log.d("AM","DELETE FROM timetable WHERE day='"+day+"'");
						SlotDialog.this.finish();
					}
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
			
		});
		/*******************************************************************************/
		//CLASS ENDS HERE... FOLLOWING ARE ITS METHODS
	}
	
	
	@Override
	public void onClick(View v) {
	   SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
	   String day = getIntent().getExtras().getString("day");
	   if(v.getId()==R.id.btn_saveadd){
		   try{
			   
			   Spinner spinner = (Spinner) findViewById(R.id.spinner1);
			   String subject = spinner.getSelectedItem().toString();
			   TimePicker st = (TimePicker) findViewById(R.id.timePicker1);
			   int st_hour = st.getCurrentHour();
			   int st_min = st.getCurrentMinute();
			   
			   String cellno = calculateCellNo(day);
			   String query = "INSERT INTO timetable VALUES('"+cellno+"','"+subject+"','"+day+"',"+st_hour+","+st_min+")";
			   query.concat("'"+cellno+"',");
			   query.concat("'"+subject+"',");
			   query.concat("'"+day+"',");
			   query.concat(st_hour+",");
			   query.concat(st_min+")");
			   
			   db.execSQL(query);
			   Log.d("AM",query);
			   
		   }catch(Exception e){
			   Log.d("AM","SAVE ADD:"+e.toString());
		   }
		   Intent intentcontinue = new Intent(SlotDialog.this,SlotDialog.class);
		   intentcontinue.putExtra("day", day);
		   startActivity(intentcontinue);
		   SlotDialog.this.finish();
	   }
	   else if(v.getId()==R.id.btn_saveproceed){
		   try{	   
			   Spinner spinner = (Spinner) findViewById(R.id.spinner1);
			   String subject = spinner.getSelectedItem().toString();
			   TimePicker st = (TimePicker) findViewById(R.id.timePicker1);
			   int st_hour = st.getCurrentHour();
			   int st_min = st.getCurrentMinute();
			
			   
			   String cellno = calculateCellNo(day);
			   String query = "INSERT INTO timetable VALUES('"+cellno+"','"+subject+"','"+day+"',"+st_hour+","+st_min+")";
			   query.concat("'"+cellno+"',");
			   query.concat("'"+subject+"',");
			   query.concat("'"+day+"',");
			   query.concat(st_hour+",");
			   query.concat(st_min+")");
			   
			   db.execSQL(query);
			   Log.d("AM",query);
			   SlotDialog.this.finish();
			   
		   }catch(Exception e){
			   Log.d("AM", "SAVE PROCEED:"+e.toString());
		   }  
		   db.close();
	   }
	   db.close();
	}

	private String calculateCellNo(String day) {
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM trivial WHERE day='"+day+"'", null);
		c.moveToFirst();
		int lec_no=c.getInt(c.getColumnIndex("last_lec"));
		lec_no++;
		int day_code=c.getInt(c.getColumnIndex("day_code"));
		String cellno=day_code+" "+lec_no;
		db.execSQL("UPDATE trivial SET last_lec="+lec_no+" WHERE day='"+day+"'");
		c.close();
		db.close();
		return cellno;
	}
	
	
}
