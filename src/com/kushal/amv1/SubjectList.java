package com.kushal.amv1;

import com.kushal.amv1.Main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SubjectList extends Activity implements OnClickListener{
	public EditText et1;
	public EditText et2;
	public EditText et3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subjectlist);
		setTitle("Step 1");
		Button b = (Button) findViewById(R.id.button1); //Save and Add Subject button
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.button3); //Save and proceed button
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.button2); //Clear all subjects button
		b.setOnClickListener(this);			
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.EditText01);
		et3 = (EditText) findViewById(R.id.EditText02);
		Toast toast = Toast.makeText(this, "Hint : Consider lectures and practicals of same subject as different subjects.", 10000);
		toast.show();
	}

	@Override
	public void onClick(View v) {
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		if(v.getId()==R.id.button1){ // Save and Add
			int num=0;
			String sub1 = new String(et1.getText().toString());
			try{if(!checkinvalid(sub1)){
				db.execSQL("INSERT INTO subjects VALUES('"+sub1+"')");
				num++;
			}
			String sub2 = new String(et2.getText().toString());
			if(!checkinvalid(sub2)){
				db.execSQL("INSERT INTO subjects VALUES('"+sub2+"')");
				num++;
			}
			String sub3 = new String(et3.getText().toString());
			if(!checkinvalid(sub3)){
				db.execSQL("INSERT INTO subjects VALUES('"+sub3+"')");
				num++;
			}}catch(Exception e){}
			//Log.d("DB", num+" subject(s) added.");
			Toast toast = Toast.makeText(SubjectList.this, num+" subject(s) added.", 2000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			Intent intent = new Intent(SubjectList.this, SubjectList.class);
			startActivity(intent);
			SubjectList.this.finish();
		}
		else if (v.getId()==R.id.button2) {
			et1.setText("");
			et2.setText("");
			et3.setText("");
		}	
		else if (v.getId()==R.id.button3){
			int num=0;
			String sub1 = new String(et1.getText().toString());
			try{if(!checkinvalid(sub1)){
				db.execSQL("INSERT INTO subjects VALUES('"+sub1+"')");
				num++;
			}
			String sub2 = new String(et2.getText().toString());
			if(!checkinvalid(sub2)){
				db.execSQL("INSERT INTO subjects VALUES('"+sub2+"')");
				num++;
			}
			String sub3 = new String(et3.getText().toString());
			if(!checkinvalid(sub3)){
				db.execSQL("INSERT INTO subjects VALUES('"+sub3+"')");
				num++;
			}}catch(Exception e){}
			//Log.d("DB", num+" subject(s) added.");
			Toast toast = Toast.makeText(SubjectList.this, num+" subject(s) added.", 2000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			Intent intent = new Intent(SubjectList.this, FeedTimetable.class);
			startActivity(intent);
			SubjectList.this.finish();
		}
	}
	//Function to check whether subject is entered is blank or null
	public static boolean checkinvalid(String s){
		int no_of_spaces=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)==' '){
				no_of_spaces++;
			}
		}
		return no_of_spaces==s.length() || s.equals("");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	@Override
	public void onBackPressed() {
		
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF EXISTS subjects");
		db.close();
		super.onBackPressed();
	}
	
}
