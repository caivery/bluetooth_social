package edu.minggo.chat.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;
/**
 * ����ͼƬ������
 * @author minggo
 * @created 2013-2-16����09:10:02
 */
public class SmileyGridLayoutAdapter extends SimpleAdapter {

	public SmileyGridLayoutAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}

}
