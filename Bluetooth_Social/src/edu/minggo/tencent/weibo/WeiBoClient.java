package edu.minggo.tencent.weibo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * ��Ҫ�Ƿ���΢��
 * @author minggo
 * @created 2013-2-28����02:41:13
 */
public class WeiBoClient {
	private OAuthConsumer consumer;
	
	public WeiBoClient(String consumer_key,String consumer_key_secret,String oauth_token,String oauth_token_secret){
		System.out.println("oauth_token:"+oauth_token+";"+"oauth_token_secret:"+oauth_token_secret );
		consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_key_secret);
		consumer.setTokenWithSecret(oauth_token, oauth_token_secret);
	}
	
	@SuppressWarnings("static-access")
	public String doPost(String url,Map<String,String> addtionalParams,List<String> decodeNames){
		
		consumer = new OAuthUtils().addAddtionalParametersFromMap(consumer, addtionalParams);
		HttpPost postRequest = new HttpPost(url);
		try {
			//���������ǩ��������֤��Ϣ��΢������һ�𽻸�consumer����
			consumer.sign(postRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//��Ѷ΢����֧��Header�������ݣ���Oauth��ʹ��Header�������ݣ�������� Headerȡ������
		Header oauthHeader = postRequest.getFirstHeader("Authorization");
		String baseString = oauthHeader.getValue().substring(5);
		
		Map<String,String> oauthMap = StringUtils.parseMapFromString(baseString);
		oauthMap = HttpUtils.decodeByDecodeNames(decodeNames, oauthMap);
		
		
		addtionalParams = HttpUtils.decodeByDecodeNames(decodeNames, addtionalParams);
		
		
		List<NameValuePair> pairs = ApacheUtils.convertMapToNameValuePairs(oauthMap);
		List<NameValuePair> weiBoPairs = ApacheUtils.convertMapToNameValuePairs(addtionalParams);
		pairs.addAll(weiBoPairs);
		
		
		HttpEntity entity = null;
		HttpResponse response = null;
		try {
			entity =  new UrlEncodedFormEntity(pairs,"UTF-8");
			postRequest.setEntity(entity);
			response = new DefaultHttpClient().execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line = "";
			while((line = reader.readLine()) != null){
				System.out.println(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = ApacheUtils.getResponseText(response);
		return result;
	}
	
	public String doGet(String url,Map<String,String> addtionalParams){
		url = UrlUtils.buildUrlByQueryStringMapAndBaseUrl(url, addtionalParams);
		try {
			//���������ǩ��������֤��Ϣ��΢������һ�𽻸�consumer����
			System.out.println("ǩ��֮ǰ��URL-->"+url);
			url = consumer.sign(url);
			System.out.println("ǩ��֮���url-->"+url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpGet getRequest =  new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response =  httpClient.execute(getRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ApacheUtils.getResponseText(response);
	}
	
	
}
