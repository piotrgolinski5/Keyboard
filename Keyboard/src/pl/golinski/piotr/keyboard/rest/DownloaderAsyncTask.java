package pl.golinski.piotr.keyboard.rest;

import java.io.IOException;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import pl.golinski.piotr.keyboard.interfaces.NotifyCallback;
import pl.golinski.piotr.keyboard.interfaces.ServiceCallback;
import pl.golinski.piotr.keyboard.utils.NetworkUtils;
import android.os.AsyncTask;
import android.util.Log;

public class DownloaderAsyncTask extends AsyncTask<List<BasicNameValuePair>, Void, String> {

	private ServiceCallback<String> mServiceCallback;
	private boolean mIsPost;
	private String mURL;

	public DownloaderAsyncTask(String URL, ServiceCallback<String> serviceCallback, boolean isPost) {
		this.mServiceCallback = serviceCallback;
		this.mURL = URL;
	}

	@Override
	protected void onPostExecute(String result) {
		Log.e("DownloaderAsyncTask", result);
		if (result == null) {
			mServiceCallback.onError(new Exception("exception"));
		}
		/*
		 * else if (result.equals("")) { mServiceCallback.onNoResult(); }
		 */else {
			 mServiceCallback.onSuccess(result);
		}
	}

	@Override
	protected String doInBackground(final List<BasicNameValuePair>... params) {
		String output = null;
		if (mIsPost == false) {
			try {
				if (mIsPost == false) {
					output = NetworkUtils.connectWebserviceGet(mURL, params[0]);
				} else {
					output = NetworkUtils.connectWebservicePost(mURL, params[0]);
				}

			} catch (IOException e) {
				mServiceCallback.onError(e);
				e.printStackTrace();
			}
		}
		return output;
	}
}
