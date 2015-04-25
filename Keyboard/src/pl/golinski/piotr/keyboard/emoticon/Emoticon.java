/*
 * Copyright 2014 Ankush Sachdeva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.golinski.piotr.keyboard.emoticon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.golinski.piotr.keyboard.model.EmoticonList.EmoticonWeb;

public class Emoticon implements Serializable {
	private static final long serialVersionUID = 1L;
	private String emoticon;
	private String name;
	private String url;

	public Emoticon() {
	}

	public Emoticon(EmoticonWeb emoticonWeb) {
		name = null;
		emoticon = emoticonWeb.name;
		url = emoticonWeb.url;
	}

	public static Emoticon fromCodePoint(int codePoint) {
		Emoticon emoticon = new Emoticon();
		emoticon.emoticon = newString(codePoint);
		emoticon.name = "emoji_" + Integer.toHexString(codePoint);
		return emoticon;
	}

	public static Emoticon fromChar(char ch) {
		Emoticon emoticon = new Emoticon();
		emoticon.emoticon = Character.toString(ch);
		emoticon.emoticon = "emoji_" + ch;
		return emoticon;
	}

	public String getUrl() {
		return url;
	}

	public Emoticon(String emoji) {
		this.emoticon = emoji;
	}

	public String getEmoticon() {
		return emoticon;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Emoticon && emoticon.equals(((Emoticon) o).emoticon);
	}

	@Override
	public int hashCode() {
		return emoticon.hashCode();
	}

	public static final String newString(int codePoint) {
		if (Character.charCount(codePoint) == 1) {
			return String.valueOf(codePoint);
		} else {
			return new String(Character.toChars(codePoint));
		}
	}

	public static List<Emoticon> getNewList(List<EmoticonWeb> emoticons) {

		List<Emoticon> list = new ArrayList<Emoticon>();

		for (EmoticonWeb e : emoticons) {
			list.add(new Emoticon(e));
		}

		return list;
	}
}
