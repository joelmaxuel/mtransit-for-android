package org.mtransit.android.task;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.mtransit.android.commons.MTLog;
import org.mtransit.android.commons.RuntimeUtils;
import org.mtransit.android.commons.data.ServiceUpdate;
import org.mtransit.android.commons.provider.ServiceUpdateProvider.ServiceUpdateFilter;
import org.mtransit.android.commons.task.MTAsyncTask;
import org.mtransit.android.data.DataSourceManager;
import org.mtransit.android.data.DataSourceProvider;
import org.mtransit.android.data.POIManager;
import org.mtransit.android.data.ServiceUpdateProviderProperties;

import android.content.Context;
import android.net.Uri;

public class ServiceUpdateLoader implements MTLog.Loggable {

	private static final String TAG = ServiceUpdateLoader.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	private static ServiceUpdateLoader instance;

	public static ServiceUpdateLoader get() {
		if (instance == null) {
			instance = new ServiceUpdateLoader();
		}
		return instance;
	}

	private ServiceUpdateLoader() {
	}

	private ThreadPoolExecutor fetchServiceUpdateExecutor;

	private static final int CORE_POOL_SIZE = RuntimeUtils.NUMBER_OF_CORES;
	private static final int MAX_POOL_SIZE = RuntimeUtils.NUMBER_OF_CORES;

	public ThreadPoolExecutor getFetchServiceUpdateExecutor() {
		if (this.fetchServiceUpdateExecutor == null) {
			this.fetchServiceUpdateExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
					new LIFOBlockingDeque<Runnable>());
		}
		return fetchServiceUpdateExecutor;
	}

	public boolean isBusy() {
		final boolean busy = this.fetchServiceUpdateExecutor != null && this.fetchServiceUpdateExecutor.getActiveCount() > 0;
		return busy;
	}

	public void clearAllTasks() {
		if (this.fetchServiceUpdateExecutor != null) {
			this.fetchServiceUpdateExecutor.shutdown();
			this.fetchServiceUpdateExecutor = null;
		}
	}

	public boolean findServiceUpdate(Context context, POIManager poim, ServiceUpdateFilter serviceUpdateFilter,
			ServiceUpdateLoader.ServiceUpdateLoaderListener listener, boolean skipIfBusy) {
		if (skipIfBusy && isBusy()) {
			return false;
		}
		Collection<ServiceUpdateProviderProperties> providers = DataSourceProvider.get(context).getTargetAuthorityServiceUpdateProviders(
				poim.poi.getAuthority());
		if (providers != null) {
			for (final ServiceUpdateProviderProperties provider : providers) {
				final ServiceUpdateFetcherCallable task = new ServiceUpdateFetcherCallable(context, listener, provider, poim, serviceUpdateFilter);
				task.executeOnExecutor(getFetchServiceUpdateExecutor());
				break;
			}
		}
		return true;
	}

	private static class ServiceUpdateFetcherCallable extends MTAsyncTask<Void, Void, Collection<ServiceUpdate>> {

		@Override
		public String getLogTag() {
			return TAG;
		}

		private WeakReference<Context> contextWR;
		private ServiceUpdateProviderProperties serviceUpdateProvider;
		private WeakReference<POIManager> poiWR;
		private ServiceUpdateLoader.ServiceUpdateLoaderListener listener;
		private ServiceUpdateFilter serviceUpdateFilter;

		public ServiceUpdateFetcherCallable(Context context, ServiceUpdateLoader.ServiceUpdateLoaderListener listener,
				ServiceUpdateProviderProperties serviceUpdateProvider, POIManager poim, ServiceUpdateFilter serviceUpdateFilter) {
			this.contextWR = new WeakReference<Context>(context);
			this.listener = listener;
			this.serviceUpdateProvider = serviceUpdateProvider;
			this.poiWR = new WeakReference<POIManager>(poim);
			this.serviceUpdateFilter = serviceUpdateFilter;
		}

		@Override
		protected Collection<ServiceUpdate> doInBackgroundMT(Void... params) {
			try {
				return call();
			} catch (Exception e) {
				MTLog.w(this, e, "Error while running task!");
				return null;
			}
		}

		@Override
		protected void onPostExecute(Collection<ServiceUpdate> result) {
			if (result != null) {
				POIManager poim = this.poiWR == null ? null : this.poiWR.get();
				if (poim != null) {
					poim.setServiceUpdates(result);
					if (listener != null) {
						listener.onServiceUpdatesLoaded(poim.poi.getUUID(), poim.getServiceUpdatesOrNull());
					}
				}
			}
		}

		public Collection<ServiceUpdate> call() throws Exception {
			Context context = this.contextWR == null ? null : this.contextWR.get();
			if (context == null) {
				return null;
			}
			POIManager poim = this.poiWR == null ? null : this.poiWR.get();
			if (poim == null) {
				return null;
			}
			if (this.serviceUpdateFilter == null) {
				return null;
			}
			final Uri uri = DataSourceProvider.get(context).getUri(this.serviceUpdateProvider.getAuthority());
			final Collection<ServiceUpdate> serviceUpdates = DataSourceManager.findServiceUpdates(context, uri, this.serviceUpdateFilter);
			return serviceUpdates;
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

	public static interface ServiceUpdateLoaderListener {
		public void onServiceUpdatesLoaded(String targetUUID, ArrayList<ServiceUpdate> serviceUpdates);
	}

}