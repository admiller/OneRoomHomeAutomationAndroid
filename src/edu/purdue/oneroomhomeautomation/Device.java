package edu.purdue.oneroomhomeautomation;

import java.util.ArrayList;

import android.view.View;

public class Device implements Comparable {

	// publicly accessible list of devices
	private static ArrayList<Device> devices = null;

	private String name;
	private boolean isOn;
	private int deviceId;
	private ArrayList<ScheduledAction> schedule;
	private View editButton;
	private View toggleButton;

	public Device(String name, boolean isOn, int deviceId) {
		this.name = name;
		this.isOn = isOn;
		this.deviceId = deviceId;
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

	public void setId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public boolean isOn() {
		return isOn;
	}

	public int getId() {
		return deviceId;
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

	@Override
	public int compareTo(Object arg0) throws ClassCastException {
		if (!(arg0 instanceof Device))
			throw new ClassCastException("A Device object expected.");
		int otherDevId = ((Device) arg0).getId();
		return this.deviceId - otherDevId;
	}

}
