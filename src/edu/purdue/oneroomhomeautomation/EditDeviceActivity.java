package edu.purdue.oneroomhomeautomation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

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

	public static final int DELETE_BUTTON_ID = 9000;

	public static final int TIME_PICKER_ID = 999;

	public static Device currentDevice;

	private Button submitButton = null;
	private Button cancelButton = null;
	private Button newTimeButton = null;

	private int selectedButtonId = -1;
	
	private int[] loadedSchedules;

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

		showSchedule(true);
	}

	private void showSchedule(boolean loadFromServer) {

		if (loadFromServer) {
			// Clear device schedule list and pull new schedule list from server
			currentDevice.getSchedule().clear();
			try {

				URL server = new URL(
						"http://104.254.216.237/oneroom/phpscripts/getSchedules.php?utilID="
								+ currentDevice.getId());
				URLConnection r = server.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						r.getInputStream()));
				JSONArray resp = new JSONArray(in.readLine());

				Log.d(TAG, "Num schedules: " + resp.length());
				loadedSchedules = new int[resp.length()];
				Log.d(TAG, resp.toString());
				for (int i = 0; i < resp.length(); i++) {

					ScheduledAction temp = null;
					int intTime = resp.getJSONArray(i).getInt(1);
					int hour = intTime / 100;
					int minute = intTime % 100;
					Time time = new Time();
					time.hour = hour;
					time.minute = minute;
					boolean isOn = resp.getJSONArray(i).getInt(2) == 1;
					int schedId = resp.getJSONArray(i).getInt(0);
					temp = new ScheduledAction(time, isOn, schedId);
					loadedSchedules[i] = schedId;

					currentDevice.getSchedule().add(temp);
				}

				in.close();

				// Sort schedules by Time
				Collections.sort(currentDevice.getSchedule());
				Log.d(TAG, "Loaded " + currentDevice.getSchedule().size()
						+ " schedules for device " + currentDevice.getId());
			} catch (Exception e) {
				Log.e(TAG, "Exception", e);
			}
		}

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
			timeButton.setText(String.format("%02d", sa.getTime().hour) + ":"
					+ String.format("%02d", sa.getTime().minute));
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

			Button deleteButton = new Button(EditDeviceActivity.this);
			deleteButton.setText("Delete");
			deleteButton.setId(DELETE_BUTTON_ID + i);
			sa.setDeleteButton(deleteButton);
			deleteButton.setOnClickListener(deleteButtonListener);

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
			// rlp = new RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// rlp.addRule(RelativeLayout.BELOW, timeButton.getId());
			// rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			// settingTextView.setLayoutParams(rlp);
			// rl.addView(settingTextView);

			// Set the ToggleButton layout parameters and add it
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.RIGHT_OF, timeButton.getId());
			rlp.addRule(RelativeLayout.BELOW, timeTextView.getId());
			tb.setLayoutParams(rlp);
			rl.addView(tb);

			// Set the DeleteButton layout parameters and add it
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.RIGHT_OF, tb.getId());
			rlp.addRule(RelativeLayout.BELOW, timeTextView.getId());
			deleteButton.setLayoutParams(rlp);
			rl.addView(deleteButton);

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
			for (int i = 0; i < currentDevice.getSchedule().size(); i++) {
				ScheduledAction sa = currentDevice.getSchedule().get(i);
				if (sa.getTimeButton().getId() == selectedButtonId) {
					Time time = new Time();
					time.hour = hourOfDay;
					time.minute = minute;
					sa.setTime(time);
					Log.v(TAG, "Set time to " + time.hour + ":" + time.minute);
					sa.getTimeButton().setText(
							String.format("%02d", time.hour) + ":"
									+ String.format("%02d", time.minute));
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

	private OnClickListener deleteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			for (int i = 0; i < currentDevice.getSchedule().size(); i++) {
				ScheduledAction sa = currentDevice.getSchedule().get(i);
				if (sa.getDeleteButton().getId() == v.getId()) {
					currentDevice.getSchedule().remove(i);
					// TODO communicate with server to make this setting save
					break;
				}
			}
			showSchedule(false);
		}
	};

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Implement saving of edited device schedule
			
			// Clear all of the old schedules
			for(int i = 0; i < loadedSchedules.length; i++) {
				int resp = -1;
				int state = -1;
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://104.254.216.237/oneroom/phpscripts/deleteSched.php");
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair("scheduleID",
							String.valueOf(loadedSchedules[i])));
					
					Log.d(TAG, "Attempting to delete schedule with id: " + String.valueOf(loadedSchedules[i]));
					
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					Log.d("DEBUG", String.valueOf(state));

					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					String responseString = EntityUtils.toString(entity,
							"UTF-8");
					resp = Integer.parseInt(responseString);

				} catch (Exception e) {
					Log.e(TAG, "EXCEPTION", e);
				}			
			}
			
			// Add all new schedules
			for (int i = 0; i < currentDevice.getSchedule().size(); i++) {
				ScheduledAction sa = currentDevice.getSchedule().get(i);
				
				int resp = -1;
				int state = -1;
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://104.254.216.237/oneroom/phpscripts/createSched.php");
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair("utilID", String
							.valueOf(currentDevice.getId())));
					int intTime = sa.getTime().hour * 100 + sa.getTime().minute;
					String isOn = sa.isOn() ? "1" : "0";
					nameValuePairs.add(new BasicNameValuePair("time", String.valueOf(intTime)));
					nameValuePairs.add(new BasicNameValuePair("state", isOn));

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					Log.d("DEBUG", String.valueOf(state));

					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					String responseString = EntityUtils.toString(entity,
							"UTF-8");
					resp = Integer.parseInt(responseString);

				} catch (Exception e) {
					Log.getStackTraceString(e);
					Log.d("EXCEPTION", e.toString());

				}
			}

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
			currentDevice.getSchedule()
					.add(new ScheduledAction(time, true, -1));
			showSchedule(false);
		}
	};

}
