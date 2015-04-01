package edu.purdue.oneroomhomeautomation;

import android.text.format.Time;
import android.view.View;
import android.widget.Button;

public class ScheduledAction {

	private Time time;
	private boolean isOn;
	private Button timeButton;
	private View toggleButton;

	public ScheduledAction(Time time, boolean isOn) {
		this.time = time;
		this.isOn = isOn;
		timeButton = null;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Time getTime() {
		return time;
	}

	public void setIsOn(boolean isOn) {
		this.isOn = isOn;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setTimeButton(View timeButton) {
		this.timeButton = (Button) timeButton;
	}

	public Button getTimeButton() {
		return timeButton;
	}

	public void setToggleButton(View toggleButton) {
		this.toggleButton = toggleButton;
	}

	public View getToggleButton() {
		return toggleButton;
	}

	public void toggleSchedule() {
		if (isOn) {
			isOn = false;
		} else {
			isOn = true;
		}
	}

}
