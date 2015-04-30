package edu.purdue.oneroomhomeautomation;

import java.util.ArrayList;

import android.view.View;

public class Device {

	// publicly accessible list of devices
	private static ArrayList<Device> devices = null;

	private String name;
	private boolean isOn;
	private ArrayList<ScheduledAction> schedule;
	private View editButton;
	private View toggleButton;

	public Device(String name, boolean isOn) {
		this.name = name;
		this.isOn = isOn;
		schedule = new ArrayList<ScheduledAction>();
		editButton = null;
		toggleButton = null;
	}

	public static ArrayList<Device> getDevices() {
		if (devices == null) {
			devices = new ArrayList<Device>();
		}
		return devices;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIsOn(boolean isOn) {
		this.isOn = isOn;
		
	}

	public String getName() {
		return name;
	}

	public boolean isOn() {
		return isOn;
	}

	public ArrayList<ScheduledAction> getSchedule() {
		return schedule;
	}

	public void setEditButton(View editButton) {
		this.editButton = editButton;
	}

	public View getEditButton() {
		return editButton;
	}

	public void setToggleButton(View toggleButton) {
		this.toggleButton = toggleButton;
	}

	public View getToggleButton() {
		return toggleButton;
	}

	public void toggleDevice() {
		if (isOn) {
			isOn = false;
		} else {
			isOn = true;
		}
	}

}
