/**
 * 
 */
package com.imooc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author zhailiang
 *	通过实现UserDetailsService来自定义用户信息获取逻辑
 *		已经通过实现UserDetails来获取用户信息
 *		然后通过PasswordEncoder来给用户的密码进行加密的操纵
 */
@Component
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * spring自带的密码加密的类
	 * 		1）encoder：用户在使用注册时候讲密码进行加密的操作
	 * 		2）match	es：匹配的操作 将用户登录的时候输入的密码和从数据库加密后的密码进行判断
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 * 这是表单登录的时候，接收到的username  表单中的用户名信息
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //这里在实际开发的时候 应该是数据库Dao层的操作
		logger.info("表单登录用户名:" + username);
		return buildUser(username);
	}

    /**
     * 这里的使用Social第三方社交的方式登录获取的到的信息 userId
     * @param userId
     * @return
     * @throws UsernameNotFoundException
     */
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.info("设计登录用户Id:" + userId);
		return buildUser(userId);
	}

	private SocialUserDetails buildUser(String userId) {
		// 根据用户名查找用户信息
		//根据查找到的用户信息判断用户是否被冻结
		//使用passwordEncoder使用encode的方式将用户的密码进行加密操作
		String password = passwordEncoder.encode("123456");
		logger.info("数据库密码是:"+password);
		return new SocialUser(userId, password,
				true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

}
