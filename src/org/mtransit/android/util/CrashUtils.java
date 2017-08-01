package org.mtransit.android.util;

import org.mtransit.android.BuildConfig;
import org.mtransit.android.commons.MTLog;
import org.mtransit.android.ui.MTApplication;

import android.support.annotation.Nullable;

import com.google.firebase.crash.FirebaseCrash;
import com.squareup.leakcanary.LeakCanary;

public final class CrashUtils implements MTLog.Loggable {

	private static final String TAG = CrashUtils.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	public static void init(MTApplication application) {
		if (BuildConfig.DEBUG) {
			try {
				if (LeakCanary.isInAnalyzerProcess(application)) {
					return;
				}
				LeakCanary.install(application);
			} catch (Exception e) {
				MTLog.w(TAG, "Error while initializing LeakCanary!", e);
			}
		}
	}

	private static void report(String message) {
		report(null, message);
	}

	private static void report(@Nullable Throwable throwable, String message) {
		try {
			FirebaseCrash.log(message);
			if (throwable == null) {
				throwable = new Exception(message);
			}
			FirebaseCrash.report(throwable);
		} catch (Exception e) {
			MTLog.w(TAG, e, "Error while reporting message '%s'!", message);
		}
	}

	public static void w(MTLog.Loggable loggable, String msg, Object... args) {
		MTLog.w(loggable, msg, args);
		report(String.format(msg, args));
	}

	public static void w(String tag, String msg, Object... args) {
		MTLog.w(tag, msg, args);
		report(String.format(msg, args));
	}

	public static void w(MTLog.Loggable loggable, Throwable t, String msg, Object... args) {
		MTLog.w(loggable, t, msg, args);
		report(t, String.format(msg, args));
	}

	public static void w(String tag, Throwable t, String msg, Object... args) {
		MTLog.w(tag, t, msg, args);
		report(t, String.format(msg, args));
	}
}