package pl.golinski.piotr.keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.golinski.piotr.keyboard.emoticon.Emoticon;
import pl.golinski.piotr.keyboard.emoticon.EmoticonRecentsManager;
import pl.golinski.piotr.keyboard.interfaces.OnEmoticonClickedListener;
import pl.golinski.piotr.keyboard.interfaces.OnKeyListener;
import pl.golinski.piotr.keyboard.model.EmoticonList;
import pl.golinski.piotr.keyboard.rest.RestClient;
import pl.golinski.piotr.keyboard.rest.RestClient.EmoticonListListener;
import pl.golinski.piotr.keyboard.utils.ResourceUtils;
import pl.golinski.piotr.keyboard.views.EmoticonGridView;
import pl.golinski.piotr.keyboard.views.EmoticonRecentsGridView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputConnection;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class EmoticonKeyboard extends InputMethodService implements ViewPager.OnPageChangeListener, OnEmoticonClickedListener, OnKeyListener {
	private Context mContext;
	private KeyboardView mKeyboardView;
	private LinearLayout mEmoticons;
	private LinearLayout mTabs;
	private ViewPager mPager;
	private EmoticonPagerAdapter mEmoticonsAdapter;
	private KeyboardAction mKeyboardAction;
	private List<EmoticonList> mList;
	private int mEmojiTabLastSelectedIndex = -1;
	private List<View> mEmoticonTabs = new ArrayList<View>();
	private EmoticonRecentsManager mRecentsManager;

	private boolean mCaps = false;

	@Override
	public View onCreateInputView() {
		mContext = getApplicationContext();
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mContext);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app
		config.defaultDisplayImageOptions(displayImageOptions);
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());

		View view = (View) getLayoutInflater().inflate(R.layout.keyboard, null);

		mKeyboardView = (KeyboardView) view.findViewById(R.id.keyboard);
		mKeyboardAction = new KeyboardAction(mContext, this);
		mKeyboardView.setOnKeyboardActionListener(mKeyboardAction);
		mKeyboardView.setKeyboard(new Keyboard(this, R.xml.qwerty));
		
		mTabs = (LinearLayout) view.findViewById(R.id.llTabs);
		mEmoticons = (LinearLayout) view.findViewById(R.id.rlEmaticons);

		mPager = (ViewPager) view.findViewById(R.id.vpPager);
		mPager.setOnPageChangeListener(this);

		mEmoticonsAdapter = new EmoticonPagerAdapter(new ArrayList<EmoticonGridView>());
		mPager.setAdapter(mEmoticonsAdapter);

		((view.findViewById(R.id.btnKeyboard))).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEmoticons.setVisibility(View.GONE);
			}
		});

		// mEmojiTabs.add(view.findViewById(R.id.ibRecents));
		// mEmojiTabs.add(view.findViewById(R.id.ibStandart));
		// updateTabs();

		RestClient restClient = RestClient.getInstance();
		restClient.register(new EmoticonListListener() {

			@Override
			public void onEmoticonList(List<EmoticonList> list) {

				mList = list;
				updateTabs();
				Log.e("onEmoticonList", "size " + list.size());
			}

		});
		restClient.getEmoticons();

		return view;
	}

	private void updateTabs() {
		mEmoticonTabs.clear();
		mTabs.removeAllViews();
		mEmoticonsAdapter.clear();

		addSingleTab(R.drawable.ic_emoji_recent_light, true);
		mEmoticonsAdapter.addEmoticonGridView(new EmoticonRecentsGridView(mContext, EmoticonRecentsManager.getInstance(mContext), this));

		addSingleTab(R.drawable.ic_emoji_people_light, true);
		mEmoticonsAdapter.addEmoticonGridView(new EmoticonGridView(mContext, People.DATA, this));

		if (mList != null) {
			for (EmoticonList list : mList) {
				View view = new View(mContext);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
				view.setBackgroundColor(Color.parseColor("#888888"));
				mTabs.addView(view, lp);
				ImageButton imageButton = new ImageButton(mContext);
				lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
				lp.weight = 1;
				imageButton.setBackgroundColor(Color.TRANSPARENT);
				imageButton.setScaleType(ScaleType.CENTER);
				ImageLoader.getInstance().displayImage(list.url, imageButton);

				mTabs.addView(imageButton, lp);
				mEmoticonTabs.add(imageButton);
				mEmoticonsAdapter.addEmoticonGridView(new EmoticonGridView(mContext, Emoticon.getNewList(list.emoticons), this));
			}
		}

		addSingleTab(R.drawable.sym_keyboard_delete_holo_dark, true);

		for (int i = 0; i < mEmoticonTabs.size(); i++) {
			mEmoticonTabs.get(i).setTag(i);
			mEmoticonTabs.get(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					if (position == mEmoticonTabs.size() -1) {
						InputConnection ic = getCurrentInputConnection();
						ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
					} else {
						mPager.setCurrentItem(position);
					}
				}
			});
		}

		mRecentsManager = EmoticonRecentsManager.getInstance(mContext);
		int page = mRecentsManager.getRecentPage();
		if (page == 0 && mRecentsManager.size() == 0) {
			page = 1;
		}

		if (page == 0) {
			onPageSelected(page);
		} else {
			mPager.setCurrentItem(page, false);
		}

	}

	private void addSingleTab(int drawable, boolean withAdd) {
		View view = new View(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
		view.setBackgroundColor(Color.parseColor("#888888"));
		mTabs.addView(view, lp);
		ImageButton imageButton = new ImageButton(mContext);
		lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		imageButton.setBackgroundColor(Color.TRANSPARENT);
		imageButton.setScaleType(ScaleType.CENTER);
		imageButton.setImageResource(drawable);

		mTabs.addView(imageButton, lp);
		if (withAdd) {
			mEmoticonTabs.add(imageButton);
		}
	}

	/*
	 * OnPageChangeListener
	 */
	@Override
	public void onPageSelected(int i) {
		if (mEmojiTabLastSelectedIndex == i) {
			return;
		}

		if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmoticonTabs.size()) {
			mEmoticonTabs.get(mEmojiTabLastSelectedIndex).setSelected(false);
		}

		mEmoticonTabs.get(i).setSelected(true);
		mEmojiTabLastSelectedIndex = i;
		mRecentsManager.setRecentPage(i);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	/*
	 * OnEmojiconClickedListener
	 */
	@Override
	public void onEmoticonClicked(Emoticon emojicon) {
		EmoticonRecentsGridView fragment = ((EmoticonPagerAdapter) mPager.getAdapter()).getRecentFragment();
		fragment.addRecentEmoticon(mContext, emojicon);
		InputConnection ic = getCurrentInputConnection();
		SpannableString ss;
		Drawable drawabe;
		String value;
		if (emojicon.getName() != null) {
			value = "[" + emojicon.getName() + "]";
			ss = new SpannableString(value);
			drawabe = ResourceUtils.getDrawable(mContext, emojicon.getName());
		} else {
			value = "[" + emojicon.getEmoticon() + "]";
			ss = new SpannableString(value);
			drawabe = ResourceUtils.getDrawableByUrl(mContext, emojicon.getUrl());
		}
		ImageSpan span = new ImageSpan(drawabe, ImageSpan.ALIGN_BASELINE);
		ss.setSpan(span, 0, value.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

		ic.commitText(ss, 1);
	}

	/*
	 * OnKeyListener
	 */
	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		InputConnection ic = getCurrentInputConnection();
		switch (primaryCode) {
		case Keyboard.KEYCODE_DELETE:
			ic.deleteSurroundingText(1, 0);
			break;
		case Keyboard.KEYCODE_SHIFT:
			mCaps = !mCaps;
			mKeyboardView.getKeyboard().setShifted(mCaps);
			mKeyboardView.invalidateAllKeys();
			break;
		case Keyboard.KEYCODE_DONE:
			ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
			break;
		case 9999:
			mEmoticons.setVisibility(View.VISIBLE);
			break;
		default:
			char code = (char) primaryCode;
			if (Character.isLetter(code) && mCaps) {
				code = Character.toUpperCase(code);
			}
			ic.commitText(String.valueOf(code), 1);
		}
	}

}
