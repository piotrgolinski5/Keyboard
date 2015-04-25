package pl.golinski.piotr.keyboard.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import pl.golinski.piotr.keyboard.interfaces.NotifyCallback;
import pl.golinski.piotr.keyboard.interfaces.ServiceCallback;
import pl.golinski.piotr.keyboard.model.EmoticonList;
import pl.golinski.piotr.keyboard.utils.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RestClient {
	private static final String mUrl = "http://www.google.com";
	private boolean mIsFakeData = true;
	private EmoticonListListener mEmoticonListListener;
	private static RestClient mInstance;

	public static RestClient getInstance(){
		if(mInstance == null){
			mInstance = new RestClient();
		}
		
		return mInstance;
	}
	
	public interface EmoticonListListener {
		public void onEmoticonList(List<EmoticonList> list);

	}

	public void register(EmoticonListListener emoticonListListener) {
		mEmoticonListListener = emoticonListListener;
	}

	public void unregister(EmoticonListListener emoticonListListener) {
		mEmoticonListListener = emoticonListListener;
	}

	public void getEmoticons() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		DownloaderAsyncTask task = new DownloaderAsyncTask(IOUtils.addStrings(mUrl, ""), new ServiceCallback<String>() {

			@Override
			public void onSuccess(String result) {
				

			}

			@Override
			public void onError(Exception exception) {

			}

			@Override
			public void onNoResult() {
				// TODO Auto-generated method stub

			}
		}, false);
		//task.execute(params);
		String result = null;
		Gson gson = new Gson();
		if (mIsFakeData) {
			List<EmoticonList> list = new ArrayList<EmoticonList>();
			
			EmoticonList emoticonList = new EmoticonList();
			emoticonList.name = "name1";
		    emoticonList.url = "https://raw.githubusercontent.com/ankushsachdeva/emojicon/master/lib/res/drawable-nodpi/emoji_00ae.png";
		    emoticonList.createFakeList();
			
			list.add(emoticonList);
			emoticonList = new EmoticonList();
			emoticonList.name = "name2";
		    emoticonList.url = "https://raw.githubusercontent.com/ankushsachdeva/emojicon/master/lib/res/drawable-nodpi/emoji_1f004.png";
		    emoticonList.createFakeList();
			
			list.add(emoticonList);
			emoticonList = new EmoticonList();
			emoticonList.name = "name2";
		    emoticonList.url = "https://raw.githubusercontent.com/ankushsachdeva/emojicon/master/lib/res/drawable-nodpi/emoji_1f0cf.png";
		    emoticonList.createFakeList();
			
			list.add(emoticonList);
			
			result = gson.toJson(list);
		}
		if (mEmoticonListListener != null) {
			try {
				mEmoticonListListener.onEmoticonList((List<EmoticonList>) gson.fromJson(result, new TypeToken<List<EmoticonList>>() {
				}.getType()));
			} catch (Exception e) {
				e.printStackTrace();
				// mEmoticonListListener.onError(e);
			}
		}
	}
}
