package edu.purdue.oneroomhomeautomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EditDeviceActivity extends Activity {
	
	public static Device currentDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editdevice);
		Button submitButton = (Button) findViewById(R.id.buttonSubmitEditDevice);
		submitButton.setOnClickListener(submitListener);
		Button cancelButton = (Button) findViewById(R.id.buttonCancelEditDevice);
		cancelButton.setOnClickListener(cancelListener);
		Button newTimeButton = (Button) findViewById(R.id.buttonNewTimeEditDevice);
		getActionBar().setTitle("Edit Device");
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Implement saving of edited device schedule
			// Close editing page
			finish();
		}
	};

	private OnClickListener cancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Close editing page
			finish();
		}
	};
	
	private OnClickListener newTimeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	};

}
