package org.mtransit.android.task;

import java.util.List;

import org.mtransit.android.commons.CollectionUtils;
import org.mtransit.android.commons.LocationUtils;
import org.mtransit.android.commons.MTLog;
import org.mtransit.android.commons.data.POI;
import org.mtransit.android.commons.task.MTCallable;
import org.mtransit.android.data.DataSourceProvider;

import android.content.Context;
import android.net.Uri;

public class FindNearbyAgencyPOIsTask extends MTCallable<List<? extends POI>> {

	private static final String TAG = FindNearbyAgencyPOIsTask.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	private Context context;
	private Uri contentUri;
	private double lat;
	private double lng;
	private double aroundDiff;
	private boolean hideDecentOnly;
	private int maxSize;
	private int minCoverage;

	public FindNearbyAgencyPOIsTask(Context context, Uri contentUri, double lat, double lng, double aroundDiff, boolean hideDecentOnly
			int minCoverage, int maxSize) {
		this.context = context;
		this.contentUri = contentUri;
		this.lat = lat;
		this.lng = lng;
		this.aroundDiff = aroundDiff;
		this.hideDecentOnly = hideDecentOnly;
		this.minCoverage = minCoverage;
		this.maxSize = maxSize;
	}

	@Override
	public List<? extends POI> callMT() throws Exception {
		List<? extends POI> pois = DataSourceProvider.findPOIsWithLatLngList(context, contentUri, lat, lng, aroundDiff, hideDecentOnly);
		LocationUtils.updateDistance(pois, lat, lng);
		float maxDistance = LocationUtils.getAroundCoveredDistance(lat, lng, aroundDiff);
		LocationUtils.removeTooFar(pois, maxDistance);
		CollectionUtils.sort(pois, POI.POI_DISTANCE_COMPARATOR);
		LocationUtils.removeTooMuchWhenNotInCoverage(pois, this.minCoverage, this.maxSize);
		return pois;
	}

}
