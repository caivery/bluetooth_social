package edu.minggo.chat.util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * ��Ч��
 * @author minggo
 * @created 2013-2-23����12:10:42
 */
public class VibratorUtil {
	/**
	 * ���ΰ�ʱ����
	 * @param context
	 * @param milliseconds
	 */
	public static void Vibrate(Context context,Long milliseconds){
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	/**
	 * �ظ���ʱ����
	 * @param context
	 * @param milliseconds
	 */
	public static void Vibrate(Context context,long[] milliseconds,boolean isRepeat){
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds,isRepeat?1:-1);
	}
}
