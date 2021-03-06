package org.mtransit.android.provider.permission;

import org.mtransit.android.common.IContext;
import org.mtransit.android.common.RequestCodes;
import org.mtransit.android.commons.MTLog;
import org.mtransit.android.ui.view.common.IActivity;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public abstract class PermissionProviderImpl implements PermissionProvider, MTLog.Loggable {

	private boolean requestedPermissions = false;

	abstract String getMainPermission();

	abstract String[] getAllPermissions();

	@Override
	public boolean permissionsGranted(@NonNull IContext context) {
		return ActivityCompat.checkSelfPermission(context.requireContext(), getMainPermission()) == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public boolean shouldShowRequestPermissionRationale(@NonNull IActivity activity) {
		return ActivityCompat.shouldShowRequestPermissionRationale(activity.requireActivity(), getMainPermission());
	}

	@Override
	public boolean hasRequestedPermissions() {
		return this.requestedPermissions;
	}

	@Override
	public void requestPermissions(@NonNull IActivity activity) {
		this.requestedPermissions = true;
		ActivityCompat.requestPermissions(activity.requireActivity(), getAllPermissions(), RequestCodes.PERMISSIONS_LOCATION_RC);
	}

	@Override
	public boolean handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,
			@NonNull OnPermissionGrantedListener onPermissionGrantedListener) {
		if (requestCode == RequestCodes.PERMISSIONS_LOCATION_RC) {
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				onPermissionGrantedListener.onPermissionGranted();
			}
			return true; // handled
		}
		return false; // not handled
	}
}
