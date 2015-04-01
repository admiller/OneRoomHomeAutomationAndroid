package edu.purdue.oneroomhomeautomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class AddDeviceActivity extends Activity {

	EditText deviceNameEditText;
	ToggleButton deviceToggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adddevice);
		Button submitButton = (Button) findViewById(R.id.buttonSubmitNewDevice);
		submitButton.setOnClickListener(submitListener);
		Button cancelButton = (Button) findViewById(R.id.buttonCancelNewDevice);
		cancelButton.setOnClickListener(cancelListener);
		deviceNameEditText = (EditText) findViewById(R.id.editTextNewDevice);
		deviceToggleButton = (ToggleButton) findViewById(R.id.toggleButtonNewDevice);
		getActionBar().setTitle("Add Device");
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Implement communication with database to permanently store
			// devices
			// Add and save device locally
			Device device = new Device(deviceNameEditText.getText().toString(),
					deviceToggleButton.isChecked());
			Device.getDevices().add(device);

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
