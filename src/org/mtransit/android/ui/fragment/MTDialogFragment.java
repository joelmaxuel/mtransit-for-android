package org.mtransit.android.ui.fragment;

import org.mtransit.android.commons.Constants;
import org.mtransit.android.commons.MTLog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * NO LOGIC HERE, just logs.
 */
public abstract class MTDialogFragment extends DialogFragment implements MTLog.Loggable {

	public MTDialogFragment() {
		super();
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "%s()", getLogTag());
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onCreateDialog(%s)", savedInstanceState);
		}
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "show(%s,%s)", manager, tag);
		}
		super.show(manager, tag);
	}

	@Override
	public int show(FragmentTransaction transaction, String tag) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "show(%s,%s)", transaction, tag);
		}
		return super.show(transaction, tag);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onCancel(%s)", dialog);
		}
		super.onCancel(dialog);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onDismiss(%s)", dialog);
		}
		super.onDismiss(dialog);
	}

	// INHERITED FROM FRAGMENT
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onActivityCreated(%s)", savedInstanceState);
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onActivityResult(%s,%s,%s)", requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onAttach(%s)", activity);
		}
		super.onAttach(activity);
	}

	@Override
	public void onAttach(Context context) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onAttach(%s)", context);
		}
		super.onAttach(context);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onConfigurationChanged(%s)", newConfig);
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onCreate(%s)", savedInstanceState);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onCreateView(%s,%s,%s)", inflater, container, savedInstanceState);
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onDestroy()");
		}
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onDestroyView()");
		}
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onDetach()");
		}
		super.onDetach();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onInflate(%s,%s,%s)", activity, attrs, savedInstanceState);
		}
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onInflate(%s,%s,%s)", context, attrs, savedInstanceState);
		}
		super.onInflate(context, attrs, savedInstanceState);
	}

	@Override
	public void onLowMemory() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onLowMemory()");
		}
		super.onLowMemory();
	}

	@Override
	public void onPause() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onPause()");
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onResume()");
		}
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onSaveInstanceState(%s)", outState);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onStart()");
		}
		super.onStart();
	}

	@Override
	public void onStop() {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onStop()");
		}
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onViewCreated(%s, %s)", view, savedInstanceState);
		}
		super.onViewCreated(view, savedInstanceState);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		if (Constants.LOG_LIFECYCLE) {
			MTLog.v(this, "onViewStateRestored(%s)", savedInstanceState);
		}
		super.onViewStateRestored(savedInstanceState);
	}
}
