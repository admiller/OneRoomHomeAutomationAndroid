package edu.purdue.oneroomhomeautomation;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditDeviceActivity extends Activity {

	public static final String TAG = "ORHA";

	/**
	 * The base number for id's when they are created dynamically
	 */
	public static final int TEXT_VIEW_ID = 5000;

	public static final int TOGGLE_BUTTON_ID = 6000;

	public static final int EDIT_BUTTON_ID = 7000;

	public static final int TIME_BUTTON_ID = 8000;

	public static final int TIME_PICKER_ID = 999;

	public static Device currentDevice;

	private Button submitButton = null;
	private Button cancelButton = null;
	private Button newTimeButton = null;
	
	private int selectedButtonId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editdevice);
		TextView deviceTitle = (TextView) findViewById(R.id.textViewDeviceTitleEditDevice);
		deviceTitle.setText(currentDevice.getName());
		submitButton = (Button) findViewById(R.id.buttonSubmitEditDevice);
		submitButton.setOnClickListener(submitListener);
		cancelButton = (Button) findViewById(R.id.buttonCancelEditDevice);
		cancelButton.setOnClickListener(cancelListener);
		newTimeButton = (Button) findViewById(R.id.buttonNewTimeEditDevice);
		newTimeButton.setOnClickListener(newTimeListener);
		getActionBar().setTitle("Edit Device");

		showSchedule();
	}

	private void showSchedule() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayoutEditDevice);
		// Clear the layout (except for static items)
		rl.removeViews(4, rl.getChildCount() - 4);

		// keeps track of the lowest toggle button for layout purposes
		ToggleButton bottomToggle = null;

		for (int i = 0; i < currentDevice.getSchedule().size(); i++) {
			ScheduledAction sa = currentDevice.getSchedule().get(i);

			TextView timeTextView = new TextView(EditDeviceActivity.this);
			timeTextView.setText("Scheduled Time");
			timeTextView.setTextAppearance(EditDeviceActivity.this,
					android.R.style.TextAppearance_Large);
			timeTextView.setId(TEXT_VIEW_ID + (i * 2));

			Button timeButton = new Button(EditDeviceActivity.this);
			timeButton.setText(sa.getTime().hour + ":" + sa.getTime().minute);
			timeButton.setId(TIME_BUTTON_ID + i);
			sa.setTimeButton(timeButton);
			timeButton.setOnClickListener(timeListener);

			TextView settingTextView = new TextView(EditDeviceActivity.this);
			settingTextView.setText("Setting");
			settingTextView.setTextAppearance(EditDeviceActivity.this,
					android.R.style.TextAppearance_Large);
			settingTextView.setId(TEXT_VIEW_ID + (i * 2 + 1));

			ToggleButton tb = new ToggleButton(EditDeviceActivity.this);
			tb.setChecked(sa.isOn());
			tb.setId(TOGGLE_BUTTON_ID + i);
			sa.setToggleButton(tb);
			tb.setOnCheckedChangeListener(toggleButtonChangeListener);

			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			// Set the Time TextView layout parameters and add the view
			if (i == 0) { // This is the first device on the list
				rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rlp.addRule(RelativeLayout.BELOW, submitButton.getId());
				timeTextView.setLayoutParams(rlp);
				rl.addView(timeTextView);
			} else { // add the device below the last device
				rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rlp.addRule(RelativeLayout.BELOW, bottomToggle.getId());
				timeTextView.setLayoutParams(rlp);
				rl.addView(timeTextView);
			}

			// Set the TimeButton layout parameters and add it
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.BELOW, timeTextView.getId());
			rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			timeButton.setLayoutParams(rlp);
			rl.addView(timeButton);

			// Set the Setting TextView layout parameters and add it
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.BELOW, timeButton.getId());
			rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			settingTextView.setLayoutParams(rlp);
			rl.addView(settingTextView);

			// Set the ToggleButton layout parameters and add it
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.BELOW, settingTextView.getId());
			tb.setLayoutParams(rlp);
			rl.addView(tb);
			// Set the bottomToggle field
			bottomToggle = tb;

		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_PICKER_ID:
			return new TimePickerDialog(this, timePickerListener, 0, 0, true);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			for(int i = 0; i < currentDevice.getSchedule().size(); i++) {
				ScheduledAction sa = currentDevice.getSchedule().get(i);
				if(sa.getTimeButton().getId() == selectedButtonId) {
					Time time = new Time();
					time.hour = hourOfDay;
					time.minute = minute;
					sa.setTime(time);
					Log.v(TAG, "Set time to " + time.hour + ":" + time.minute);
					sa.getTimeButton().setText(time.hour + ":" + time.minute);
					break;
				}
			}
		}
	};
	
	private OnClickListener timeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			selectedButtonId = v.getId();
			showDialog(TIME_PICKER_ID);
		}
	};
	
	private OnCheckedChangeListener toggleButtonChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton button, boolean isOn) {
			for (int i = 0; i < currentDevice.getSchedule().size(); i++) {
				ScheduledAction sa = currentDevice.getSchedule().get(i);
				if (sa.getToggleButton().getId() == button.getId()) {
					sa.toggleSchedule();
					// TODO communicate with server to make this setting save
					break;
				}
			}
		}
	};

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
			Time time = new Time();
			time.hour = 0;
			time.minute = 0;
			currentDevice.getSchedule().add(new ScheduledAction(time, true));
			showSchedule();
		}
	};

}
