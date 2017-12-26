package com.sinochem.yunlian.upm.sso.util.RSAUtils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;


public class AES {




	public static final String CHAR_ENCODING = "UTF-8";
    public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
	/**
	 * 加密
	 * 
	 * @param / content
	 *            需要加密的内容
	 * @param /password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();

			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat,"AES");
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, seckey);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (Exception e){
			throw new RuntimeException("encrypt fail!", e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param / content
	 *            待解密内容
	 * @param /password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, seckey);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (Exception e){
			throw new RuntimeException("decrypt fail!", e);
		}
	}
	
	/*public static String encryptToBase64(String data, String key){
		try {
			byte[] valueByte = encrypt(data.getBytes(CHAR_ENCODING), key.getBytes(CHAR_ENCODING));
			return Base64Utils.encode(valueByte);
		} catch (Exception e) {
			throw new RuntimeException("encrypt fail!", e);
		}

	}*/

	/*public static String encryptData(String key, String content) {
		byte[] encryptedBytes = new byte[0];
		try {
			byte[] byteContent = content.getBytes("UTF-8");
			// 注意，为了能与 iOS 统一
			// 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
			byte[] enCodeFormat = key.getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			byte[] initParam = IV_STRING.getBytes();
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			// 指定加密的算法、工作模式和填充方式
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			encryptedBytes = cipher.doFinal(byteContent);
			// 同样对加密后数据进行 base64 编码
			return Base64Utils.encode(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	public static String decryptFromBase64(String data, String key){
		try {
			byte[] originalData = Base64Utils.decode(data);
			byte[] valueByte = decrypt(originalData, key.getBytes(CHAR_ENCODING));
			return new String(valueByte, CHAR_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		} catch (Exception e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	/*public static String encryptWithKeyBase64(String data, String key){
		try {
			byte[] valueByte = encrypt(data.getBytes(CHAR_ENCODING), Base64.decode(key.getBytes()));
			return new String(Base64.encode(valueByte));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}

	public static String decryptWithKeyBase64(String data, String key){
		try {
			byte[] originalData = Base64.decode(data.getBytes());
			byte[] valueByte = decrypt(originalData, Base64.decode(key.getBytes()));
			return new String(valueByte, CHAR_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}*/

	/*public static byte[] genarateRandomKey(){
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance(AES_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(" genarateRandomKey fail!", e);
		}
		SecureRandom random = new SecureRandom();
		keygen.init(random);
		Key key = keygen.generateKey();
		return key.getEncoded();
	}

	public static String genarateRandomKeyWithBase64(){
		return new String(Base64.encode(genarateRandomKey()));
	}
*/
	public static void main(String[] args) throws Exception {

		//Android
		String randomKeyAZ =
				"W74sVJWmtlS4wqcjrmyU5cIYRsmCc5NGQQ1udKkZ7oROcik96Nn2U9CMo3XpwIQul8bOj3ITqGuJwhcX4Wnuc1Uk2HGLtYO1rD4lgTsp+ggRzGSpXBI1FSTU9FL7TBetiN051ndUWrqXsTeFI2KCX+uQX7mpAG3YKQyMvU84d2I=";
		String keyAZ = RSAUtils.decryptByPrivateKey(randomKeyAZ);
		System.out.println("AN加密key:"+keyAZ);
		String contentAZ = "4qU/BRJx5TBDVIvulF9lIw==";
		String plainTextAZ = decryptFromBase64(contentAZ, keyAZ);
		System.out.println("AN解密数据: " + plainTextAZ);

		//IOS
		/*String randomKeyAIOS =
				"c9kEn3VZf6HiG8FCntaPkamGS6dqwgB3g12Tn5wwETAX8PVmlmWCYXyfn3ukTkmAjUVBdYkT0fZtRLpObrLOjz1vGmM15Yl1vF8SZ6HrBlal15\\/bkd4PV4FFgPlp\\/tFBkgvJjzOMzhxC377e1KLuWn6G8Hchjr86bPo4tF5Q1GA=";
		String keyIOS = RSAUtils.decryptByPrivateKey(randomKeyAIOS);
		System.out.println("IOS加密key:"+keyIOS);
		String contentIOS = "lKtm9L7PmZPpnfxROf5ebg==";
		String plainTextIOS = decryptFromBase64(contentIOS, "1111111111111111");
		System.out.println("IOS解密数据: " + plainTextIOS);*/
	}
		
}
