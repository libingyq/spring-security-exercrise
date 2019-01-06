/**
 * 
 */
package com.imooc.security.core.social.qq.connet;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

import com.imooc.security.core.social.qq.api.QQ;
import com.imooc.security.core.social.qq.api.QQImpl;

/**
 * @author zhailiang
 *  构建：
 *      serviceProvider
 *      这里的泛型QQ 为自己的接口
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

	private String appId;
    /**
     * 获取授权码的地址
     *  将用户导向认证任务器的地址
     *  用户会进入这个地址
     *      用户会点击 "确定授权"  这步就是获取授权码
     */
	private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 拿着授权码去获取Token的地址
     */
	private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * 解释：
     *      这是使用的是父类的构造器
     *          appId:为应用的唯一标识  相当于该应用在QQ在注册的 "用户名"
     *          appSecret: 相当于该应用在QQ在注册的 "密码"
     * @param appId
     * @param appSecret
     */
	public QQServiceProvider(String appId, String appSecret) {
	    //这里使用的是默认的 OAuth2Template
		super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
		this.appId = appId;
	}
	
	@Override
	public QQ getApi(String accessToken) {
		return new QQImpl(accessToken, appId);
	}

}
