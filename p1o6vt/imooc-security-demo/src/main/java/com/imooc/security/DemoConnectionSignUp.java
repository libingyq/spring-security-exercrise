/**
 * 
 */
package com.imooc.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * @author zhailiang
 *	解释：
 *		这样做的原因是：当用户进行使用第三方进行登录的时候 若是第一次就之久进入到首页 默认完成注册
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

	/* (non-Javadoc)
	 * @see org.springframework.social.connect.ConnectionSignUp#execute(org.springframework.social.connect.Connection)
	 */
	@Override
	public String execute(Connection<?> connection) {
		//根据社交用户信息默认创建用户并返回用户唯一标识
		return connection.getDisplayName();
	}

}
