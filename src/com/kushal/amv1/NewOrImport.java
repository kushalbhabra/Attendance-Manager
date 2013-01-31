package com.kushal.amv1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class NewOrImport extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neworimport);
		setTitle("Attendance Manager-setup");
		Button b = (Button) findViewById(R.id.btn_gotoSub);
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.btn_import);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SQLiteDatabase db = openOrCreateDatabase("am", MODE_PRIVATE, null);
		switch(v.getId()){
		case R.id.btn_gotoSub:
			db.execSQL("CREATE TABLE IF NOT EXISTS subjects(subject_name varchar)");
			Intent intentNotInstalled = new Intent(NewOrImport.this,SubjectList.class);
			startActivity(intentNotInstalled);
			this.finish();
			break;
		case R.id.btn_import:
			ImportDatabaseFileTask im = new ImportDatabaseFileTask();
			im.execute(null);
			break;
		}
	}
	private class ImportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(NewOrImport.this);

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
              Toast.makeText(NewOrImport.this, "Import successful!", Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(NewOrImport.this,Main.class);
              startActivity(intent);
              NewOrImport.this.finish();
           } else {
              Toast.makeText(NewOrImport.this, "Import failed. \n /sdcard/bluetooth/am  not found!", Toast.LENGTH_SHORT).show();
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
