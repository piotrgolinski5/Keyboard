package pl.golinski.piotr.keyboard.model;

import java.util.ArrayList;
import java.util.List;

public class EmoticonList {
	public String url;
	public String name;
	public List<EmoticonWeb> emoticons;

	public class EmoticonWeb {
		public EmoticonWeb(String url, String name) {
			this.url = url;
			this.name = name;
		}

		public String url;
		public String name;
	}

	public void createFakeList() {
		emoticons = new ArrayList<EmoticonList.EmoticonWeb>();
		for (int i = 0; i < 10; i++) {
			emoticons.add(new EmoticonWeb("https://raw.githubusercontent.com/ankushsachdeva/emojicon/master/lib/res/drawable-nodpi/emoji_00" + (30 + i) + ".png", "name" + 1));
		}
	}
}
