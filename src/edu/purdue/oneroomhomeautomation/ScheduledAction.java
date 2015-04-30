package edu.purdue.oneroomhomeautomation;

import android.text.format.Time;
import android.view.View;
import android.widget.Button;

public class ScheduledAction implements Comparable {

	private Time time;
	private boolean isOn;
	private int schedId;
	private Button timeButton;
	private View toggleButton;
	private Button deleteButton;

	public ScheduledAction(Time time, boolean isOn, int schedId) {
		this.time = time;
		this.isOn = isOn;
		this.schedId = schedId;
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

	public void setId(int schedId) {
		this.schedId = schedId;
	}

	public int getId() {
		return schedId;
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
	
	public void setDeleteButton(View deleteButton) {
		this.deleteButton = (Button) deleteButton;
	}
	
	public Button getDeleteButton() {
		return deleteButton;
	}

	public void toggleSchedule() {
		if (isOn) {
			isOn = false;
		} else {
			isOn = true;
		}
	}

	@Override
	public int compareTo(Object arg0) throws ClassCastException {
		if (!(arg0 instanceof ScheduledAction))
			throw new ClassCastException("A ScheduledAction object expected.");
		Time otherTimeObj = ((ScheduledAction) arg0).getTime();
		int otherTime = otherTimeObj.hour * 100 + otherTimeObj.minute;
		int myTime = getTime().hour * 100 + getTime().minute;
		return myTime - otherTime;
	}

}
