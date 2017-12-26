package com.sinochem.yunlian.upm.sso.util.SSM;
import com.sinochem.yunlian.upm.sso.util.JsonUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板短信DEMO
 *
 */
public class TemplateSms {
	public static String APP_ID = "";//应用ID------登录平台在应用设置可以找到
	public static String APP_SECRET = "";//应用secret-----登录平台在应用设置可以找到
	public static String ACCESS_TOKEN = "";//访问令牌AT-------CC模式，AC模式都可，推荐CC模式获取令牌
	public static String TEMPLATE_ID = "";//模板ID


	
	public static String sendSms(String tel) throws Exception {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String timestamp = dateFormat.format(date);
		System.err.println(timestamp);
		//Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		//这里存放模板参数，如果模板没有参数直接用template_param={}
		map.put("param1", "test");
		map.put("param2", RandomUtil.randomFor6());
		map.put("param3","3" );

		String template_param = JsonUtil.toJsonString(map);
		//String template_param = gson.toJson(map);
		System.out.println(template_param);
		String postUrl = "http://api.189.cn/v2/emp/templateSms/sendSms";
		
		String postEntity = "app_id=" + APP_ID + "&access_token="
				+ ACCESS_TOKEN + "&acceptor_tel=" + tel + "&template_id="
				+ TEMPLATE_ID + "&template_param=" + template_param
				+ "&timestamp=" + URLEncoder.encode(timestamp, "utf-8");
		System.out.println(postUrl);
		System.out.println(postEntity);
		String resJson = "";
		String idertifier = null;
		Map<String, String> map2 =null;
		try {
			resJson = HttpInvoker.httpPost1(postUrl, null, postEntity);

			/*map2 = gson.fromJson(resJson,
					new TypeToken<Map<String, String>>() {
					}.getType());
			idertifier = map2.get("idertifier").toString();*/
			map2 = JsonUtil.toObject(resJson, new HashMap<String, String>().getClass());
		} catch (IOException e) {
			System.err.println(resJson);
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(resJson);
			e.printStackTrace();
		}
		System.err.println(resJson);
		return idertifier;

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String result = "";
		try {
				result = sendSms("18850748105");
				System.out.println(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
