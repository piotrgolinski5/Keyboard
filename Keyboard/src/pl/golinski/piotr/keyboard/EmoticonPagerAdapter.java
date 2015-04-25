package pl.golinski.piotr.keyboard;

import java.util.List;

import pl.golinski.piotr.keyboard.views.EmoticonGridView;
import pl.golinski.piotr.keyboard.views.EmoticonRecentsGridView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class EmoticonPagerAdapter extends PagerAdapter {
	private List<EmoticonGridView> mViews;
	private EmoticonRecentsGridView mEmoticonRecentsGridView;

	public EmoticonPagerAdapter(List<EmoticonGridView> views) {
		super();
		this.mViews = views;
	}

	public void addEmoticonGridView(EmoticonGridView view) {
		mViews.add(view);
	
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void clear() {
		mViews.clear();
	}

	@Override
	public int getCount() {
		Log.e("getCount", ""+mViews.size());
		return mViews.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mViews.get(position).mRootView;
		ViewPager viewPager = (ViewPager) container;
		viewPager.addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		ViewPager viewPager = (ViewPager) container;
		viewPager.removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object key) {
		return key == view;
	}

	public EmoticonRecentsGridView getRecentFragment() {
		if (mEmoticonRecentsGridView == null) {
			for (EmoticonGridView gridView : mViews) {
				if (gridView instanceof EmoticonRecentsGridView) {
					mEmoticonRecentsGridView = (EmoticonRecentsGridView) gridView;
					return mEmoticonRecentsGridView;
				}
			}

			return null;
		} else {
			return mEmoticonRecentsGridView;
		}
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
