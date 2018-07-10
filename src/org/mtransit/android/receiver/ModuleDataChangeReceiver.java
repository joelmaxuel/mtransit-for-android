package org.mtransit.android.receiver;

import org.mtransit.android.commons.MTLog;
import org.mtransit.android.commons.receiver.DataChange;
import org.mtransit.android.data.DataSourceProvider;
import org.mtransit.android.dev.CrashReporter;
import org.mtransit.android.di.Injection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class ModuleDataChangeReceiver extends BroadcastReceiver implements MTLog.Loggable {

	private static final String LOG_TAG = ModuleDataChangeReceiver.class.getSimpleName();

	@Override
	public String getLogTag() {
		return LOG_TAG;
	}

	@NonNull
	private CrashReporter crashReporter;

	public ModuleDataChangeReceiver() {
		super();
		crashReporter = Injection.providesCrashReporter();
	}

	@Override
	public void onReceive(@NonNull Context context, Intent intent) {
		String action = intent.getAction();
		if (!DataChange.ACTION_DATA_CHANGE.equals(action)) {
			crashReporter.shouldNotHappen("Wrong receiver action '%s'!", action);
			return;
		}
		Bundle extras = intent.getExtras();
		boolean forceReset = extras != null && extras.getBoolean(DataChange.FORCE, false);
		if (forceReset) {
			if (DataSourceProvider.isSet()) {
				DataSourceProvider.triggerModulesUpdated();
			}
		} else {
			DataSourceProvider.resetIfNecessary(context);
		}
	}
}
