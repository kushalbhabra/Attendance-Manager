package com.kushal.amv1;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class FeedTimetable extends Activity  implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_timetable);
		setTitle("Step 2");
		Button b=(Button) findViewById(R.id.btn_monday);
		b.setOnClickListener(this);
		b=(Button) findViewById(R.id.btn_tuesday);
		b.setOnClickListener(this);
		b=(Button) findViewById(R.id.btn_wednesday);
		b.setOnClickListener(this);
		b=(Button) findViewById(R.id.btn_thursday);
		b.setOnClickListener(this);
		b=(Button) findViewById(R.id.btn_friday);
		b.setOnClickListener(this);
		b=(Button) findViewById(R.id.btn_saturday);
		b.setOnClickListener(this);
		b=(Button) findViewById(R.id.btn_sunday);
		b.setOnClickListener(this);
		b=(Button) findViewById(R.id.btn_finish);
		b.setOnClickListener(this);
		
		try{
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		String createTT=new String("CREATE TABLE IF NOT EXISTS timetable(cellno varchar primary key,subject varchar,day varchar,st_hour int,st_min int)");

		db.execSQL(createTT);
		db.execSQL("CREATE TABLE IF NOT EXISTS trivial(day varchar,day_code int,last_lec int)");
		db.execSQL("INSERT INTO trivial VALUES('monday',2,0)");
		db.execSQL("INSERT INTO trivial VALUES('tuesday',3,0)");
		db.execSQL("INSERT INTO trivial VALUES('wednesday',4,0)");
		db.execSQL("INSERT INTO trivial VALUES('thursday',5,0)");
		db.execSQL("INSERT INTO trivial VALUES('friday',6,0)");
		db.execSQL("INSERT INTO trivial VALUES('saturday',7,0)");
		db.execSQL("INSERT INTO trivial VALUES('sunday',1,0)");
		db.close();
		}catch(Exception e){
		   //	Toast t=Toast.makeText(this, (CharSequence) e, 2000);
			//t.show();
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(FeedTimetable.this,SlotDialog.class);
		if(v.getId()==R.id.btn_monday){
			intent.putExtra("day", "monday");
			startActivity(intent);
		}
		if(v.getId()==R.id.btn_tuesday){
			intent.putExtra("day", "tuesday");
			startActivity(intent);
		}
		if(v.getId()==R.id.btn_wednesday){
			intent.putExtra("day", "wednesday");
			startActivity(intent);
		}
		if(v.getId()==R.id.btn_thursday){
			intent.putExtra("day", "thursday");
			startActivity(intent);
		}
		if(v.getId()==R.id.btn_friday){
			intent.putExtra("day", "friday");
			startActivity(intent);
		}
		if(v.getId()==R.id.btn_saturday){
			intent.putExtra("day", "saturday");
			startActivity(intent);
		}
		if(v.getId()==R.id.btn_sunday){
			intent.putExtra("day", "sunday");
			startActivity(intent);
		}
		if(v.getId()==R.id.btn_finish){
			
			Intent intentdone = new Intent(FeedTimetable.this,StartDate.class);
			startActivity(intentdone);
			FeedTimetable.this.finish();
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
		db.execSQL("DROP TABLE IF EXISTS timetable");
		db.execSQL("DROP TABLE IF EXISTS trivial");
		db.close();
		super.onBackPressed();
	}

}
