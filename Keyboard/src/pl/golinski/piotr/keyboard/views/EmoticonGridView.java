package pl.golinski.piotr.keyboard.views;


import java.util.List;

import pl.golinski.piotr.keyboard.EmoticonKeyboard;
import pl.golinski.piotr.keyboard.R;
import pl.golinski.piotr.keyboard.adapter.EmoticonAdapter;
import pl.golinski.piotr.keyboard.emoticon.Emoticon;
import pl.golinski.piotr.keyboard.interfaces.OnEmoticonClickedListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

public class EmoticonGridView {
	private EmoticonKeyboard mEmoticonKeyboard;
	protected Context mContext;
	protected GridView mGridView;
	public View mRootView;

	public EmoticonGridView(Context context, List<Emoticon> emoticons, EmoticonKeyboard emoticonKeyboard) {
		EmoticonAdapter mAdapter = new EmoticonAdapter(context, emoticons);
		init(context, emoticonKeyboard, mAdapter);
	}

	public EmoticonGridView(Context context, Emoticon[] emoticons, EmoticonKeyboard emoticonKeyboard) {
		EmoticonAdapter mAdapter = new EmoticonAdapter(context, emoticons);
		init(context, emoticonKeyboard, mAdapter);
	}

	@SuppressLint("InflateParams")
	private void init(Context context, EmoticonKeyboard emoticonKeyboard, EmoticonAdapter mAdapter) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mEmoticonKeyboard = emoticonKeyboard;
		mRootView = inflater.inflate(R.layout.emojicon_grid, null);
		mGridView = (GridView) mRootView.findViewById(R.id.Emoji_GridView);
		mAdapter.setEmojiClickListener(mOnEmoticonClickedListener);
		mGridView.setAdapter(mAdapter);
	}

	private OnEmoticonClickedListener mOnEmoticonClickedListener = new OnEmoticonClickedListener() {

		@Override
		public void onEmoticonClicked(Emoticon emojicon) {
			if (mEmoticonKeyboard != null) {
				mEmoticonKeyboard.onEmoticonClicked(emojicon);
			}
		}
	};

}
