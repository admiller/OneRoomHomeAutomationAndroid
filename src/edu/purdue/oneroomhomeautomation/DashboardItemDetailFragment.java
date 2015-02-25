package edu.purdue.oneroomhomeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.purdue.oneroomhomeautomation.dummy.DummyContent;

/**
 * A fragment representing a single DashboardItem detail screen. This fragment
 * is either contained in a {@link DashboardItemListActivity} in two-pane mode
 * (on tablets) or a {@link DashboardItemDetailActivity} on handsets.
 */
public class DashboardItemDetailFragment extends Fragment {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_dashboarditem_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			if (mItem.content.equals("Connected Devices")) {
				rootView = inflater.inflate(
						R.layout.fragment_connecteddevices_detail, container,
						false);
				rootView = createConnectedDevicesContent(rootView);
				Button addButton = (Button) rootView.findViewById(R.id.buttonAddDevice);
				addButton.setOnClickListener(addButtonOnClickListener);
				Button editButton = (Button) rootView
						.findViewById(R.id.buttonEditDevice1);
				editButton.setOnClickListener(editButtonOnClickListener);
				getActivity().getActionBar().setTitle("Connected Devices");
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
