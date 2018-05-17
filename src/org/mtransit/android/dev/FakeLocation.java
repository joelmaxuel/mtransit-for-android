package org.mtransit.android.dev;

import android.location.Location;

public final class FakeLocation {

	public static final boolean ENABLED = false;
	// public static final boolean ENABLED = true; // DEBUG

	public static Location getLocation() {
		// CANADA
		// location = LocationUtils.getNewLocation(45.5131577, -73.4087376, 77f); // DEBUG
		// location = LocationUtils.getNewLocation(45.525191, -73.521961, 77f); // DEBUG - Station Longueuil
		// location = LocationUtils.getNewLocation(45.4789944, -73.449425, 77f); // DEBUG - RTL Line 5 - Grande Allée et mtée St-Hubert
		// location = LocationUtils.getNewLocation(45.514851, -73.559654, 77f); // DEBUG
		// location = LocationUtils.getNewLocation(45.4947620353203, -73.570805750666, 77f); // DEBUG
		// location = LocationUtils.getNewLocation(45.5254137,-73.5742727);
		// location = LocationUtils.getNewLocation(45.5159807, -73.5656846, 77f); // DEBUG
		// location = LocationUtils.getNewLocation(45.522018, -73.579445, 77f); // DEBUG (Montreal Office)
		// location = LocationUtils.getNewLocation(45.518733, -73.581145, 77f); // DEBUG (Montreal ...)
		// location = LocationUtils.getNewLocation(45.514841, -73.559646, 77f); // DEBUG (Montreal, Berri-UQAM)
		// location = LocationUtils.getNewLocation(45.512597, -73.560596, 77f); // DEBUG (Montreal, UQAM)
		// location = LocationUtils.getNewLocation(45.504774, -73.577123, 77f); // DEBUG (Montreal, McGill University)
		// location = LocationUtils.getNewLocation(45.5, -73.566667, 77f); // DEBUG (Montreal)
		// location = LocationUtils.getNewLocation(45.498570, -73.569077, 77f); // DEBUG (Montreal train)
		// location = LocationUtils.getNewLocation(45.473916, -74.060423, 77f); // DEBUG (Deux-Montagnes)
		// location = LocationUtils.getNewLocation(45.583333, -73.75, 77f); // DEBUG (Laval)
		// location = LocationUtils.getNewLocation(45.558335, -73.721824, 77f); // DEBUG (Laval - Montmorency)
		// location = LocationUtils.getNewLocation(45.566799, -73.744000, 77f); // DEBUG (Laval - Cosmodome)
		// location = LocationUtils.getNewLocation(45.533333, -73.516667, 77f); // DEBUG (Longueuil)
		// location = LocationUtils.getNewLocation(45.638366, -73.840007, 77f); // DEBUG (Laurentides)
		// location = LocationUtils.getNewLocation(46.021284, -73.448818, 77f); // DEBUG (Lanaudière)
		// location = LocationUtils.getNewLocation(45.547007, -73.229786, 77f); // DEBUG (Richelieu)
		// location = LocationUtils.getNewLocation(45.088713, -74.173483, 77f); // DEBUG (Haut-St-Laurent)
		// location = LocationUtils.getNewLocation(45.399002, -74.025913, 77f); // DEBUG (La Presqu'île)
		// location = LocationUtils.getNewLocation(45.417042, -73.463271, 77f); // DEBUG (Le Richelain)
		// location = LocationUtils.getNewLocation(45.714468, -73.474216, 77f); // DEBUG (L'Assomption)
		// location = LocationUtils.getNewLocation(45.697795, -73.653956, 77f); // DEBUG (Les Moulins)
		// location = LocationUtils.getNewLocation(45.360619, -73.514753, 77f); // DEBUG (Candiac)
		// location = LocationUtils.getNewLocation(45.447702, -73.278559, 77f); // DEBUG (Chambly-Richelieu-Carignan)
		// location = LocationUtils.getNewLocation(45.385600, -73.567099, 77f); // DEBUG (Roussillon)
		// location = LocationUtils.getNewLocation(45.360579, -73.732754, 77f); // DEBUG (Sud-Ouest)
		// location = LocationUtils.getNewLocation(45.652719, -73.299551, 77f); // DEBUG (Sorel-Varennes)
		// location = LocationUtils.getNewLocation(45.587540, -73.326283, 77f); // DEBUG (Ste-Julie)
		// location = LocationUtils.getNewLocation(45.400023, -74.051315, 77f); // DEBUG (Vaudreuil)
		// location = LocationUtils.getNewLocation(45.499370, -73.560304, 77f); // DEBUG (Orleans Express - Montreal)
		// location = LocationUtils.getNewLocation(46.818937, -71.213429, 77f); // DEBUG (Orleans Express - Quebec)
		// location = LocationUtils.getNewLocation(46.146602, -60.224134, 77f); // DEBUG (Maritime Bus - )
		// location = LocationUtils.getNewLocation(46.815010, -71.202920, 77f); // DEBUG (Quebec)
		// location = LocationUtils.getNewLocation(46.748814, -71.232096, 77f); // DEBUG (Lévis)
		// location = LocationUtils.getNewLocation(45.378654, -71.929601, 77f); // DEBUG (Sherbrooke)
		// location = LocationUtils.getNewLocation(46.620383, -72.681895, 77f); // DEBUG (Shawinigan)
		// location = LocationUtils.getNewLocation(45.430151, -75.709187, 77f); // DEBUG (Gatineau)
		// location = LocationUtils.getNewLocation(45.425144, -75.699950, 77f); // DEBUG (Ottawa)
		// location = LocationUtils.getNewLocation(45.387269, -75.689013, 77f); // DEBUG (Ottawa - Brewer Park)
		// location = LocationUtils.getNewLocation(45.388951, -75.710554, 77f); // DEBUG (Ottawa - Nature/Agriculture/Food/Farm)
		// location = LocationUtils.getNewLocation(45.016680, -74.722180, 77f); // DEBUG (Cornwall)
		// location = LocationUtils.getNewLocation(44.233297, -76.479631, 77f); // DEBUG (Kingston)
		// location = LocationUtils.getNewLocation(48.416896, -89.236989, 77f); // DEBUG (Thunder Bay)
		// location = LocationUtils.getNewLocation(43.945374, -78.896391, 77f); // DEBUG (Durham Region)
		// location = LocationUtils.getNewLocation(43.653425, -79.384080, 77f); // DEBUG (Toronto)
		// location = LocationUtils.getNewLocation(43.667114, -79.397241, 77f); // DEBUG (Toronto - University)
		// location = LocationUtils.getNewLocation(43.681321, -79.611455, 77f); // DEBUG (Toronto - Pearson)
		// location = LocationUtils.getNewLocation(43.466476, -80.532906, 77f); // DEBUG (Waterloo - GRT)
		// location = LocationUtils.getNewLocation(43.326095, -79.798886, 77f); // DEBUG (Burlington)
		// location = LocationUtils.getNewLocation(43.706969, -79.724756, 77f); // DEBUG (Brampton)
		// location = LocationUtils.getNewLocation(43.568954, -79.654336, 77f); // DEBUG (Mississauga)
		// location = LocationUtils.getNewLocation(43.546694, -80.246579, 77f); // DEBUG (Guelph)
		// location = LocationUtils.getNewLocation(43.255387, -79.871990, 77f); // DEBUG (Hamilton)
		// location = LocationUtils.getNewLocation(43.009595, -81.273736, 77f); // DEBUG (London)
		// location = LocationUtils.getNewLocation(42.305132, -83.067483, 77f); // DEBUG (Windsor)
		// location = LocationUtils.getNewLocation(43.862927, -79.422701, 77f); // DEBUG (York Region)
		// location = LocationUtils.getNewLocation(43.464729, -79.701734, 77f); // DEBUG (Oakville)
		// location = LocationUtils.getNewLocation(44.412043, -79.667775, 77f); // DEBUG (Barrie)
		// location = LocationUtils.getNewLocation(43.117566, -79.247697, 77f); // DEBUG (St Catharines)
		// location = LocationUtils.getNewLocation(43.014299, -79.263982, 77f); // DEBUG (Welland)
		// location = LocationUtils.getNewLocation(43.078902, -79.078168, 77f); // DEBUG (Niagara Falls)
		// location = LocationUtils.getNewLocation(42.870485, -79.054661, 77f); // DEBUG (Fort Erie)
		// location = LocationUtils.getNewLocation(46.466326, -80.972989, 77f); // DEBUG (Greater Sudbury)
		// location = LocationUtils.getNewLocation(43.521473, -79.896475, 77f); // DEBUG (Milton)
		// location = LocationUtils.getNewLocation(45.945428, -66.640688, 77f); // DEBUG (Fredericton)
		// location = LocationUtils.getNewLocation(46.106415, -64.785663, 77f); // DEBUG (Moncton)
		// location = LocationUtils.getNewLocation(44.647308, -63.580287, 77f); // DEBUG (Halifax)
		// location = LocationUtils.getNewLocation(47.523474, -52.753646, 77f); // DEBUG (St John's)
		// location = LocationUtils.getNewLocation(49.807523, -97.136600, 77f); // DEBUG (Winnipeg)
		// location = LocationUtils.getNewLocation(49.845335, -99.963355, 77f); // DEBUG (Brandon)
		// location = LocationUtils.getNewLocation(52.133398, -106.631368, 77f); // DEBUG (Saskatoon)
		// location = LocationUtils.getNewLocation(50.418294, -104.587026, 77f); // DEBUG (Regina)
		// location = LocationUtils.getNewLocation(49.678658, -112.859590, 77f); // DEBUG (Lethbridge)
		// location = LocationUtils.getNewLocation(51.078164, -114.135795, 77f); // DEBUG (Calgary)
		// location = LocationUtils.getNewLocation(51.277658, -114.013925, 77f); // DEBUG (Airdrie)
		// location = LocationUtils.getNewLocation(52.248035, -113.822084, 77f); // DEBUG (Red Deer)
		// location = LocationUtils.getNewLocation(51.178845, -115.569817, 77f); // DEBUG (Banff)
		// location = LocationUtils.getNewLocation(53.523214, -113.526310, 77f); // DEBUG (Edmonton)
		// location = LocationUtils.getNewLocation(53.631757, -113.630215, 77f); // DEBUG (St Albert)
		// location = LocationUtils.getNewLocation(53.543450, -113.304297, 77f); // DEBUG (Strathcona County)
		// location = LocationUtils.getNewLocation(55.137635, -118.783205, 77f); // DEBUG (Grande Prairie)
		// location = LocationUtils.getNewLocation(49.280267, -123.114748, 77f); // DEBUG (Vancouver)
		// location = LocationUtils.getNewLocation(48.463404, -123.311699, 77f); // DEBUG (Victoria)
		// location = LocationUtils.getNewLocation(50.118816, -122.954990, 77f); // DEBUG (Whistler)
		// location = LocationUtils.getNewLocation(49.705840, -123.150360, 77f); // DEBUG (Squamish)
		// location = LocationUtils.getNewLocation(49.397239, -123.510916, 77f); // DEBUG (Sunshine Coast)
		// location = LocationUtils.getNewLocation(55.756273, -120.221935, 77f); // DEBUG (Dawson Creek)
		// location = LocationUtils.getNewLocation(56.245855, -120.847862, 77f); // DEBUG (Fort St. John)
		// location = LocationUtils.getNewLocation(50.248307, -119.348325, 77f); // DEBUG (Vernon Regional)
		// location = LocationUtils.getNewLocation(49.155939, -123.965865, 77f); // DEBUG (Nanaimo)
		// location = LocationUtils.getNewLocation(49.028428, -122.284984, 77f); // DEBUG (Abbotsford)
		// location = LocationUtils.getNewLocation(49.940752, -119.396298, 77f); // DEBUG (Kelowna)
		// location = LocationUtils.getNewLocation(50.696690, -120.377338, 77f); // DEBUG (Kamloops)
		// location = LocationUtils.getNewLocation(53.892376, -122.813684, 77f); // DEBUG (Prince George)
		// location = LocationUtils.getNewLocation(49.671418, -124.927355, 77f); // DEBUG (Comox)
		// location = LocationUtils.getNewLocation(49.101089, -121.972742, 77f); // DEBUG (Chilliwack)
		// location = LocationUtils.getNewLocation(50.022539, -125.243029, 77f); // DEBUG (Campbell River)
		// location = LocationUtils.getNewLocation(60.720503, -135.052861, 77f); // DEBUG (Whitehorse)
		//
		// US
		// location = LocationUtils.getNewLocation(61.191010, -149.816301, 77f); // DEBUG (AK Anchorage)
		// location = LocationUtils.getNewLocation(58.384999, -134.640329, 77f); // DEBUG (AK Juneau)
		// location = LocationUtils.getNewLocation(47.921233, -122.279350, 77f); // DEBUG (WA Snohomish County Community Transit)
		// location = LocationUtils.getNewLocation(45.626552, -122.674932, 77f); // DEBUG (WA Clark County C-TRAN)
		// location = LocationUtils.getNewLocation(46.143465, -122.937214, 77f); // DEBUG (WA Longview RiverCities)
		// location = LocationUtils.getNewLocation(47.921233, -122.279350, 77f); // DEBUG (WA Everett Transit)
		// location = LocationUtils.getNewLocation(48.462735, -123.009693, 77f); // DEBUG (WA Washington State Ferries)
		// 
		// OTHER
		// location = LocationUtils.getNewLocation(0.0, 0.0); // DEBUG
		// location = LocationUtils.getNewLocation(1.0, 1.0); // DEBUG
		// location = null; // DEBUG (no location)
		return null;
	}
}
