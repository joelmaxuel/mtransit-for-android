package org.mtransit.android.task;

import java.util.ArrayList;

import org.mtransit.android.commons.SqlUtils;
import org.mtransit.android.commons.provider.GTFSProviderContract;
import org.mtransit.android.commons.provider.POIProviderContract;
import org.mtransit.android.data.DataSourceManager;
import org.mtransit.android.data.POIManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RTSTripStopsLoader extends MTAsyncTaskLoaderV4<ArrayList<POIManager>> {

	private static final String TAG = RTSAgencyRoutesLoader.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	private String authority;
	private long tripId;

	@Nullable
	private ArrayList<POIManager> pois;

	public RTSTripStopsLoader(@NonNull Context context, String authority, long tripId) {
		super(context);
		this.authority = authority;
		this.tripId = tripId;
	}

	@Override
	public ArrayList<POIManager> loadInBackgroundMT() {
		if (this.pois != null) {
			return this.pois;
		}
		this.pois = new ArrayList<>();
		POIProviderContract.Filter poiFilter = POIProviderContract.Filter.getNewSqlSelectionFilter(SqlUtils.getWhereEquals(
				GTFSProviderContract.RouteTripStopColumns.T_TRIP_K_ID, this.tripId));
		poiFilter.addExtra(POIProviderContract.POI_FILTER_EXTRA_SORT_ORDER, //
				SqlUtils.getSortOrderAscending(GTFSProviderContract.RouteTripStopColumns.T_TRIP_STOPS_K_STOP_SEQUENCE));
		this.pois = DataSourceManager.findPOIs(getContext(), this.authority, poiFilter);
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
	public void deliverResult(@Nullable ArrayList<POIManager> data) {
		this.pois = data;
		if (isStarted()) {
			super.deliverResult(data);
		}
	}
}
