package org.mtransit.android.provider.location;

import org.mtransit.android.ui.view.common.IActivity;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface MTLocationProvider {

	void doSetupIfRequired(@NonNull ScreenWithLocationView screenWithLocationView);

	boolean needsSetup(@NonNull ScreenWithLocationView screenWithLocationView);

	void doSetup(@NonNull ScreenWithLocationView screenWithLocationView);

	boolean handleRequestPermissionsResult(@NonNull ScreenWithLocationView screenWithLocationView,
										   int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

	@Nullable
	Location getLastLocationOrNull();

	void readLastLocation();

	void addOnLastLocationChangeListener(@NonNull OnLastLocationChangeListener onLastLocationChangeListener);

	void removeOnLastLocationChangeListener(@NonNull OnLastLocationChangeListener onLastLocationChangeListener);

	interface OnPermissionsPreRequest {
		void onPermissionsPreRequestPositiveBtnClick(@NonNull ScreenWithLocationView screenWithLocationView);

		@SuppressWarnings("unused")
		void onPermissionsPreRequestNegativeBtnClick(@NonNull ScreenWithLocationView screenWithLocationView);
	}

	interface OnPermissionsRationale {
		void onPermissionsRationalePositiveBtnClick(@NonNull ScreenWithLocationView screenWithLocationView);

		void onPermissionsRationaleNegativeBtnClick(@NonNull ScreenWithLocationView screenWithLocationView);
	}

	interface OnPermissionsPermanentlyDenied {
		void onPermissionsPermanentlyDeniedPositiveBtnClick(@NonNull ScreenWithLocationView screenWithLocationView);

		void onPermissionsPermanentlyDeniedNegativeBtnClick(@NonNull ScreenWithLocationView screenWithLocationView);
	}

	interface ScreenWithLocationView extends IActivity {
		void showPermissionsPreRequest(@NonNull MTLocationProvider.OnPermissionsPreRequest listener);

		void showPermissionsRationale(@NonNull MTLocationProvider.OnPermissionsRationale listener);

		void showPermissionsPermanentlyDenied(@NonNull MTLocationProvider.OnPermissionsPermanentlyDenied listener);

		void showApplicationDetailsSettingsScreen();
	}

	interface OnLastLocationChangeListener {
		void onLastLocationChanged(@Nullable Location lastLocation);
	}
}
