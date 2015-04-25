package pl.golinski.piotr.keyboard.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class NetworkUtils {
	public static String connectWebserviceGet(String url, List<BasicNameValuePair> params) throws IOException {
		HttpClient client = new DefaultHttpClient();
		/*
		 * HttpParams httpParameters = client.getParams();
		 * HttpConnectionParams.setConnectionTimeout(httpParameters, 24000);
		 * HttpConnectionParams.setSoTimeout(httpParameters, 24000);
		 */
		if (params != null && params.size() > 0) {
			url += "?" + URLEncodedUtils.format(params, "UTF-8");
		}
		Log.e("URL", url);
		HttpGet request = new HttpGet(url);
		for (BasicNameValuePair param : params) {
			request.setHeader(param.getName(), param.getValue());
		}
		HttpResponse response = null;
		InputStream inputStream = null;
		String output = "";
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			//int status = response.getStatusLine().getStatusCode();
			output = IOUtils.inputStreamToByteArrayOutputStream(inputStream).toString();
			inputStream.close();
		} catch (Exception e) {
			Log.e("NetworkUtils", e.getLocalizedMessage());
			return null;
		}

		return output;
	}

	public static String connectWebservicePost(String url, List<BasicNameValuePair> params) throws IOException {
		InputStream inputStream = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			inputStream = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String output = IOUtils.inputStreamToByteArrayOutputStream(inputStream).toString();
		inputStream.close();
		return output;

	}
}
