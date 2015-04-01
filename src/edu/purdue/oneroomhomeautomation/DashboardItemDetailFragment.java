package edu.purdue.oneroomhomeautomation;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import edu.purdue.oneroomhomeautomation.dummy.DummyContent;

/**
 * A fragment representing a single DashboardItem detail screen. This fragment
 * is either contained in a {@link DashboardItemListActivity} in two-pane mode
 * (on tablets) or a {@link DashboardItemDetailActivity} on handsets.
 */
public class DashboardItemDetailFragment extends Fragment {

	/**
	 * The base number for id's when they are created dynamically
	 */
	public static final int TEXT_VIEW_ID = 5000;

	public static final int TOGGLE_BUTTON_ID = 6000;

	public static final int EDIT_BUTTON_ID = 7000;

	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public DashboardItemDetailFragment() {
	}

	public View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showConnectedDevices();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_dashboarditem_detail,
				container, false);

		if (mItem != null) {
			if (mItem.content.equals("Connected Devices")) {
				rootView = inflater.inflate(
						R.layout.fragment_connecteddevices_detail, container,
						false);
				showConnectedDevices();
			} else if (mItem.content.equals("Account Settings")) {
				rootView = inflater.inflate(
						R.layout.fragment_accountsettings_detail, container,
						false);
				rootView = createAccountSettingsContent(rootView);
				getActivity().getActionBar().setTitle("Account Settings");
			} else if (mItem.content.equals("Logout")) {
				rootView = inflater.inflate(R.layout.fragment_logout_detail,
						container, false);
				rootView = createLogoutContent(rootView);
				Button logoutButton = (Button) rootView
						.findViewById(R.id.buttonLogout);
				logoutButton.setOnClickListener(logoutButtonOnClickListener);
				getActivity().getActionBar().setTitle("Logout?");
			} else {
				((TextView) rootView.findViewById(R.id.dashboarditem_detail))
						.setText(mItem.content);
			}
		}

		return rootView;
	}

	public void showConnectedDevices() {
		rootView = createConnectedDevicesContent(rootView);
		RelativeLayout rl = (RelativeLayout) rootView
				.findViewById(R.id.relativeLayoutConnectedDevices);
		rl.removeViews(2, rl.getChildCount() - 2);

		Button addButton = (Button) rootView.findViewById(R.id.buttonAddDevice);
		addButton.setOnClickListener(addButtonOnClickListener);

		// TODO get the list of devices from the server. Hard coded atm
		if (Device.getDevices().size() == 0) {
			Device lightsDevice = new Device("Lights", false);
			Device cameraDevice = new Device("Camera", true);

			Device.getDevices().add(lightsDevice);
			Device.getDevices().add(cameraDevice);
		}

		// keeps track of the lowest toggle button for layout purposes
		ToggleButton bottomToggle = null;
		Button bottomEdit = null;
		// Loop through devices
		for (int i = 0; i < Device.getDevices().size(); i++) {
			Device device = Device.getDevices().get(i);

			// Create and set the TextView's text
			TextView tv = new TextView(getActivity());
			tv.setText("Device " + (i + 1) + ": " + device.getName());
			tv.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Large);
			tv.setId(TEXT_VIEW_ID + i);

			ToggleButton tb = new ToggleButton(getActivity());
			tb.setChecked(device.isOn());
			tb.setId(TOGGLE_BUTTON_ID + i);
			device.setToggleButton(tb);

			Button eb = new Button(getActivity());
			eb.setText("Edit");
			eb.setId(EDIT_BUTTON_ID + i);
			device.setEditButton(eb);

			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			// Set the TextView layout parameters and add the view
			if (i == 0) { // This is the first device on the list
				rlp.addRule(RelativeLayout.BELOW, addButton.getId());
				tv.setLayoutParams(rlp);
				rl.addView(tv);
			} else { // add the device below the last device
				rlp.addRule(RelativeLayout.BELOW, bottomToggle.getId());
				tv.setLayoutParams(rlp);
				rl.addView(tv);
			}

			// Set the ToggleButton layout parameters and add it
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.BELOW, tv.getId());
			tb.setLayoutParams(rlp);
			rl.addView(tb);
			// Set the bottomToggle field
			bottomToggle = tb;

			// Set the EditButton layout parameters and add it
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.ALIGN_BASELINE, tb.getId());
			rlp.addRule(RelativeLayout.ALIGN_BOTTOM, tb.getId());
			if (bottomEdit != null) {
				rlp.addRule(RelativeLayout.ALIGN_RIGHT, bottomEdit.getId());
			} else {
				rlp.addRule(RelativeLayout.RIGHT_OF, tv.getId());
			}
			eb.setLayoutParams(rlp);
			rl.addView(eb);
			// Set the bottomEdit field
			bottomEdit = eb;
		}

		// Button editButton = (Button) rootView
		// .findViewById(R.id.buttonEditDevice1);
		// editButton.setOnClickListener(editButtonOnClickListener);
		getActivity().getActionBar().setTitle("Connected Devices");
	}

	public View createConnectedDevicesContent(View rootView) {
		return ((LinearLayout) rootView
				.findViewById(R.id.connecteddevices_detail));
	}

	public View createAccountSettingsContent(View rootView) {
		return ((LinearLayout) rootView
				.findViewById(R.id.accountsettings_detail));
	}

	public View createLogoutContent(View rootView) {
		return ((LinearLayout) rootView.findViewById(R.id.logout_detail));
	}

	private OnClickListener logoutButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent loginIntent = new Intent(getActivity(),
					LoginScreenActivity.class);
			startActivity(loginIntent);
			getActivity().finish();
		}
	};

	private OnClickListener editButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent editIntent = new Intent(getActivity(),
					EditDeviceActivity.class);
			startActivity(editIntent);
		}
	};

	private OnClickListener addButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent addIntent = new Intent(getActivity(),
					AddDeviceActivity.class);
			startActivity(addIntent);
		}
	};
}
