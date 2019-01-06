/**
 * 
 */
package com.imooc.security.core.social.qq.connet;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import com.imooc.security.core.social.qq.api.QQ;
import com.imooc.security.core.social.qq.api.QQUserInfo;

/**
 * @author zhailiang
 *  作用：
 *      将通过api获取到的用户个性化的信息 通过适配器转化为 Connection连接中要求的标准值
 */
public class QQAdapter implements ApiAdapter<QQ> {

	@Override
	public boolean test(QQ api) {
		return true;
	}

    /**
     * 将服务商提供的个性化的信息 通过适配器转化为标准化的信息
     * @param api
     * @param values
     */
	@Override
	public void setConnectionValues(QQ api, ConnectionValues values) {
		QQUserInfo userInfo = api.getUserInfo();
        //展示用的名称
		values.setDisplayName(userInfo.getNickname());
		//图片连接地址
		values.setImageUrl(userInfo.getFigureurl_qq_1());
		//主页中的时间线 （QQ没有 微博有）
		values.setProfileUrl(null);
		//服务商的id 也就是openId 就是QQ给别的第三方的client标识的唯一id
		values.setProviderUserId(userInfo.getOpenId());
	}

	@Override
	public UserProfile fetchUserProfile(QQ api) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(QQ api, String message) {
		//do noting
	}

}
