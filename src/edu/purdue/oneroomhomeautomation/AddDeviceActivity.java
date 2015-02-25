package edu.purdue.oneroomhomeautomation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddDeviceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adddevice);
		Button submitButton = (Button) findViewById(R.id.buttonSubmitNewDevice);
		submitButton.setOnClickListener(submitListener);
		Button cancelButton = (Button) findViewById(R.id.buttonCancelNewDevice);
		cancelButton.setOnClickListener(cancelListener);
		getActionBar().setTitle("Add Device");
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Implement saving of new device 
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

}
