package com.kushal.amv1;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LectureArrayAdapter extends ArrayAdapter<Lecture> {

	// declaring our ArrayList of Lectures
	private ArrayList<Lecture> objects;

	/* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Lecture> objects,
	* because it is the list of objects we want to display.
	*/
	public LectureArrayAdapter(Context context, int textViewResourceId, ArrayList<Lecture> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list Lecture will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.lectureinlist, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Lecture object.
		 */
		Lecture i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.
			TextView subject = (TextView) v.findViewById(R.id.textView1);
			TextView time = (TextView) v.findViewById(R.id.textView2);
			
			
			subject.setText(i.getSubject());
			time.setText(i.getTime());
			
			String atn = i.getAtn();
			if(atn.equals("y")){
				v.setBackgroundColor(0xDD007700);
			}
			if(atn.equals("n")){
				v.setBackgroundColor(0xDD770000);
			}
			if(atn.equals("na")){
				v.setBackgroundColor(0xDD777777);
			}
			
			
			
		}

		// the view must be returned to our activity
		
		return v;

	}

}
