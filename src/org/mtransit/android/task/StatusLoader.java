package org.mtransit.android.task;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.mtransit.android.commons.MTLog;
import org.mtransit.android.commons.RuntimeUtils;
import org.mtransit.android.commons.data.POIStatus;
import org.mtransit.android.commons.provider.StatusFilter;
import org.mtransit.android.commons.task.MTAsyncTask;
import org.mtransit.android.data.DataSourceManager;
import org.mtransit.android.data.DataSourceProvider;
import org.mtransit.android.data.POIManager;
import org.mtransit.android.data.StatusProviderProperties;

import android.content.Context;

public class StatusLoader implements MTLog.Loggable {

	private static final String TAG = StatusLoader.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	private static StatusLoader instance;

	public static StatusLoader get() {
		if (instance == null) {
			instance = new StatusLoader();
		}
		return instance;
	}

	private StatusLoader() {
	}

	private ThreadPoolExecutor fetchStatusExecutor;


	private static final int CORE_POOL_SIZE = RuntimeUtils.NUMBER_OF_CORES > 1 ? RuntimeUtils.NUMBER_OF_CORES / 2 : 1;

	private static final int MAX_POOL_SIZE = RuntimeUtils.NUMBER_OF_CORES > 1 ? RuntimeUtils.NUMBER_OF_CORES / 2 : 1;
	public ThreadPoolExecutor getFetchStatusExecutor() {
		if (this.fetchStatusExecutor == null) {
			this.fetchStatusExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
					new LIFOBlockingDeque<Runnable>());
		}
		return fetchStatusExecutor;
	}

	public boolean isBusy() {
		final boolean busy = this.fetchStatusExecutor != null && this.fetchStatusExecutor.getActiveCount() > 0;
		return busy;
	}

	public void clearAllTasks() {
		if (this.fetchStatusExecutor != null) {
			this.fetchStatusExecutor.shutdown();
			this.fetchStatusExecutor = null;
		}
	}

	public boolean findStatus(Context context, POIManager poim, StatusFilter statusFilter, StatusLoader.StatusLoaderListener listener, boolean skipIfBusy) {
		if (skipIfBusy && isBusy()) {
			return false;
		}
		Collection<StatusProviderProperties> providers = DataSourceProvider.get(context).getTargetAuthorityStatusProviders(poim.poi.getAuthority());
		if (providers != null) {
			for (final StatusProviderProperties provider : providers) {
				final StatusFetcherCallable task = new StatusFetcherCallable(context, listener, provider, poim, statusFilter); // , null, timestamp);
				task.executeOnExecutor(getFetchStatusExecutor());
				break;
			}
		}
		return true;
	}

	private static class StatusFetcherCallable extends MTAsyncTask<Void, Void, POIStatus> {

		@Override
		public String getLogTag() {
			return TAG;
		}

		private WeakReference<Context> contextWR;
		private StatusProviderProperties statusProvider;
		private WeakReference<POIManager> poiWR;
		private StatusLoader.StatusLoaderListener listener;
		private StatusFilter statusFilter;

		public StatusFetcherCallable(Context context, StatusLoader.StatusLoaderListener listener, StatusProviderProperties statusProvider, POIManager poim,
				StatusFilter statusFilter) {
			this.contextWR = new WeakReference<Context>(context);
			this.listener = listener;
			this.statusProvider = statusProvider;
			this.poiWR = new WeakReference<POIManager>(poim);
			this.statusFilter = statusFilter;
		}

		@Override
		protected POIStatus doInBackgroundMT(Void... params) {
			try {
				return call();
			} catch (Exception e) {
				MTLog.w(this, e, "Error while running task!");
				return null;
			}
		}

		@Override
		protected void onPostExecute(POIStatus result) {
			if (result != null) {
				POIManager poim = this.poiWR == null ? null : this.poiWR.get();
				if (poim != null) {
					poim.setStatus(result);
					if (listener != null) {
						listener.onStatusLoaded(result);
					} else {
						MTLog.d(this, "onPostExecute() > listener null!");
					}
				} else {
					MTLog.d(this, "onPostExecute() > rts null!");
				}
			} else {
				MTLog.d(this, "onPostExecute() > result null!");
			}
		}

		public POIStatus call() throws Exception {
			Context context = this.contextWR == null ? null : this.contextWR.get();
			if (context == null) {
				return null;
			}
			POIManager poim = this.poiWR == null ? null : this.poiWR.get();
			if (poim == null) {
				return null;
			}
			if (this.statusFilter == null) {
				return null;
			}
			POIStatus status = DataSourceManager.findStatus(context, this.statusProvider.getAuthority(), this.statusFilter);
			return status;
		}

	}

	public static class LIFOBlockingDeque<E> extends LinkedBlockingDeque<E> implements MTLog.Loggable {

		private static final String TAG = LIFOBlockingDeque.class.getSimpleName();

		@Override
		public String getLogTag() {
			return TAG;
		}

		private static final long serialVersionUID = -470545646554946137L;

		@Override
		public boolean offer(E e) {
			return super.offerFirst(e);
		}

		@Override
		public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
			return super.offerFirst(e, timeout, unit);
		}

		@Override
		public boolean add(E e) {
			return super.offerFirst(e);
		}

		@Override
		public void put(E e) throws InterruptedException {
			super.putFirst(e);
		}
	}

	public static interface StatusLoaderListener {
		public void onStatusLoaded(POIStatus status);
	}

}
