package org.mtransit.android.task;

import java.util.ArrayList;

import org.mtransit.android.commons.UriUtils;
import org.mtransit.android.commons.data.Trip;
import org.mtransit.android.commons.provider.GTFSRouteTripStopProvider;
import org.mtransit.android.commons.provider.POIFilter;
import org.mtransit.android.commons.task.MTAsyncTaskLoaderV4;
import org.mtransit.android.data.DataSourceManager;
import org.mtransit.android.data.POIManager;

import android.content.Context;
import android.net.Uri;

public class RTSTripStopsLoader extends MTAsyncTaskLoaderV4<ArrayList<POIManager>> {

	private static final String TAG = RTSAgencyRoutesLoader.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	private Trip trip;
	private ArrayList<POIManager> pois;
	private String authority;

	public RTSTripStopsLoader(Context context, Trip trip, String authority) {
		super(context);
		this.trip = trip;
		this.authority = authority;
	}

	@Override
	public ArrayList<POIManager> loadInBackgroundMT() {
		if (this.pois != null) {
			return this.pois;
		}
		this.pois = new ArrayList<POIManager>();
		final Uri contentUri = UriUtils.newContentUri(this.authority);
		POIFilter poiFilter = new POIFilter(GTFSRouteTripStopProvider.RouteTripStopColumns.T_TRIP_K_ID + "=" + this.trip.id);
		poiFilter.addExtra("sortOrder", GTFSRouteTripStopProvider.RouteTripStopColumns.T_TRIP_STOPS_K_STOP_SEQUENCE + " ASC");
		this.pois = DataSourceManager.findPOIs(getContext(), contentUri, poiFilter);
		return pois;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if (this.pois != null) {
			deliverResult(this.pois);
		}
		if (this.pois == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

	@Override
	public void deliverResult(ArrayList<POIManager> data) {
		this.pois = data;
		if (isStarted()) {
			super.deliverResult(data);
		}
	}

}
