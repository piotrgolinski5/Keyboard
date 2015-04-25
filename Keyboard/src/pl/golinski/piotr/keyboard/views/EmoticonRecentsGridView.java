package pl.golinski.piotr.keyboard.views;


import java.util.List;

import pl.golinski.piotr.keyboard.EmoticonKeyboard;
import pl.golinski.piotr.keyboard.adapter.EmoticonAdapter;
import pl.golinski.piotr.keyboard.emoticon.Emoticon;
import pl.golinski.piotr.keyboard.emoticon.EmoticonRecentsManager;
import pl.golinski.piotr.keyboard.interfaces.EmoticonRecentsListener;
import pl.golinski.piotr.keyboard.interfaces.OnEmoticonClickedListener;
import android.content.Context;

public class EmoticonRecentsGridView extends EmoticonGridView implements EmoticonRecentsListener {
	private EmoticonAdapter mAdapter;
	private EmoticonKeyboard mEmoticonKeyboard;

	public EmoticonRecentsGridView(Context context, List<Emoticon> emojicons, EmoticonKeyboard emoticonKeyboard) {
		super(context, emojicons, emoticonKeyboard);
		mEmoticonKeyboard = emoticonKeyboard;
		EmoticonRecentsManager recentsManager = EmoticonRecentsManager.getInstance(mContext);
		mAdapter = new EmoticonAdapter(mContext, recentsManager);
		mAdapter.setEmojiClickListener(mOnEmoticonClickedListener);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	public void addRecentEmoticon(Context context, Emoticon emojicon) {
		EmoticonRecentsManager recentsManager = EmoticonRecentsManager.getInstance(context);
		recentsManager.push(emojicon);

		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	private OnEmoticonClickedListener mOnEmoticonClickedListener = new OnEmoticonClickedListener() {

		@Override
		public void onEmoticonClicked(Emoticon emoticon) {
			if (mEmoticonKeyboard != null) {
				mEmoticonKeyboard.onEmoticonClicked(emoticon);
			}
		}
	};

}
