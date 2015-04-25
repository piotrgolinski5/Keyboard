package pl.golinski.piotr.keyboard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ResourceUtils {

	public static int getResourceId(Context context, String variableName) {
		try {
			return context.getResources().getIdentifier(variableName, "drawable", context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static Drawable getDrawable(Context context, String name) {
		Drawable drawable = context.getResources().getDrawable(getResourceId(context, name));
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		return drawable;
	}
	
	public static Drawable getDrawableByUrl(Context context, String url) {
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
		Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		return drawable;
	}

}
