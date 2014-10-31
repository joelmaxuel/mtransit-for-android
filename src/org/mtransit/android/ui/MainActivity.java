package org.mtransit.android.ui;

import java.lang.ref.WeakReference;
import java.util.List;

import org.mtransit.android.R;
import org.mtransit.android.commons.PreferenceUtils;
import org.mtransit.android.data.DataSourceProvider;
import org.mtransit.android.data.MenuAdapter;
import org.mtransit.android.task.StatusLoader;
import org.mtransit.android.ui.fragment.ABFragment;
import org.mtransit.android.util.AdsUtils;
import org.mtransit.android.util.AnalyticsUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressWarnings("deprecation")
// need to switch to support-v7-appcompat
public class MainActivity extends MTActivityWithLocation implements AdapterView.OnItemClickListener, FragmentManager.OnBackStackChangedListener,
		AnalyticsUtils.Trackable, MenuAdapter.MenuUpdateListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	public String getLogTag() {
		return TAG;
	}

	private static final String TRACKING_SCREEN_NAME = "Main";

	@Override
	public String getScreenName() {
		return TRACKING_SCREEN_NAME;
	}

	private static final boolean LOCATION_ENABLED = true;

	private static final String EXTRA_SELECTED_ROOT_SCREEN_POSITION = "extra_selected_root_screen";
	private static final String EXTRA_SELECTED_ROOT_SCREEN_ID = "extra_selected_root_screen_id";

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private MenuAdapter mDrawerListAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private int mDrawerState = DrawerLayout.STATE_IDLE;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private CharSequence mDrawerSubtitle;
	private CharSequence mSubtitle;

	private int mIcon;
	private int mDrawerIcon;

	private Integer mBgColor;
	private Integer mDrawerBgColor;

	private View mCustomView;
	private View mDrawerCustomView;

	public static Intent newInstance(Context context, int optSelectedRootScreenPosition, String optSelectedRootScreenId) {
		Intent intent = new Intent(context, MainActivity.class);
		if (optSelectedRootScreenPosition >= 0) {
			intent.putExtra(EXTRA_SELECTED_ROOT_SCREEN_POSITION, optSelectedRootScreenPosition);
		}
		if (!TextUtils.isEmpty(optSelectedRootScreenId)) {
			intent.putExtra(EXTRA_SELECTED_ROOT_SCREEN_ID, optSelectedRootScreenId);
		}
		return intent;
	}

	public MainActivity() {
		super(LOCATION_ENABLED);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mSubtitle = mDrawerSubtitle = getActionBar().getSubtitle();
		mIcon = mDrawerIcon = R.mipmap.ic_launcher;
		mBgColor = mDrawerBgColor = ABFragment.NO_BG_COLOR;
		mCustomView = mDrawerCustomView = getActionBar().getCustomView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer); // (mDrawerList) getSupportFragmentManager().findFragmentById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerListAdapter = new MenuAdapter(this, this);
		mDrawerList.setAdapter(mDrawerListAdapter);
		mDrawerList.setOnItemClickListener(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ABToggle(this, mDrawerLayout);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getSupportFragmentManager().addOnBackStackChangedListener(this);

		if (savedInstanceState == null) {
			final String itemId = PreferenceUtils.getPrefLcl(this, PreferenceUtils.PREFS_LCL_ROOT_SCREEN_ITEM_ID, MenuAdapter.ITEM_ID_SELECTED_SCREEN_DEFAULT);
			selectItem(this.mDrawerListAdapter.getScreenItemPosition(itemId), null, false);
		} else {
			onRestoreState(savedInstanceState);
		}
		AdsUtils.setupAd(this);
	}

	private static class ABToggle extends ActionBarDrawerToggle {

		private WeakReference<MainActivity> mainActivityWR;

		public ABToggle(MainActivity mainActivity, DrawerLayout drawerLayout) {
			super(mainActivity, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
			this.mainActivityWR = new WeakReference<MainActivity>(mainActivity);
		}

		@Override
		public void onDrawerClosed(View view) {
			final MainActivity mainActivity = this.mainActivityWR == null ? null : this.mainActivityWR.get();
			if (mainActivity != null) {
				mainActivity.updateABDrawerClosed();
				mainActivity.invalidateOptionsMenu();
			}
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			final MainActivity mainActivity = this.mainActivityWR == null ? null : this.mainActivityWR.get();
			if (mainActivity != null) {
				mainActivity.updateABDrawerOpened();
				mainActivity.invalidateOptionsMenu();
			}
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			final MainActivity mainActivity = this.mainActivityWR == null ? null : this.mainActivityWR.get();
			if (mainActivity != null) {
				mainActivity.mDrawerState = newState;
			}
		}
	}

	private void onRestoreState(Bundle savedInstanceState) {
		int savedRootScreen = savedInstanceState.getInt(EXTRA_SELECTED_ROOT_SCREEN_POSITION, -1);
		if (savedRootScreen >= 0) {
			this.currentSelectedItemPosition = savedRootScreen;
		}
		String savedRootScreenId = savedInstanceState.getString(EXTRA_SELECTED_ROOT_SCREEN_ID, null);
		if (!TextUtils.isEmpty(savedRootScreenId)) {
			this.currentSelectedScreenItemId = savedRootScreenId;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setAB();
		updateAB();
		AnalyticsUtils.trackScreenView(this, this);
		AdsUtils.resumeAd(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AdsUtils.pauseAd(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		DataSourceProvider.reset(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AdsUtils.destroyAd(this);
		if (mDrawerLayout != null) {
			mDrawerLayout.setDrawerListener(null);
			mDrawerLayout = null;
		}
		mDrawerToggle = null;
		mCustomView = null;
		mDrawerCustomView = null;
		DataSourceProvider.destroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(EXTRA_SELECTED_ROOT_SCREEN_POSITION, this.currentSelectedItemPosition);
		outState.putString(EXTRA_SELECTED_ROOT_SCREEN_ID, this.currentSelectedScreenItemId);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		onRestoreState(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectItem(position, null, false);
	}


	private int currentSelectedItemPosition = -1;

	private String currentSelectedScreenItemId = null;

	private void selectItem(int position, ABFragment newFragmentOrNull, boolean addToStack) {
		if (position < 0) {
			return;
		}
		final FragmentManager fm = getSupportFragmentManager();
		if (position == this.currentSelectedItemPosition) {
			while (fm.getBackStackEntryCount() > 0) {
				fm.popBackStackImmediate();
			}
			closeDrawer();
			return;
		}
		if (!this.mDrawerListAdapter.isRootScreen(position)) {
			if (this.currentSelectedItemPosition >= 0) {
				this.mDrawerList.setItemChecked(this.currentSelectedItemPosition, true); // keep current position
			}
			return;
		}
		final ABFragment newFragment = newFragmentOrNull != null ? newFragmentOrNull : this.mDrawerListAdapter.getNewStaticFragmentAt(position);
		if (newFragment == null) {
			return;
		}
		clearFragmentBackStackImmediate(fm); // root screen
		StatusLoader.get().clearAllTasks();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, newFragment);
		if (addToStack) {
			ft.addToBackStack(null);
			this.backStackEntryCount++;
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		setAB(newFragment);
		this.mDrawerList.setItemChecked(position, true);
		closeDrawer();
		this.currentSelectedItemPosition = position;
		this.currentSelectedScreenItemId = this.mDrawerListAdapter.getScreenItemId(position);
		if (!addToStack && this.mDrawerListAdapter.isRootScreen(position)) {
			PreferenceUtils.savePrefLcl(this, PreferenceUtils.PREFS_LCL_ROOT_SCREEN_ITEM_ID, this.currentSelectedScreenItemId, false);
		}
	}

	@Override
	public void onMenuUpdated() {
		final String itemId = PreferenceUtils.getPrefLcl(this, PreferenceUtils.PREFS_LCL_ROOT_SCREEN_ITEM_ID, MenuAdapter.ITEM_ID_SELECTED_SCREEN_DEFAULT);
		final int newSelectedItemPosition = this.mDrawerListAdapter.getScreenItemPosition(itemId);
		if (this.currentSelectedScreenItemId != null && this.currentSelectedScreenItemId.equals(itemId)) {
			this.currentSelectedItemPosition = newSelectedItemPosition;
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				mDrawerList.setItemChecked(this.currentSelectedItemPosition, true);
			} else {
				mDrawerList.setItemChecked(this.currentSelectedItemPosition, false);
			}
			return;
		}
		selectItem(newSelectedItemPosition, null, false); // re-select, selected item
	}

	private void clearFragmentBackStackImmediate(FragmentManager fm) {
		while (fm.getBackStackEntryCount() > 0) {
			fm.popBackStackImmediate();
		}
	}

	public void addFragmentToStack(ABFragment newFragment) {
		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, newFragment);
		ft.addToBackStack(null);
		this.backStackEntryCount++;
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		setAB(newFragment);
		this.mDrawerList.setItemChecked(this.currentSelectedItemPosition, false);
	}

	@Override
	public void onUserLocationChanged(Location newLocation) {
		final List<Fragment> fragments = getSupportFragmentManager().getFragments();
		if (fragments != null) {
			for (Fragment fragment : fragments) {
				if (fragment != null && fragment instanceof MTActivityWithLocation.UserLocationListener) {
					((MTActivityWithLocation.UserLocationListener) fragment).onUserLocationChanged(newLocation);
				}
			}
		}
	}

	@Deprecated
	@Override
	public void setTitle(int titleId) {
		super.setTitle(titleId); // call setTitle(CharSequence)
	}

	@Override
	@Deprecated
	public void setTitle(CharSequence title) {
		setABTitle(title);
	}

	private void setAB() {
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
		if (f != null && f instanceof ABFragment) {
			ABFragment abf = (ABFragment) f;
			setAB(abf);
		}
	}

	private void setAB(ABFragment abf) {
		setAB(abf.getABTitle(this), abf.getABSubtitle(this), abf.getABIconDrawableResId(), abf.getABBgColor(), abf.getABCustomView());
	}

	private void setAB(CharSequence title, CharSequence subtitle, int iconResId, Integer bgColor, View customView) {
		mTitle = title;
		mSubtitle = subtitle;
		mIcon = iconResId;
		mBgColor = bgColor;
		mCustomView = customView;
	}
	private void setABTitle(CharSequence title) {
		mTitle = title;
		updateAB();
	}

	@SuppressWarnings("unused")
	private void setABSubtitle(CharSequence subtitle) {
		mSubtitle = subtitle;
		updateAB();
	}

	@SuppressWarnings("unused")
	private void setABIcon(int resId) {
		mIcon = resId;
		updateAB();
	}

	public void notifyABChange() {
		notifyABChange(getSupportFragmentManager().findFragmentById(R.id.content_frame));
	}

	private void notifyABChange(Fragment f) {
		if (f != null && f instanceof ABFragment) {
			ABFragment abf = (ABFragment) f;
			setAB(abf);
			updateAB();
		}
	}

	@Override
	public void onBackStackChanged() {
		// MTLog.d(this, "onBackStackChanged() > getSupportFragmentManager().getBackStackEntryCount(): %s",
		// getSupportFragmentManager().getBackStackEntryCount());
		// MTLog.d(this, "onBackStackChanged() > this.currentSelectedItemPosition: %s", this.currentSelectedItemPosition);
		// this.mDrawerToggle.setDrawerIndicatorEnabled(isDrawerOpen() ? true : getSupportFragmentManager().getBackStackEntryCount() < 1);
		this.backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
		setAB();
		updateAB(); // up/drawer icon
		if (this.backStackEntryCount == 0) {
			mDrawerList.setItemChecked(this.currentSelectedItemPosition, true);
		} else {
			mDrawerList.setItemChecked(this.currentSelectedItemPosition, false);
		}
	}

	@Override
	public void onBackPressed() {
		if (isDrawerOpen()) {
			closeDrawer();
			return;
		}
		super.onBackPressed();
	}

	private void closeDrawer() {
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private boolean isDrawerOpen() {
		return mDrawerLayout.isDrawerOpen(mDrawerList);
	}

	private long lastInvalidateOptionsMenu = -1l;

	private static final long MIN_DURATION_BETWEEN_OPTION_MENU_INVALIDATE_IN_MS = 250l; // 0.5 second

	private Runnable invalidateOptionsMenuLater = new Runnable() {

		@Override
		public void run() {
			invalidateOptionsMenu();
		}
	};

	@Override
	public void invalidateOptionsMenu() {
		final long now = System.currentTimeMillis();
		final long howLongBeforeNextInvalidateInMs = this.lastInvalidateOptionsMenu + MIN_DURATION_BETWEEN_OPTION_MENU_INVALIDATE_IN_MS - now;
		if (mDrawerState != DrawerLayout.STATE_IDLE || howLongBeforeNextInvalidateInMs > 0) {
			this.handler.postDelayed(this.invalidateOptionsMenuLater, howLongBeforeNextInvalidateInMs > 0 ? howLongBeforeNextInvalidateInMs
					: MIN_DURATION_BETWEEN_OPTION_MENU_INVALIDATE_IN_MS);
			return;
		}
		this.handler.removeCallbacks(this.invalidateOptionsMenuLater);
		super.invalidateOptionsMenu();
		this.lastInvalidateOptionsMenu = now;
	}
	private Handler handler = new Handler();

	private long lastUpdateAB = -1l;

	private static final long MIN_DURATION_BETWEEN_UPDATE_AB_IN_MS = 250l; // 0.5 second

	private Runnable updateABLater = new Runnable() {

		@Override
		public void run() {
			updateAB();
		}
	};

	private int backStackEntryCount = 0;

	private void updateAB() {
		final long now = System.currentTimeMillis();
		final long howLongBeforeNextUpdateABInMs = this.lastUpdateAB + MIN_DURATION_BETWEEN_UPDATE_AB_IN_MS - now;
		if (mDrawerState != DrawerLayout.STATE_IDLE || howLongBeforeNextUpdateABInMs > 0) {
			this.handler.postDelayed(this.updateABLater, howLongBeforeNextUpdateABInMs > 0 ? howLongBeforeNextUpdateABInMs
					: MIN_DURATION_BETWEEN_UPDATE_AB_IN_MS);
			return;
		}
		this.handler.removeCallbacks(this.updateABLater);
		if (isDrawerOpen()) {
			updateABDrawerOpened();
		} else {
			updateABDrawerClosed();
		}
		this.lastUpdateAB = now;
	}

	private void updateABDrawerClosed() {
		getActionBar().setTitle(mTitle);
		getActionBar().setSubtitle(mSubtitle);
		if (mIcon > 0) {
			getActionBar().setIcon(mIcon);
			getActionBar().setDisplayShowHomeEnabled(true);
		} else {
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		if (mBgColor != null) {
			getActionBar().setBackgroundDrawable(new ColorDrawable(mBgColor));
		} else {
			getActionBar().setBackgroundDrawable(null);
		}
		if (mCustomView != null) {
			getActionBar().setCustomView(mCustomView);
			getActionBar().getCustomView().setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final FragmentManager fm = getSupportFragmentManager();
					if (fm.getBackStackEntryCount() > 0) {
						fm.popBackStackImmediate();
					}
				}
			});
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setDisplayShowCustomEnabled(true);
		} else {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setDisplayShowCustomEnabled(false);
		}
		this.mDrawerToggle.setDrawerIndicatorEnabled(this.backStackEntryCount < 1);
		invalidateOptionsMenu();
	}

	private void updateABDrawerOpened() {
		getActionBar().setTitle(mDrawerTitle);
		getActionBar().setSubtitle(mDrawerSubtitle);
		if (mDrawerIcon > 0) {
			getActionBar().setIcon(mDrawerIcon);
			getActionBar().setDisplayShowHomeEnabled(true);
		} else {
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		if (mDrawerBgColor != null) {
			getActionBar().setBackgroundDrawable(new ColorDrawable(mDrawerBgColor));
		} else {
			getActionBar().setBackgroundDrawable(null);
		}
		if (mDrawerCustomView != null) {
			getActionBar().setCustomView(mDrawerCustomView);
			getActionBar().getCustomView().setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final FragmentManager fm = getSupportFragmentManager();
					if (fm.getBackStackEntryCount() > 0) {
						fm.popBackStackImmediate();
					}
				}
			});
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setDisplayShowCustomEnabled(true);
		} else {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setDisplayShowCustomEnabled(false);
		}
		this.mDrawerToggle.setDrawerIndicatorEnabled(true);
		invalidateOptionsMenu();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		final MenuItem menuToggleListGrid = menu.findItem(R.id.menu_toggle_list_grid);
		if (menuToggleListGrid != null) {
			menuToggleListGrid.setVisible(!drawerOpen);
		}
		final MenuItem menuAddRemoveFavorite = menu.findItem(R.id.add_remove_favorite);
		if (menuAddRemoveFavorite != null) {
			menuAddRemoveFavorite.setVisible(!drawerOpen);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void popFragmentFromStack(Fragment fragment) {
		if (fragment != null) {
			final FragmentManager fm = getSupportFragmentManager();
			final FragmentTransaction ft = fm.beginTransaction();
			ft.remove(fragment);
			ft.commit();
			fm.popBackStackImmediate();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == android.R.id.home) {
			final FragmentManager fm = getSupportFragmentManager();
			if (fm.getBackStackEntryCount() > 0) {
				fm.popBackStackImmediate();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
