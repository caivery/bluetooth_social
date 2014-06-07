package edu.minggo.tencent.weibo;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class OAuthUtils {
	/**
	 * ��һ��Map��Ĳ�����ֵ��ȡ������consumer������(Map�������Ϊ����΢�����������)
	 * @param consumer
	 * @param addtionalParams
	 * @return
	 */
	public static OAuthConsumer addAddtionalParametersFromMap(OAuthConsumer consumer,
			Map<String, String> addtionalParams) {
		Set<String> keys = addtionalParams.keySet();
		Iterator<String> it = keys.iterator();
		
		HttpParameters httpParameters = new HttpParameters();
		while(it.hasNext()){
			String key = it.next();
			String value = addtionalParams.get(key);
			value = value.replaceAll(" ", "monggi");
			value = URLEncoder.encode(value);
			value = value.replaceAll("monggi", "%20");
			httpParameters.put(key, value);
		}
		consumer.setAdditionalParameters(httpParameters);
		return consumer;
	}
}
