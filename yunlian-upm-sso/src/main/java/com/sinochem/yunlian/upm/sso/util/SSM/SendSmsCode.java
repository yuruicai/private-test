package com.sinochem.yunlian.upm.sso.util.SSM;

import com.sinochem.yunlian.upm.sso.util.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;


/**
 * 发送验证码
 * @author pengyikun
 *
 */
public class SendSmsCode {
    //发送验证码的请求路径URL
    private static final String
            SERVER_URL="https://api.netease.im/sms/sendcode.action";
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    private static final String 
            APP_KEY="32fc6ea29b6fa32914b7685cf6b8d053";
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    private static final String APP_SECRET="6bac5288cc39";
    //随机数
    private static final String NONCE="123456";
    //短信模板ID
    private static final String TEMPLATEID="3130215";
    //手机号
	//    private static final String MOBILE="13788996531";
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN="6";

    
    public static Map<?, ?> send(String userPhone) throws Exception
	{
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpPost httpPost = new HttpPost(SERVER_URL);
    	String curTime = String.valueOf((new Date()).getTime() / 1000L);
    	/*
    	 * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
    	 */
    	String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
    	
    	// 设置请求的header
    	httpPost.addHeader("AppKey", APP_KEY);
    	httpPost.addHeader("Nonce", NONCE);
    	httpPost.addHeader("CurTime", curTime);
    	httpPost.addHeader("CheckSum", checkSum);
    	httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    	
    	// 设置请求的的参数，requestBody参数
    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    	/*
    	 * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
    	 * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
    	 * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
    	 */
    	nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
    	nvps.add(new BasicNameValuePair("mobile", userPhone));
    	nvps.add(new BasicNameValuePair("codeLen", CODELEN));
    	
    	httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
    	
    	// 执行请求
    	HttpResponse response = httpClient.execute(httpPost);
    	/*
    	 * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
    	 * 2.具体的code有问题的可以参考官网的Code状态表
    	 */
    	String resultJson = EntityUtils.toString(response.getEntity(), "utf-8");
		Map<?, ?> map = JsonUtil.toObject(resultJson, new HashMap<>().getClass());

		return map;
	}

	public static String send1(String userPhone) throws Exception
	{
		Map<?, ?> map = send(userPhone);
		String obj = (String) map.get("obj");
		return obj;
	}
	public static Integer getCode(String userPhone) throws Exception
	{
		Map<?, ?> map = send(userPhone);
		Integer code = (Integer) map.get("code");
		return code;
	}
    
    public static void main(String[] args) throws Exception {
    	/*String smsCode = send("18515439860");
    	System.out.println(smsCode);
    	String resultJson = "{\"code\":200,\"msg\":\"1\",\"obj\":\"127648\"}";
    	Map<String, String> map = JsonUtil.toObject(resultJson, new HashMap<String, String>().getClass());
    	System.out.println(map.get("obj"));*/
		String s = send1("15120023590");
		System.out.println(s);
		Integer code = getCode("18515439860");
		System.out.println(code);

	}
}