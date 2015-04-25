package pl.golinski.piotr.keyboard.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

	public static ByteArrayOutputStream inputStreamToByteArrayOutputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream content = new ByteArrayOutputStream();
		if (inputStream != null) {
			int readBytes = 0;
			byte[] sBuffer = new byte[4096];
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}
		}
		return content;
	}

	public static String addStrings(Object... params) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object s : params) {
			stringBuilder.append(s);
		}
		return stringBuilder.toString();

	}

	public static String addStringsWithSpaces(Object... params) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object s : params) {
			stringBuilder.append(s);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();

	}

}
