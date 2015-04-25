package pl.golinski.piotr.keyboard;

import pl.golinski.piotr.keyboard.interfaces.OnKeyListener;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class KeyboardAction implements OnKeyboardActionListener {
	private OnKeyListener mOnKeyListener;
	private Context mContext;

	public KeyboardAction(Context context, OnKeyListener onKeyListener) {
		mOnKeyListener = onKeyListener;
		mContext = context;
	}

	private void playClick(int keyCode) {
		AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		switch (keyCode) {
		case 32:
			am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
			break;
		case Keyboard.KEYCODE_DONE:
		case 10:
			am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
			break;
		case Keyboard.KEYCODE_DELETE:
			am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
			break;
		default:
			am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
		}
	}

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		playClick(primaryCode);

		if (mOnKeyListener != null) {
			mOnKeyListener.onKey(primaryCode, keyCodes);
		}

	}

	@Override
	public void onPress(int primaryCode) {
	}

	@Override
	public void onRelease(int primaryCode) {
	}

	@Override
	public void onText(CharSequence text) {
	}

	@Override
	public void swipeLeft() {
	}

	@Override
	public void swipeRight() {
	}

	@Override
	public void swipeDown() {
	}

	@Override
	public void swipeUp() {
	}

}
