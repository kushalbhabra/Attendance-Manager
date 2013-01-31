package com.kushal.amv1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TT_options extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_options);
		setTitle("Timetable options");
		Button b = (Button) findViewById(R.id.btn_rst);
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.btn_exp);
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.btn_imp);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_rst:
				AlertDialog.Builder builder=new AlertDialog.Builder(TT_options.this);
				builder.setMessage("Do you wish to save your current attendance data?");
				builder.setCancelable(true); //User can enter yes or no.. he can get out of it
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Toast t=Toast.makeText(MainTimeTable.this, "I hv to write this code..", 2000);
						//t.show();
						try{
							SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE,null);
							db.execSQL("DROP TABLE IF EXISTS attendanceBackUp");
							db.execSQL("CREATE TABLE IF NOT EXISTS attendanceBackUp(cellNo varchar,thedate date,day varchar,subject varchar,atn varchar)");
							db.execSQL("INSERT INTO attendanceBackUp SELECT * FROM attendance WHERE atn='y' OR atn='n'");
							//Log.d("BACKUPDONE", "Backup Successfull");
						}catch(Exception e){
							//Log.d("BACKUPEXCEP", e.toString());
						}
						SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE,null);
			    		db.execSQL("DROP TABLE IF EXISTS timetable");
			    		db.execSQL("DROP TABLE IF EXISTS attendance");
			    		db.execSQL("DROP TABLE IF EXISTS trivial");
			    		
			    		Intent intent = new Intent(TT_options.this,Main.class);
			    		startActivity(intent);
			    		TT_options.this.finish();
			    		
			    		
					}
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE,null);
			    		db.execSQL("DROP TABLE IF EXISTS timetable");
			    		db.execSQL("DROP TABLE IF EXISTS attendance");
			    		db.execSQL("DROP TABLE IF EXISTS trivial");
			    		db.execSQL("DROP TABLE IF EXISTS attendanceBackUp");
			    		Intent intent = new Intent(TT_options.this,Main.class);
			    		startActivity(intent);
			    		TT_options.this.finish();
			    		
					}
				});
				
				AlertDialog alert = builder.create();
				alert.show();
	    		
				break;
			case R.id.btn_exp:
				ExportDatabaseFileTask ex = new ExportDatabaseFileTask();
				ex.execute(null);
				break;
			case R.id.btn_imp:
				ImportDatabaseFileTask im = new ImportDatabaseFileTask();
				im.execute(null);
				break;
			
		}
	
	}
	private class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
	        private final ProgressDialog dialog = new ProgressDialog(TT_options.this);

	        // can use UI thread here
	        protected void onPreExecute() {
	           this.dialog.setMessage("Exporting database...");
	           this.dialog.show();
	        }

	        // automatically done on worker thread (separate from UI thread)
	        protected Boolean doInBackground(final String... args) {

	           File dbFile = new File(Environment.getDataDirectory() + "/data/com.kushal.amv1/databases/am");

	           File exportDir = new File(Environment.getExternalStorageDirectory(), "");
	           if (!exportDir.exists()) {
	              exportDir.mkdirs();
	           }
	           File file = new File(exportDir, dbFile.getName());

	           try {
	              file.createNewFile();
	              this.copyFile(dbFile, file);
	              return true;
	           } catch (IOException e) {
	              //Log.e("mypck", e.getMessage(), e);
	              return false;
	           }
	        }

	        // can use UI thread here
	        protected void onPostExecute(final Boolean success) {
	           if (this.dialog.isShowing()) {
	              this.dialog.dismiss();
	           }
	           if (success) {
	              Toast.makeText(TT_options.this, "Exported successfully to /sdcard!" , Toast.LENGTH_SHORT).show();
	           } else {
	              Toast.makeText(TT_options.this, "Export failed", Toast.LENGTH_SHORT).show();
	           }
	           
	        }

	        void copyFile(File src, File dst) throws IOException {
	           FileChannel inChannel = new FileInputStream(src).getChannel();
	           FileChannel outChannel = new FileOutputStream(dst).getChannel();
	           try {
	              inChannel.transferTo(0, inChannel.size(), outChannel);
	           } finally {
	              if (inChannel != null)
	                 inChannel.close();
	              if (outChannel != null)
	                 outChannel.close();
	           }
	        }
	        
	   
	 }
	private class ImportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(TT_options.this);

        // can use UI thread here
        protected void onPreExecute() {
           this.dialog.setMessage("Importing database...");
           this.dialog.show();
        }

        // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final String... args) {

           File exportDir= new File(Environment.getDataDirectory() + "/data/com.kushal.amv1/databases/");

           File dbFile = new File(Environment.getExternalStorageDirectory(), "/bluetooth/am");
           if (!exportDir.exists()) {
              exportDir.mkdirs();
           }
           File file = new File(exportDir, dbFile.getName());

           try {
              file.createNewFile();
              this.copyFile(dbFile, file);
              return true;
           } catch (IOException e) {
              //Log.e("mypck", e.getMessage(), e);
              return false;
           }
        }

        // can use UI thread here
        protected void onPostExecute(final Boolean success) {
           if (this.dialog.isShowing()) {
              this.dialog.dismiss();
           }
           if (success) {
              Toast.makeText(TT_options.this, "Import successful! \n Pls Restart Application", Toast.LENGTH_SHORT).show();
           } else {
              Toast.makeText(TT_options.this, "Import failed. \n /sdcard/bluetooth/am  not found!", Toast.LENGTH_SHORT).show();
           }
           
        }

        void copyFile(File src, File dst) throws IOException {
           FileChannel inChannel = new FileInputStream(src).getChannel();
           FileChannel outChannel = new FileOutputStream(dst).getChannel();
           try {
              inChannel.transferTo(0, inChannel.size(), outChannel);
           } finally {
              if (inChannel != null)
                 inChannel.close();
              if (outChannel != null)
                 outChannel.close();
           }
        }
        
   
 }
}
