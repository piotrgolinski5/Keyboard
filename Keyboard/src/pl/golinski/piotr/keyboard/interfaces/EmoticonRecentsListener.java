package pl.golinski.piotr.keyboard.interfaces;

import pl.golinski.piotr.keyboard.emoticon.Emoticon;

import android.content.Context;

public interface EmoticonRecentsListener {
	public void addRecentEmoticon(Context context, Emoticon emojicon);
}
