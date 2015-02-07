package org.mtransit.android.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.mtransit.android.commons.ColorUtils;
import org.mtransit.android.commons.LocaleUtils;
import org.mtransit.android.commons.MTLog;
import org.mtransit.android.commons.data.DefaultPOI;
import org.mtransit.android.commons.data.POI;
import org.mtransit.android.provider.ModuleProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

public class Module extends DefaultPOI {

	private static final String TAG = Module.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	public static final int DST_ID = DataSourceType.TYPE_MODULE.getId();

	private String pkg;

	private int targetTypeId;

	private String color = null;

	private String location = null;

	private String nameFr = null;

	public Module(String authority, String pkg, int targetTypeId) {
		super(authority, DST_ID, POI.ITEM_VIEW_TYPE_MODULE, POI.ITEM_STATUS_TYPE_APP, POI.ITEM_ACTION_TYPE_APP);
		this.pkg = pkg;
		this.targetTypeId = targetTypeId;
	}

	public String getPkg() {
		return pkg;
	}

	public int getTargetTypeId() {
		return targetTypeId;
	}

	public String getColor() {
		return color;
	}

	private Integer colorInt = null;

	public int getColorInt() {
		if (this.colorInt == null) {
			this.colorInt = ColorUtils.parseColor(getColor());
		}
		return this.colorInt;
	}

	public String getLocation() {
		return location;
	}

	@Override
	public String getName() {
		if (LocaleUtils.isFR() && !TextUtils.isEmpty(this.nameFr)) {
			return this.nameFr;
		}
		return super.getName();
	}

	@Override
	public String toString() {
		return new StringBuilder().append(Module.class.getSimpleName()).append(":[") //
				.append("authority:").append(getAuthority()).append(',') //
				.append("pkg:").append(this.pkg).append(',') //
				.append("id:").append(getId()).append(',') //
				.append(']').toString();
	}

	@Override
	public int getType() {
		return POI.ITEM_VIEW_TYPE_MODULE;
	}

	@Override
	public int getStatusType() {
		return POI.ITEM_STATUS_TYPE_APP;
	}

	@Override
	public int getActionsType() {
		return POI.ITEM_ACTION_TYPE_APP;
	}

	@Override
	public boolean hasLocation() {
		return true; // required for distance sort
	}

	@Override
	public String getUUID() {
		return POI.POIUtils.getUUID(getAuthority(), this.pkg);
	}

	private static final String JSON_PKG = "pkg";
	private static final String JSON_TARGET_TYPE_ID = "targetTypeId";
	private static final String JSON_COLOR = "color";
	private static final String JSON_LOCATION = "location";
	private static final String JSON_NAME_FR = "name_fr";

	@Override
	public JSONObject toJSON() {
		try {
			JSONObject json = new JSONObject();
			json.put(JSON_PKG, this.pkg);
			json.put(JSON_TARGET_TYPE_ID, this.targetTypeId);
			if (!TextUtils.isEmpty(this.color)) {
				json.put(JSON_COLOR, this.color);
			}
			if (!TextUtils.isEmpty(this.location)) {
				json.put(JSON_LOCATION, this.location);
			}
			if (!TextUtils.isEmpty(this.nameFr)) {
				json.put(JSON_NAME_FR, this.nameFr);
			}
			DefaultPOI.toJSON(this, json);
			return json;
		} catch (JSONException jsone) {
			MTLog.w(this, jsone, "Error while converting to JSON (%s)!", this);
			return null;
		}
	}

	@Override
	public POI fromJSON(JSONObject json) {
		return fromJSONStatic(json);
	}

	public static Module fromJSONStatic(JSONObject json) {
		try {
			Module module = new Module( //
					DefaultPOI.getAuthorityFromJSON(json), //
					json.getString(JSON_PKG), //
					json.getInt(JSON_TARGET_TYPE_ID) //
			);
			String optColor = json.optString(JSON_COLOR);
			if (!TextUtils.isEmpty(optColor)) {
				module.color = optColor;
			}
			String optLocation = json.optString(JSON_LOCATION);
			if (!TextUtils.isEmpty(optLocation)) {
				module.location = optLocation;
			}
			String optNameFr = json.optString(JSON_NAME_FR);
			if (!TextUtils.isEmpty(optNameFr)) {
				module.nameFr = optNameFr;
			}
			DefaultPOI.fromJSON(json, module);
			return module;
		} catch (JSONException jsone) {
			MTLog.w(TAG, jsone, "Error while parsing JSON '%s'!", json);
			return null;
		}
	}

	public static Module fromSimpleJSONStatic(JSONObject json, String authority) {
		try {
			Module module = new Module( //
					authority, //
					json.getString(JSON_PKG), //
					json.getInt(JSON_TARGET_TYPE_ID) //
			);
			module.setName(json.getString(JSON_NAME));
			module.setLat(json.getDouble(JSON_LAT));
			module.setLng(json.getDouble(JSON_LNG));
			String optColor = json.optString(JSON_COLOR);
			if (!TextUtils.isEmpty(optColor)) {
				module.color = optColor;
			}
			String optLocation = json.optString(JSON_LOCATION);
			if (!TextUtils.isEmpty(optLocation)) {
				module.location = optLocation;
			}
			String optNameFr = json.optString(JSON_NAME_FR);
			if (!TextUtils.isEmpty(optNameFr)) {
				module.nameFr = optNameFr;
			}
			return module;
		} catch (JSONException jsone) {
			MTLog.w(TAG, jsone, "Error while parsing simple JSON '%s'!", json);
			return null;
		}
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = super.toContentValues();
		values.put(ModuleProvider.ModuleColumns.T_MODULE_K_PKG, this.pkg);
		values.put(ModuleProvider.ModuleColumns.T_MODULE_K_TARGET_TYPE_ID, this.targetTypeId);
		values.put(ModuleProvider.ModuleColumns.T_MODULE_K_COLOR, this.color);
		values.put(ModuleProvider.ModuleColumns.T_MODULE_K_LOCATION, this.location);
		values.put(ModuleProvider.ModuleColumns.T_MODULE_K_NAME_FR, this.nameFr);
		return values;
	}

	@Override
	public POI fromCursor(Cursor c, String authority) {
		return fromCursorStatic(c, authority);
	}

	public static Module fromCursorStatic(Cursor c, String authority) {
		String pkg = c.getString(c.getColumnIndexOrThrow(ModuleProvider.ModuleColumns.T_MODULE_K_PKG));
		int targetTypeId = c.getInt(c.getColumnIndexOrThrow(ModuleProvider.ModuleColumns.T_MODULE_K_TARGET_TYPE_ID));
		Module module = new Module(authority, pkg, targetTypeId);
		module.color = c.getString(c.getColumnIndexOrThrow(ModuleProvider.ModuleColumns.T_MODULE_K_COLOR));
		module.location = c.getString(c.getColumnIndexOrThrow(ModuleProvider.ModuleColumns.T_MODULE_K_LOCATION));
		module.nameFr = c.getString(c.getColumnIndexOrThrow(ModuleProvider.ModuleColumns.T_MODULE_K_NAME_FR));
		DefaultPOI.fromCursor(c, module);
		return module;
	}

}
