package pl.golinski.piotr.keyboard.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import pl.golinski.piotr.keyboard.R;
import pl.golinski.piotr.keyboard.emoticon.Emoticon;
import pl.golinski.piotr.keyboard.interfaces.OnEmoticonClickedListener;
import pl.golinski.piotr.keyboard.utils.ResourceUtils;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class EmoticonAdapter extends ArrayAdapter<Emoticon> {
	private OnEmoticonClickedListener mEmoticonClickListener;
	private Context mContext;

	public EmoticonAdapter(Context context, List<Emoticon> data) {
		super(context, R.layout.adapter_item, data);
		mContext = context;
	}

	public EmoticonAdapter(Context context, Emoticon[] data) {
		super(context, R.layout.adapter_item, data);
		mContext = context;
	}

	public void setEmojiClickListener(OnEmoticonClickedListener listener) {
		this.mEmoticonClickListener = listener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = View.inflate(getContext(), R.layout.adapter_item, null);
			ViewHolder holder = new ViewHolder();
			holder.icon = (ImageView) v.findViewById(R.id.ivIcon);
			v.setTag(holder);
		}

		Emoticon emoticon = getItem(position);
		ViewHolder holder = (ViewHolder) v.getTag();
		if (emoticon.getName() != null) {
			holder.icon.setImageResource(ResourceUtils.getResourceId(mContext, emoticon.getName()));
		} else {
			ImageLoader.getInstance().displayImage(emoticon.getUrl(), holder.icon);
		}
		holder.icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEmoticonClickListener.onEmoticonClicked(getItem(position));
			}
		});
		return v;
	}

	class ViewHolder {
		ImageView icon;
	}
}