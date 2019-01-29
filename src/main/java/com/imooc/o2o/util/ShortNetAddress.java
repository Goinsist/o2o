package com.imooc.o2o.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShortNetAddress {
	private static Logger log = LoggerFactory.getLogger(ShortNetAddress.class);

	public static int TIMEOUT = 30 * 1000;
	public static String ENCODING = "UTF-8";



/**
*
* 功能描述:
* 根据传入的url，通过访问百度短链接的接口，将其转化成短的url
 * @return 
* @author gongyu
* @date 2019/1/1 0001 14:36
*/
	public static String getShortURL(String originURL) {
		String tinyUrl = null;
		 String token ="5e4ba3de2cfb3e0d2f7c14bfbaadc280";
		String params = "{\"url\":\""+ originURL + "\"}";

		BufferedReader reader = null;
		try {
			// 创建连接
			URL url = new URL("https://dwz.cn/admin/v2/create");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(TIMEOUT);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.setRequestProperty("Token", token); // 设置发送数据的格式");

			// 发起请求
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);
			out.flush();
			out.close();


			// 读取响应
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			String res = "";
			while ((line = reader.readLine()) != null) {
				res += line;
			}
			reader.close();

// 抽取生成短网址
			BaiduDwz.UrlResponse urlResponse = new Gson().fromJson(res, BaiduDwz.UrlResponse.class);
			if (urlResponse.getCode() == 0) {
				return urlResponse.getShortUrl();
			} else {
				System.out.println(urlResponse.getErrMsg());
			}

			return "短链接发送失败"; // TODO：自定义错误信息
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
		return "发送失败"; // TODO：自定义错误信息
	}


	/**
	 * ‘ 百度短链接接口 无法处理不知名网站，会安全识别报错
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String res = getShortURL("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2c605206217d88b5&redirect_uri=http://115.28.159.6/cityrun/wechatlogin.action&role_type=1&response_type=code&scope=snsapi_userinfo&state=STATE123qweasd#wechat_redirect");
		System.out.println(res);

	}
}
