/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 *
 * This is a small demo application which demonstrates the use of the
 * dateSelector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kushal.amv1;

import java.util.Calendar;

import com.googlecode.android.widgets.DateSlider.AlternativeDateSlider;
import com.googlecode.android.widgets.DateSlider.CustomDateSlider;
import com.googlecode.android.widgets.DateSlider.DateSlider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Small Demo activity which demonstrates the use of the DateSlideSelector
 *
 * @author Daniel Berndt - Codeus Ltd
 *
 */
public class MinimalDemo extends Activity {

static final int ALTDATESELECTOR_ID = 1;

    private TextView dateText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // load and initialise the Activity
        super.onCreate(savedInstanceState);
        
         showDialog(ALTDATESELECTOR_ID);
    }

    // define the listener which is called once a user selected the date.
    private DateSlider.OnDateSetListener mDateSetListener =
        new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                // update the dateText view with the corresponding date
                //dateText.setText(String.format("The chosen date:%n%te. %tB %tY", selectedDate, selectedDate, selectedDate));
            	Toast.makeText(MinimalDemo.this, String.format("The chosen date:%n%te. %tB %tY", selectedDate, selectedDate, selectedDate), 10000).show();
            	startActivity(new Intent(MinimalDemo.this,Main.class));
            }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        // this method is called after invoking 'showDialog' for the first time
        // here we initiate the corresponding DateSlideSelector and return the dialog to its caller
    	
    	// get today's date and time
        final Calendar c = Calendar.getInstance();
        
        switch (id) {
        case ALTDATESELECTOR_ID:
            return new AlternativeDateSlider(this,mDateSetListener,c);
        }
        return null;
    }
}