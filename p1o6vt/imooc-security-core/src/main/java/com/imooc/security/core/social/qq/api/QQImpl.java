/**
 * 
 */
package com.imooc.security.core.social.qq.api;

import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhailiang
 *  这里就是获取用户信息的接口实现
 *  QQ登录实现：
 *      1.去往QQ互联上查看获取详情
 *      2.共有三个必须要传入的参数
 *          1：Access_Token（3个月的有效期）（需要传入父类中AbstarctOAuth2ApiBinding中的final类型的变量）
 *          2:openId（与QQ号码一一对应是 通过Access_Token来获取）
 *          3.appid 登录QQ成功后 分配给应用的appid 需要在QQ互联上注册获取
 *      注意：
 *          因为每个用户在使用这个类 获取认证信息的和用户信息都是不一样的 所以这个类要是多实例的 不能是单利方式
 *          所以不能使用@component（因为要是使用这个注解就会将该类变为单实例的）
 *
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    //这里是通过accessToken来获取appId
	private static final String URL_GET_OPENID = "htt/ps://graph.qq.com/oauth2.0me?access_token=%s";

	//然后通过appid和openid来获取用户信息
	private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
	
	private String appId;
	
	private String openId;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public QQImpl(String accessToken, String appId) {
	    //这里是使用的父类的构造器来初始化accessToken  accessToken用来存放获取用户信息的令牌  每个用户的令牌都是不一样的
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
		
		this.appId = appId;
		
		String url = String.format(URL_GET_OPENID, accessToken);
		//这里使用的父类中AbstractOAuth2ApiBinding中的全局变量restTemplate的来向服务提供商发送请求获取用户信息的
		String result = getRestTemplate().getForObject(url, String.class);

		System.out.println(result);
		
		this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
	}
	
	/* (non-Javadoc)
	 * @see com.imooc.security.core.social.qq.api.QQ#getUserInfo()
	 */
	@Override
	public QQUserInfo getUserInfo() {
		
		String url = String.format(URL_GET_USERINFO, appId, openId);
		String result = getRestTemplate().getForObject(url, String.class);
		
		System.out.println(result);
		
		QQUserInfo userInfo = null;
		try {
			userInfo = objectMapper.readValue(result, QQUserInfo.class);
			userInfo.setOpenId(openId);
			return userInfo;
		} catch (Exception e) {
			throw new RuntimeException("获取用户信息失败", e);
		}
	}

}
